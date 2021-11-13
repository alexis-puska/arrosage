package fr.iocean.arrosage.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import fr.iocean.arrosage.RelayStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatusRelayDTO implements Serializable {

	private static final long serialVersionUID = 7096062524743069455L;

	private int id;
	private String zone;
	private RelayStatusEnum Status;
	private Long remainingTime;
	private Instant estimatedStopHours;
	private Instant estimatedStartHours;

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String value = id + " " + zone + " : " + Status + "\t";
		if (estimatedStartHours != null) {
			value += "départ : " + formatter.format(LocalDateTime.ofInstant(estimatedStartHours, ZoneOffset.UTC))
					+ "\t";
		}
		if (estimatedStopHours != null) {
			value += "arrêt  : " + formatter.format(LocalDateTime.ofInstant(estimatedStopHours, ZoneOffset.UTC));
		}
		return value;
	}

}
