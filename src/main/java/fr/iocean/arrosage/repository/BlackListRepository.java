package fr.iocean.arrosage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.iocean.arrosage.domain.BlackList;

/**
 * Spring Data  repository for the BlackList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {

	Optional<BlackList> findByIpAddress(String remoteAddr);
}
