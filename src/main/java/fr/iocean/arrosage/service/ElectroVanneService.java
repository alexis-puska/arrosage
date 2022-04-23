package fr.iocean.arrosage.service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import fr.iocean.arrosage.RelayStatusEnum;
import fr.iocean.arrosage.config.ApplicationProperties;
import fr.iocean.arrosage.service.dto.Relay;
import fr.iocean.arrosage.service.dto.RelayDTO;
import fr.iocean.arrosage.service.dto.StatusRelayDTO;

@Service
public class ElectroVanneService {

	private final Logger log = LoggerFactory.getLogger(ElectroVanneService.class);
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	private final long tempsArrosage;
	private final long tempsSecurite;

	private final ApplicationProperties applicationProperties;
	private final ScheduledExecutorService executor;
	private final GpioController gpio;
	private Map<Integer, Relay> relays;

	public ElectroVanneService(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
		this.tempsArrosage = this.applicationProperties.getTempsArrosage();
		this.tempsSecurite = this.applicationProperties.getTempsSecurite();
		this.executor = Executors.newSingleThreadScheduledExecutor();
		gpio = GpioFactory.getInstance();
		this.relays = new HashMap<>();
		this.log.info("+-----------------------------------+");
		this.log.info("| initialisation relays !           |");
		this.log.info("+-----------------------------------+");
		this.initPinConfig(1, "portail        ", RaspiPin.GPIO_21);
		this.initPinConfig(2, "piscine        ", RaspiPin.GPIO_22);
		this.initPinConfig(3, "pergolas       ", RaspiPin.GPIO_23);
		this.initPinConfig(4, "abris          ", RaspiPin.GPIO_27);
		this.initPinConfig(5, "Goutte à goutte", RaspiPin.GPIO_24);
		//this.initPinConfig(6, "Goutte à goutte", RaspiPin.gpio_28);
		//this.initPinConfig(7, "Goutte à goutte", RaspiPin.gpio_29);
		//this.initPinConfig(8, "Goutte à goutte", RaspiPin.gpio_25);
		this.log.info("+-----------------------------------+");
		this.log.info("| initialisation relays terminées ! |");
		this.log.info("+-----------------------------------+");

	}

	private void initPinConfig(int relay, String zone, Pin pin) {
		GpioPinDigitalOutput conf = gpio.provisionDigitalOutputPin(pin, "MyLED", PinState.HIGH);
		this.relays.put(relay, new Relay(relay, zone, conf, null, null, null, null));
		this.log.info("nouvelle zone : {} {}", relay, zone);
	}

	public synchronized void openVanneScheduled(int id) {
		long decallage = 0;
		Instant start = getNextStart();
		if (start == null) {
			// allumage immédiat !
			this.relays.get(id).getConf().low();
			this.log.info("ouverture immediat vanne : {}", id);
		} else {
			start = start.plusMillis(tempsSecurite);
			decallage = Math.abs(start.until(Instant.now(), ChronoUnit.MILLIS));
			this.log.info("ouverture différée vanne : {}, à : {}", id,
					formatter.format(ZonedDateTime.ofInstant(start, ZoneOffset.systemDefault())));
			this.relays.get(id).setStartHours(start);
			ScheduledFuture<?> ft = executor.schedule(new AutoStartVanneTask(id, this), decallage,
					TimeUnit.MILLISECONDS);
			this.relays.get(id).setStart(ft);
		}
		this.relays.get(id).setStopHours(Instant.now().plusMillis(tempsArrosage + decallage));
		ScheduledFuture<?> ft = executor.schedule(new AutoStopVanneTask(id, this), tempsArrosage + decallage,
				TimeUnit.MILLISECONDS);
		this.relays.get(id).setStop(ft);
		this.logStatus();
	}

	public synchronized void closeVanneScheduled(int id) {
		Relay relay = this.relays.get(id);
		this.cancelRelay(relay);
		this.logStatus();
	}

