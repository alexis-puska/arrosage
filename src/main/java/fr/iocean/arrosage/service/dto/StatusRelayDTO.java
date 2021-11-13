package fr.iocean.arrosage.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
		String value = id + " " + zone + " | " + Status.getValueString();
		if (estimatedStartHours != null) {
			value += "| départ : "
					+ formatter.format(ZonedDateTime.ofInstant(estimatedStartHours, ZoneOffset.systemDefault())) + " ";
		} else {
			value += "|                              ";
		}
		if (estimatedStopHours != null) {
			value += "| arrêt  : "
					+ formatter.format(ZonedDateTime.ofInstant(estimatedStopHours, ZoneOffset.systemDefault())) + " ";
		} else {
			value += "|                              ";
		}
		return value;
	}
}
