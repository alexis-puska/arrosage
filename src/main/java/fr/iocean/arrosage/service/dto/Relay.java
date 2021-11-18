package fr.iocean.arrosage.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Relay implements Serializable {

	private static final long serialVersionUID = 8798629590939511863L;

	private int id;
	private String zone;
	private GpioPinDigitalOutput conf;
	private Instant startHours;
	private ScheduledFuture<?> start;
	private Instant stopHours;
	private ScheduledFuture<?> stop;
}