	public synchronized void addTime(int id) {
		for (Relay relay : this.relays.values()) {
			if (relay.getStart() != null && !relay.getStart().isDone()) {
				relay.getStart().cancel(true);
			}
			if (relay.getStop() != null && !relay.getStop().isDone()) {
				relay.getStop().cancel(true);
			}
		}
		Relay r = this.relays.get(id);

		for (Relay relay : this.relays.values()) {
			if (relay.getId() != id) {
				if (relay.getStartHours() != null && relay.getStartHours().isAfter(r.getStopHours())) {
					relay.setStartHours(relay.getStartHours().plusMillis(tempsArrosage));
				}
				if (relay.getStopHours() != null && relay.getStopHours().isAfter(r.getStopHours())) {
					relay.setStopHours(relay.getStopHours().plusMillis(tempsArrosage));
				}
			}
		}
		r.setStopHours(r.getStopHours().plusMillis(tempsArrosage));

		for (Relay relay : this.relays.values()) {
			if (relay.getStartHours() != null) {
				long decallage = Math.abs(relay.getStartHours().until(Instant.now(), ChronoUnit.MILLIS));
				ScheduledFuture<?> ft = executor.schedule(new AutoStartVanneTask(relay.getId(), this), decallage,
						TimeUnit.MILLISECONDS);
				relay.setStart(ft);
			}
			if (relay.getStopHours() != null) {
				long decallage = Math.abs(relay.getStopHours().until(Instant.now(), ChronoUnit.MILLIS));
				ScheduledFuture<?> ft = executor.schedule(new AutoStopVanneTask(relay.getId(), this), decallage,
						TimeUnit.MILLISECONDS);
				relay.setStop(ft);
			}
		}
		this.logStatus();
	}

	public synchronized void cancel(int id) {
		Relay relay = this.relays.get(id);
		if (relay.getStopHours() == null) {
			log.info("Can't cancel a scheduled relay without end hours");
		} else {
			this.cancelRelay(relay);
		}
		this.logStatus();
	}

	public List<StatusRelayDTO> getStatus() {
		return relays.entrySet().stream().map(entry -> {
			StatusRelayDTO dto = new StatusRelayDTO();
			Relay relay = entry.getValue();
			dto.setId(relay.getId());
			dto.setZone(entry.getValue().getZone());
			if (entry.getValue().getConf().isLow()) {
				dto.setRemainingTime(Instant.now().until(relay.getStopHours(), ChronoUnit.MILLIS));
				dto.setStatus(RelayStatusEnum.ON);
			} else if (relay.getStartHours() != null) {
				dto.setRemainingTime(Instant.now().until(relay.getStartHours(), ChronoUnit.MILLIS));
				dto.setStatus(RelayStatusEnum.WAIT);
			} else {
				dto.setStatus(RelayStatusEnum.OFF);
			}
			dto.setEstimatedStartHours(relay.getStartHours());
			dto.setEstimatedStopHours(relay.getStopHours());
			return dto;
		}).sorted((v1, v2) -> v1.getId() - v2.getId()).collect(Collectors.toList());
	}

	public void cancelAll() {
		this.relays.entrySet().forEach(entry -> {
			this.cancelRelay(entry.getValue());
		});
		this.logStatus();
	}

	private void cancelRelay(Relay relay) {

		if (relay.getStart() != null && !relay.getStart().isDone()) {
			relay.getStart().cancel(true);
		}
		if (relay.getStop() != null && !relay.getStop().isDone()) {
			relay.getStop().cancel(true);
		}
		relay.setStart(null);
		relay.setStop(null);
		relay.setStartHours(null);
		relay.setStopHours(null);
		relay.getConf().high();
	}

	private Instant getNextStart() {
		Instant next = null;
		for (Relay relay : this.relays.values()) {
			if (relay.getStop() != null) {
				if (next == null || relay.getStopHours().isAfter(next)) {
					next = relay.getStopHours();
				}
			}
		}
		return next;
	}

	public void openVanneByTask(int id) {
		this.relays.get(id).getConf().low();
		this.relays.get(id).setStart(null);
		this.relays.get(id).setStartHours(null);
		this.logStatus();
	}

	public void closeVanneByTask(int id) {
		this.relays.get(id).getConf().high();
		this.relays.get(id).setStop(null);
		this.relays.get(id).setStopHours(null);
		this.logStatus();
	}

    public List<RelayDTO> getListRelay() {
        List<RelayDTO> info = this.relays.values().stream().map(relay -> {
            return new RelayDTO(relay.getId(), relay.getZone());
        }).collect(Collectors.toList());
        Collections.sort(info);
        return info;
    }

	private void logStatus() {
		log.info("+------------------------------------------------------------------------------------------------+");
		this.getStatus().stream().sorted((r1, r2) -> {
			if (r1.getEstimatedStopHours() == null) {
				return -1;
			}
			if (r2.getEstimatedStopHours() == null) {
				return 0;
			}
			if (r1.getEstimatedStopHours().isBefore(r2.getEstimatedStopHours())) {
				return -1;
			}
			if (r1.getEstimatedStopHours().isAfter(r2.getEstimatedStopHours())) {
				return 1;
			}
			return 0;
		}).forEach(status -> log.info("| zone : {} |", status.printInfo()));
		log.info("+------------------------------------------------------------------------------------------------+");
	}

	@PreDestroy
	public void destroy() {
		this.gpio.shutdown();
	}
}
