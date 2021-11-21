package fr.iocean.arrosage.service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.iocean.arrosage.config.ApplicationProperties;
import fr.iocean.arrosage.domain.BlackList;
import fr.iocean.arrosage.repository.BlackListRepository;
import fr.iocean.arrosage.service.dto.BlackListDTO;
import fr.iocean.arrosage.service.mapper.BlackListMapper;

/**
 * Service Implementation for managing {@link BlackList}.
 */
@Service
@Transactional
public class BlackListService {

	private final Logger log = LoggerFactory.getLogger(BlackListService.class);

	private final BlackListRepository blackListRepository;
	private final BlackListMapper blackListMapper;
	private final ApplicationProperties applicationProperties;

	public BlackListService(BlackListRepository blackListRepository, BlackListMapper blackListMapper,
			ApplicationProperties applicationProperties) {
		this.blackListRepository = blackListRepository;
		this.blackListMapper = blackListMapper;
		this.applicationProperties = applicationProperties;
	}

	/**
	 * Get all the blackLists.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<BlackListDTO> findAll(Pageable pageable) {
		log.debug("Request to get all BlackLists");
		return blackListRepository.findAll(pageable).map(blackListMapper::toDto);
	}

	/**
	 * Delete the blackList by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete BlackList : {}", id);
		blackListRepository.deleteById(id);
	}

	public boolean isAdresseLocked(String remoteAddr) {
		Optional<BlackList> blackListOptional = this.blackListRepository.findByIpAddress(remoteAddr);
		if (blackListOptional.isPresent()) {
			BlackList blackList = blackListOptional.get();
			if (blackList.getLocked() == true) {
				if (ZonedDateTime.now().isBefore(blackList.getUnlockDate())) {
					return true;
				} else {
					this.blackListRepository.delete(blackList);
				}
			}
		}
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateLastTry(String remoteAddr) {
		Optional<BlackList> blackListOptional = this.blackListRepository.findByIpAddress(remoteAddr);
		BlackList blackList;
		if (blackListOptional.isPresent()) {
			blackList = blackListOptional.get();
			blackList.setCount(blackList.getCount() + 1);
			if (blackList.getCount() > this.applicationProperties.getMaxLoginTry()) {
				blackList.setUnlockDate(
						ZonedDateTime.now().plus(this.applicationProperties.getBanTime(), ChronoUnit.MILLIS));
				blackList.setLocked(true);
				blackList.setLastTry(ZonedDateTime.now());
			}
		} else {
			blackList = new BlackList();
			blackList.setIpAddress(remoteAddr);
			blackList.setCount(1l);
			blackList.setLastTry(ZonedDateTime.now());
			blackList.setLocked(false);
		}
		this.blackListRepository.save(blackList);
	}
}
