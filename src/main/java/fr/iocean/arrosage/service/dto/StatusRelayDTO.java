package fr.iocean.arrosage.service.dto;

import java.io.Serializable;
import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatusRelayDTO implements Serializable {

	private static final long serialVersionUID = 7096062524743069455L;

	private int relay;
	private String zone;
	private boolean on;
	private Long remainingTime;
	private Instant estimatedHours;
}
