package fr.iocean.arrosage.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.iocean.arrosage.domain.Programmation;
import fr.iocean.arrosage.repository.ProgrammationRepository;
import fr.iocean.arrosage.service.dto.ProgrammationDTO;
import fr.iocean.arrosage.service.mapper.ProgrammationMapper;

/**
 * Service Implementation for managing {@link Programmation}.
 */
@Service
@Transactional
public class ProgrammationService {

    private final Logger log = LoggerFactory.getLogger(ProgrammationService.class);

    private final ElectroVanneService electroVanneService;

    private final ProgrammationRepository programmationRepository;

    private final ProgrammationMapper programmationMapper;

    public ProgrammationService(ElectroVanneService electroVanneService,
            ProgrammationRepository programmationRepository, ProgrammationMapper programmationMapper) {
        this.electroVanneService = electroVanneService;
        this.programmationRepository = programmationRepository;
        this.programmationMapper = programmationMapper;
    }

    /**
     * Save a programmation.
     *
     * @param programmationDTO the entity to save.
     * @return the persisted entity.
     */
    public ProgrammationDTO save(ProgrammationDTO programmationDTO) {
        log.debug("Request to save Programmation : {}", programmationDTO);
        Programmation programmation = programmationMapper.toEntity(programmationDTO);
        programmation = programmationRepository.save(programmation);
        return programmationMapper.toDto(programmation);
    }

    /**
     * Get all the programmations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProgrammationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Programmations");
        return programmationRepository.findAll(pageable).map(programmationMapper::toDto);
    }

    /**
     * Get one programmation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProgrammationDTO> findOne(Long id) {
        log.debug("Request to get Programmation : {}", id);
        return programmationRepository.findById(id).map(programmationMapper::toDto);
    }

    /**
     * Delete the programmation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Programmation : {}", id);
        programmationRepository.deleteById(id);
    }

    @Scheduled(cron = "0 * * ? * *")
    public void launch() {
        ZonedDateTime now = ZonedDateTime.now();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now.toInstant(), ZoneOffset.UTC);
        List<Programmation> programmations = this.programmationRepository
                .getProgrammationByHourAndMinute(localDateTime.getHour(), localDateTime.getMinute());
        programmations = programmations.stream().map(programmation -> {
            if(ZonedDateTime.now().until(programmation.getDate(), ChronoUnit.DAYS) % programmation.getDayFrequency() == 0) {
                System.out.println("je me lance");
                String sequence = programmation.getSequence();
                List<Integer> relayIds = new ArrayList<>(Arrays.asList(sequence.split(","))).stream()
                        .map(id -> Integer.parseInt(id)).collect(Collectors.toList());
    
                List<Integer> toStart = new ArrayList<>();
                List<Integer> toProlong = new ArrayList<>();
    
                relayIds.stream().forEach(id -> {
                    if (toStart.contains(id)) {
                        toProlong.add(id);
                    } else {
                        toStart.add(id);
                    }
                });
    
                toStart.stream().forEach(id -> {
                    log.info("cron start avec id : {}", id);
                    this.electroVanneService.openVanneScheduled(id);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                });
    
                toProlong.stream().forEach(id -> {
                    log.info("cron prolongation avec id : {}", id);
                    this.electroVanneService.addTime(id);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                });
                programmation.setCounter(programmation.getCounter() != null ? programmation.getCounter() + 1 : 1);
            }
            return programmation;
        }).collect(Collectors.toList());
        this.programmationRepository.saveAll(programmations);
        this.programmationRepository.pruneProgrammation();
    }
}
