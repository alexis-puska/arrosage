package fr.iocean.arrosage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.iocean.arrosage.domain.Programmation;

/**
 * Spring Data repository for the Programmation entity.
 */
@Repository
public interface ProgrammationRepository extends JpaRepository<Programmation, Long> {

    @Query("SELECT p FROM Programmation p WHERE hour(date) = :hour AND minute(date) = :minute")
    public List<Programmation> getProgrammationByHourAndMinute(@Param("hour") int hour, @Param("minute") int minute);

    @Modifying
    @Query("DELETE FROM Programmation WHERE day <= counter")
    public void pruneProgrammation();

}
