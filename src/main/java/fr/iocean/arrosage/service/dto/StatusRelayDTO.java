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

	public String printInfo() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String value = id + " " + zone + " : " + Status + "\t";
		if (estimatedStartHours != null) {
			value += "| départ : " + formatter.format(LocalDateTime.ofInstant(estimatedStartHours, ZoneOffset.UTC))
					+ " ";
		} else {
			value += "|                              ";
		}
		if (estimatedStopHours != null) {
			value += "| arrêt  : " + formatter.format(LocalDateTime.ofInstant(estimatedStopHours, ZoneOffset.UTC))
					+ " ";
		} else {
			value += "|                              ";
		}
		return value;
	}
}
