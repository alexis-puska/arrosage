package fr.iocean.arrosage.web.rest;

import fr.iocean.arrosage.service.BlackListService;
import fr.iocean.arrosage.web.rest.errors.BadRequestAlertException;
import fr.iocean.arrosage.service.dto.BlackListDTO;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.iocean.arrosage.domain.BlackList}.
 */
@RestController
@RequestMapping("/api")
public class BlackListResource {

    private final Logger log = LoggerFactory.getLogger(BlackListResource.class);

    private static final String ENTITY_NAME = "blackList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BlackListService blackListService;

    public BlackListResource(BlackListService blackListService) {
        this.blackListService = blackListService;
    }

    /**
     * {@code POST  /black-lists} : Create a new blackList.
     *
     * @param blackListDTO the blackListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new blackListDTO, or with status {@code 400 (Bad Request)} if the blackList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/black-lists")
    public ResponseEntity<BlackListDTO> createBlackList(@RequestBody BlackListDTO blackListDTO) throws URISyntaxException {
        log.debug("REST request to save BlackList : {}", blackListDTO);
        if (blackListDTO.getId() != null) {
            throw new BadRequestAlertException("A new blackList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlackListDTO result = blackListService.save(blackListDTO);
        return ResponseEntity.created(new URI("/api/black-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /black-lists} : Updates an existing blackList.
     *
     * @param blackListDTO the blackListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blackListDTO,
     * or with status {@code 400 (Bad Request)} if the blackListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blackListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/black-lists")
    public ResponseEntity<BlackListDTO> updateBlackList(@RequestBody BlackListDTO blackListDTO) throws URISyntaxException {
        log.debug("REST request to update BlackList : {}", blackListDTO);
        if (blackListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BlackListDTO result = blackListService.save(blackListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, blackListDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /black-lists} : get all the blackLists.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of blackLists in body.
     */
    @GetMapping("/black-lists")
    public ResponseEntity<List<BlackListDTO>> getAllBlackLists(Pageable pageable) {
        log.debug("REST request to get a page of BlackLists");
        Page<BlackListDTO> page = blackListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /black-lists/:id} : get the "id" blackList.
     *
     * @param id the id of the blackListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the blackListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/black-lists/{id}")
    public ResponseEntity<BlackListDTO> getBlackList(@PathVariable Long id) {
        log.debug("REST request to get BlackList : {}", id);
        Optional<BlackListDTO> blackListDTO = blackListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(blackListDTO);
    }

    /**
     * {@code DELETE  /black-lists/:id} : delete the "id" blackList.
     *
     * @param id the id of the blackListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/black-lists/{id}")
    public ResponseEntity<Void> deleteBlackList(@PathVariable Long id) {
        log.debug("REST request to delete BlackList : {}", id);
        blackListService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
