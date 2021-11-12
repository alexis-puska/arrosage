package fr.iocean.arrosage.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.iocean.arrosage.service.ElectroVanneService;
import fr.iocean.arrosage.service.dto.StatusRelayDTO;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class ElectroVanneResource {

	private final Logger log = LoggerFactory.getLogger(ElectroVanneResource.class);

	private final ElectroVanneService electroVanneService;

	public ElectroVanneResource(ElectroVanneService electroVanneService) {

		this.electroVanneService = electroVanneService;
	}

	@GetMapping("/electro-vanne/open/{id}")
	public void openVanne(@PathVariable("id") int id) {
		this.log.debug("Ouverture vanne : ", id);
		this.electroVanneService.openVanneScheduled(id);
	}

	@GetMapping("/electro-vanne/close/{id}")
	public void closeVanne(@PathVariable("id") int id) {
		this.log.debug("Fermeture vanne : ", id);
		this.electroVanneService.closeVanneScheduled(id);
	}

	@GetMapping("/electro-vanne/add-time/{id}")
	public void addTime(@PathVariable("id") int id) {
		this.log.debug("Ouverture vanne : ", id);
		this.electroVanneService.addTime(id);
	}

	@GetMapping("/electro-vanne/cancel/{id}")
	public void cancel(@PathVariable("id") int id) {
		this.log.debug("Ouverture vanne : ", id);
		this.electroVanneService.cancel(id);
	}

	@GetMapping("/electro-vanne/status")
	public List<StatusRelayDTO> getStatus() {
		this.log.debug("getStatus");
		return this.electroVanneService.getStatus();
	}

	@GetMapping("/electro-vanne/cancel-all")
	public void cancelAll() {
		this.log.debug("cancelAll");
		this.electroVanneService.cancelAll();
	}
}
