package fr.iocean.arrosage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.iocean.arrosage.config.ApplicationProperties;
import fr.iocean.arrosage.repository.BlackListRepository;
import fr.iocean.arrosage.repository.ProgrammationRepository;
import fr.iocean.arrosage.service.dto.MetricDTO;

@Service
@Transactional
public class HomeService {

    private final Logger log = LoggerFactory.getLogger(BlackListService.class);

    private final BlackListRepository blackListRepository;
    private final ApplicationProperties applicationProperties;
    private final ProgrammationRepository programmationRepository;

    public HomeService(BlackListRepository blackListRepository, ApplicationProperties applicationProperties,
            ProgrammationRepository programmationRepository) {
        this.blackListRepository = blackListRepository;
        this.applicationProperties = applicationProperties;
        this.programmationRepository = programmationRepository;
    }

    public MetricDTO getMetrics() {
        MetricDTO dto = new MetricDTO();
        dto.setBlackList((int) this.blackListRepository.count());
        dto.setProgrammation((int) this.programmationRepository.count());
        dto.setZone(this.applicationProperties.getRelays().size());
        return dto;
    }

}
