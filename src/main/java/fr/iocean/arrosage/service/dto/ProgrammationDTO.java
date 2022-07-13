package fr.iocean.arrosage.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * A DTO for the {@link fr.iocean.arrosage.domain.Programmation} entity.
 */
public class ProgrammationDTO implements Serializable {

    private static final long serialVersionUID = -6330013069998609049L;

    private Long id;

    @NotNull
    private ZonedDateTime date;

    @NotNull
    @Max(value = 30)
    private Integer day;

    @NotNull
    @Pattern(regexp = "^(?!,$)[\\d,]+$")
    private String sequence;

    private Integer counter;
    
    private Integer dayFrequency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

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
        if (!(o instanceof ProgrammationDTO)) {
            return false;
        }

        return id != null && id.equals(((ProgrammationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgrammationDTO{" + "id=" + getId() + ", date='" + getDate() + "'" + ", day=" + getDay()
                + ", sequence='" + getSequence() + "'" + ", counter=" + getCounter()+ ", dayFrequency=" + getDayFrequency() + "}";
    }
}
