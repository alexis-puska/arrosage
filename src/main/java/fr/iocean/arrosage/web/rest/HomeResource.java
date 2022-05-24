package fr.iocean.arrosage.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.iocean.arrosage.service.HomeService;
import fr.iocean.arrosage.service.dto.MetricDTO;

@RestController
@RequestMapping("/api")
public class HomeResource {

    private final Logger log = LoggerFactory.getLogger(ProgrammationResource.class);

    private final HomeService homeService;

    public HomeResource(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home/metrics")
    public MetricDTO getMetrics() {
        this.log.info("get metrics");
        return this.homeService.getMetrics();
    }
}
