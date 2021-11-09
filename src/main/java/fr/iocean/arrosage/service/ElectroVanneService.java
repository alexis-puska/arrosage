package fr.iocean.arrosage.service;

import java.time.Instant;
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

import fr.iocean.arrosage.service.dto.Relay;
import fr.iocean.arrosage.service.dto.StatusRelayDTO;

@Service
public class ElectroVanneService {

	private final Logger log = LoggerFactory.getLogger(ElectroVanneService.class);

	private static final long tempsArrosageInMs = 10000;

	private final ScheduledExecutorService executor;
	private final Context pi4j;
	private final Platforms platforms;
	private Map<Integer, Relay> relays;
	private Map<Integer, ScheduledFuture<?>> timers;

	public ElectroVanneService() {
		this.executor = Executors.newSingleThreadScheduledExecutor();
		this.pi4j = Pi4J.newAutoContext();
		this.platforms = pi4j.platforms();
		this.platforms.describe().print(System.out);
		this.relays = new HashMap<>();
		this.timers = new HashMap<>();
		this.initPinConfig(1, "portail", 29);
		this.initPinConfig(2, "piscine", 31);
		this.initPinConfig(3, "pergolas", 33);
		this.initPinConfig(4, "vide sanitaire", 34);
		this.initPinConfig(5, "abris", 35);
	}

	private void initPinConfig(int relay, String zone, int pin) {
		DigitalOutputConfigBuilder ledConfig = DigitalOutput.newConfigBuilder(pi4j).id("relay" + relay)
				.name("relay" + relay).address(pin).shutdown(DigitalState.LOW).initial(DigitalState.LOW);
		DigitalOutput conf = pi4j.create(ledConfig);
		this.relays.put(relay, new Relay(relay, zone, conf));
	}

	public synchronized void openVanne(int relay) {
		this.relays.get(relay).getConf().high();
		ScheduledFuture<?> ft = this.timers.get(relay);
		if (ft == null || ft.isDone()) {
			log.info("pas de timer ou timer terminé : creation");
			ft = executor.schedule(new AutoCloseVanneTask(relay, this), tempsArrosageInMs, TimeUnit.MILLISECONDS);
		} else if (!ft.isDone()) {
			log.info("timer present : ajout de temps");
			long remainingTime = ft.getDelay(TimeUnit.MILLISECONDS);
			if (ft.cancel(true)) {
				ft = executor.schedule(new AutoCloseVanneTask(relay, this), tempsArrosageInMs + remainingTime,
						TimeUnit.MILLISECONDS);
			}
		}
		this.timers.put(relay, ft);
	}

	public synchronized void closeVanne(int relay) {
		this.relays.get(relay).getConf().low();
		if (timers.containsKey(relay)) {
			ScheduledFuture<?> ft = this.timers.get(relay);
			if (ft.isDone()) {
				log.info("close vanne timer terminé : suppresion");
				this.timers.remove(relay);
			} else if (!ft.isDone()) {
				long remainingTime = ft.getDelay(TimeUnit.MILLISECONDS);
				log.info("timer present : {} ms restant", remainingTime);
				if (ft.cancel(true)) {
					log.info("close vanne timer terminé : suppresion");
					this.timers.remove(relay);
				}
			}
		}
	}

	public void cancelAll() {
		this.timers.entrySet().stream().forEach(entry -> {
			if (entry.getValue() != null && !entry.getValue().isDone()) {
				entry.getValue().cancel(true);
			}
		});
		this.relays.entrySet().stream().forEach(entry -> {
			entry.getValue().getConf().low();
		});

	}

	public List<StatusRelayDTO> getStatus() {
		return relays.entrySet().stream().map(entry -> {
			StatusRelayDTO dto = new StatusRelayDTO();
			int relay = entry.getValue().getRelay();
			dto.setRelay(relay);
			dto.setZone(entry.getValue().getZone());
			dto.setOn(entry.getValue().getConf().isHigh());
			if (this.timers.containsKey(relay)) {
				ScheduledFuture<?> t = this.timers.get(relay);
				if (!t.isDone()) {
					long delai = t.getDelay(TimeUnit.MILLISECONDS);
					dto.setRemainingTime(delai);
					dto.setEstimatedHours(Instant.now().plusMillis(delai));
				} else {
					dto.setRemainingTime(null);
				}
			} else {
				dto.setRemainingTime(null);
			}
			return dto;
		}).sorted((v1, v2) -> v1.getRelay() - v2.getRelay()).collect(Collectors.toList());
	}

	@PreDestroy
	public void destroy() {
		this.pi4j.shutdown();
	}

}
