package fr.iocean.arrosage.service.dto;

import java.io.Serializable;

import com.pi4j.io.gpio.digital.DigitalOutput;

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

	private int relay;
	private String zone;
	private DigitalOutput conf;
}
