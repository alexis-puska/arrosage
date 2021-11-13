package fr.iocean.arrosage.service.dto;

import java.io.Serializable;
import java.time.Instant;

import fr.iocean.arrosage.RelayStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StatusRelayDTO implements Serializable {

	private static final long serialVersionUID = 7096062524743069455L;

	private int id;
	private String zone;
	private RelayStatusEnum Status;
	private Long remainingTime;
	private Instant estimatedStopHours;
	private Instant estimatedStartHours;
}
