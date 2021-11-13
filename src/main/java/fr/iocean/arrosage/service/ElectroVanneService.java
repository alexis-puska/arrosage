package fr.iocean.arrosage.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.platform.Platforms;

import fr.iocean.arrosage.RelayStatusEnum;
import fr.iocean.arrosage.config.ApplicationProperties;
import fr.iocean.arrosage.service.dto.Relay;
import fr.iocean.arrosage.service.dto.StatusRelayDTO;

@Service
public class ElectroVanneService {

	private final Logger log = LoggerFactory.getLogger(ElectroVanneService.class);

	private final long tempsArrosage;
	private final long tempsSecurite;

	private final ApplicationProperties applicationProperties;
	private final ScheduledExecutorService executor;
	private final Context pi4j;
	private final Platforms platforms;
	private Map<Integer, Relay> relays;

	public ElectroVanneService(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
		this.tempsArrosage = this.applicationProperties.getTempsArrosage();
		this.tempsSecurite = this.applicationProperties.getTempsSecurite();
		this.executor = Executors.newSingleThreadScheduledExecutor();
		this.pi4j = Pi4J.newAutoContext();
		this.platforms = pi4j.platforms();
		this.platforms.describe().print(System.out);
		this.relays = new HashMap<>();
		this.initPinConfig(1, "portail        ", 29);
		this.initPinConfig(2, "piscine        ", 31);
		this.initPinConfig(3, "pergolas       ", 33);
		this.initPinConfig(4, "abris          ", 34);
		this.initPinConfig(5, "Goutte à goutte", 35);
	}

	private void initPinConfig(int relay, String zone, int pin) {
		DigitalOutputConfigBuilder ledConfig = DigitalOutput.newConfigBuilder(pi4j).id("relay" + relay)
				.name("relay" + relay).address(pin).shutdown(DigitalState.LOW).initial(DigitalState.LOW);
		DigitalOutput conf = pi4j.create(ledConfig);
		this.relays.put(relay, new Relay(relay, zone, conf, null, null, null, null));
	}

	public synchronized void openVanneScheduled(int id) {
		long decallage = 0;
		Instant start = getNextStart();
		if (start == null) {
			// allumage immédiat !
			this.relays.get(id).getConf().high();
			this.log.info("ouverture immediat vanne : {}", id);
		} else {
			start = start.plusMillis(tempsSecurite);
			decallage = Math.abs(start.until(Instant.now(), ChronoUnit.MILLIS));
			this.log.info("ouverture différée vanne : {}, à : {}", id, start);
			this.relays.get(id).setStartHours(start);
			ScheduledFuture<?> ft = executor.schedule(new AutoStartVanneTask(id, this), decallage,
					TimeUnit.MILLISECONDS);
			this.relays.get(id).setStart(ft);
		}
		this.relays.get(id).setStopHours(Instant.now().plusMillis(tempsArrosage + decallage));
		ScheduledFuture<?> ft = executor.schedule(new AutoStopVanneTask(id, this), tempsArrosage + decallage,
				TimeUnit.MILLISECONDS);
		this.relays.get(id).setStop(ft);
	}

	public synchronized void closeVanneScheduled(int id) {
		Relay relay = this.relays.get(id);
		this.cancelRelay(relay);
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
	}

	public synchronized void cancel(int id) {
		Relay relay = this.relays.get(id);
		if (relay.getStopHours() == null) {
			log.info("Can't cancel a scheduled relay without end hours");
		} else {
			this.cancelRelay(relay);
		}
	}

	public List<StatusRelayDTO> getStatus() {
		return relays.entrySet().stream().map(entry -> {
			StatusRelayDTO dto = new StatusRelayDTO();
			Relay relay = entry.getValue();
			dto.setId(relay.getId());
			dto.setZone(entry.getValue().getZone());
			if (entry.getValue().getConf().isHigh()) {
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
		relay.getConf().low();
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
		this.relays.get(id).getConf().high();
		this.relays.get(id).setStart(null);
		this.relays.get(id).setStartHours(null);
	}

	public void closeVanneByTask(int id) {
		this.relays.get(id).getConf().low();
		this.relays.get(id).setStop(null);
		this.relays.get(id).setStopHours(null);
	}

	@PreDestroy
	public void destroy() {
		this.pi4j.shutdown();
	}
}
