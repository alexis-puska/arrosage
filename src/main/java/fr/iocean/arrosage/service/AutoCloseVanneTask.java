package fr.iocean.arrosage.service;

import java.time.LocalDateTime;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AutoCloseVanneTask extends TimerTask {

	private final Logger log = LoggerFactory.getLogger(AutoCloseVanneTask.class);

	private int relay;
	private ElectroVanneService electroVanneService;

	public void run() {
		log.info("Fermeture automatique vanne : {}, à : {}", relay, LocalDateTime.now().toString());
		this.electroVanneService.closeVanne(this.relay);
	}
}
