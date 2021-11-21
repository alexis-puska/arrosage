package fr.iocean.arrosage.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link fr.iocean.arrosage.domain.BlackList} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class BlackListDTO implements Serializable {

	private static final long serialVersionUID = 3505767965101071272L;

	private Long id;
	private String ipAddress;
	private Boolean locked;
	private ZonedDateTime unlockDate;
	private ZonedDateTime lastTry;
	private Long count;
}
