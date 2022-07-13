package fr.iocean.arrosage.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Programmation.
 */
@Entity
@Table(name = "programmation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Programmation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Max(value = 30)
    @Column(name = "day", nullable = false)
    private Integer day;

    @NotNull
    @Pattern(regexp = "^(?!,$)[\\d,]+$")
    @Column(name = "sequence", nullable = false)
    private String sequence;

    @Column(name = "counter")
    private Integer counter;
    
    @NotNull
    @Max(value = 30)
    @Min(value = 1)
    @Column(name = "day_frequency", nullable = false)
    private Integer dayFrequency;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Programmation date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getDay() {
        return day;
    }

    public Programmation day(Integer day) {
        this.day = day;
        return this;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getSequence() {
        return sequence;
    }

    public Programmation sequence(String sequence) {
        this.sequence = sequence;
        return this;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Integer getCounter() {
        return counter;
    }

    public Programmation counter(Integer counter) {
        this.counter = counter;
        return this;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Integer getDayFrequency() {
        return dayFrequency;
    }

    public void setDayFrequency(Integer dayFrequency) {
        this.dayFrequency = dayFrequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Programmation)) {
            return false;
        }
        return id != null && id.equals(((Programmation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Programmation{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", day=" + getDay() +
            ", sequence='" + getSequence() + "'" +
            ", counter=" + getCounter() +
            ", dayFrequency=" + getDayFrequency() +
            "}";
    }
}
