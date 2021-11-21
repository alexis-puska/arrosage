package fr.iocean.arrosage.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A BlackList.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "black_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BlackList implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ip_address")
	private String ipAddress;

	@Column(name = "locked")
	private Boolean locked;

	@Column(name = "unlock_date")
	private ZonedDateTime unlockDate;

	@Column(name = "last_try")
	private ZonedDateTime lastTry;

	@Column(name = "count")
	private Long count;

}
