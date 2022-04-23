package fr.iocean.arrosage.web.rest;

import fr.iocean.arrosage.service.ProgrammationService;
import fr.iocean.arrosage.web.rest.errors.BadRequestAlertException;
import fr.iocean.arrosage.service.dto.ProgrammationDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.iocean.arrosage.domain.Programmation}.
 */
@RestController
@RequestMapping("/api")
public class ProgrammationResource {

    private final Logger log = LoggerFactory.getLogger(ProgrammationResource.class);

    private static final String ENTITY_NAME = "programmation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgrammationService programmationService;

    public ProgrammationResource(ProgrammationService programmationService) {
        this.programmationService = programmationService;
    }

    /**
     * {@code POST  /programmations} : Create a new programmation.
     *
     * @param programmationDTO the programmationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new programmationDTO, or with status {@code 400 (Bad Request)} if the programmation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/programmations")
    public ResponseEntity<ProgrammationDTO> createProgrammation(@Valid @RequestBody ProgrammationDTO programmationDTO) throws URISyntaxException {
        log.debug("REST request to save Programmation : {}", programmationDTO);
        if (programmationDTO.getId() != null) {
            throw new BadRequestAlertException("A new programmation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProgrammationDTO result = programmationService.save(programmationDTO);
        return ResponseEntity.created(new URI("/api/programmations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /programmations} : Updates an existing programmation.
     *
     * @param programmationDTO the programmationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programmationDTO,
     * or with status {@code 400 (Bad Request)} if the programmationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the programmationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/programmations")
    public ResponseEntity<ProgrammationDTO> updateProgrammation(@Valid @RequestBody ProgrammationDTO programmationDTO) throws URISyntaxException {
        log.debug("REST request to update Programmation : {}", programmationDTO);
        if (programmationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProgrammationDTO result = programmationService.save(programmationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, programmationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /programmations} : get all the programmations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of programmations in body.
     */
    @GetMapping("/programmations")
    public ResponseEntity<List<ProgrammationDTO>> getAllProgrammations(Pageable pageable) {
        log.debug("REST request to get a page of Programmations");
        Page<ProgrammationDTO> page = programmationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /programmations/:id} : get the "id" programmation.
     *
     * @param id the id of the programmationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the programmationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/programmations/{id}")
    public ResponseEntity<ProgrammationDTO> getProgrammation(@PathVariable Long id) {
        log.debug("REST request to get Programmation : {}", id);
        Optional<ProgrammationDTO> programmationDTO = programmationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(programmationDTO);
    }

    /**
     * {@code DELETE  /programmations/:id} : delete the "id" programmation.
     *
     * @param id the id of the programmationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/programmations/{id}")
    public ResponseEntity<Void> deleteProgrammation(@PathVariable Long id) {
        log.debug("REST request to delete Programmation : {}", id);
        programmationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
