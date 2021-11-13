package fr.iocean.arrosage.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AutoStopVanneTask extends TimerTask {

	private final Logger log = LoggerFactory.getLogger(AutoStopVanneTask.class);
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	private int relay;
	private ElectroVanneService electroVanneService;

	public void run() {
		log.info("Fermeture automatique vanne : {}, à : {}", relay, formatter.format(LocalDateTime.now()));
		this.electroVanneService.closeVanneByTask(this.relay);
	}
}
