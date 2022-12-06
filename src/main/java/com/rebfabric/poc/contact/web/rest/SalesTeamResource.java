package com.rebfabric.poc.contact.web.rest;

import com.rebfabric.poc.contact.repository.SalesTeamRepository;
import com.rebfabric.poc.contact.service.SalesTeamService;
import com.rebfabric.poc.contact.service.dto.SalesTeamDTO;
import com.rebfabric.poc.contact.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.rebfabric.poc.contact.domain.SalesTeam}.
 */
@RestController
@RequestMapping("/api")
public class SalesTeamResource {

    private final Logger log = LoggerFactory.getLogger(SalesTeamResource.class);

    private static final String ENTITY_NAME = "repfabricContactSalesTeam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalesTeamService salesTeamService;

    private final SalesTeamRepository salesTeamRepository;

    public SalesTeamResource(SalesTeamService salesTeamService, SalesTeamRepository salesTeamRepository) {
        this.salesTeamService = salesTeamService;
        this.salesTeamRepository = salesTeamRepository;
    }

    /**
     * {@code POST  /sales-teams} : Create a new salesTeam.
     *
     * @param salesTeamDTO the salesTeamDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salesTeamDTO, or with status {@code 400 (Bad Request)} if the salesTeam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sales-teams")
    public ResponseEntity<SalesTeamDTO> createSalesTeam(@RequestBody SalesTeamDTO salesTeamDTO) throws URISyntaxException {
        log.debug("REST request to save SalesTeam : {}", salesTeamDTO);
        if (salesTeamDTO.getId() != null) {
            throw new BadRequestAlertException("A new salesTeam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalesTeamDTO result = salesTeamService.save(salesTeamDTO);
        return ResponseEntity
            .created(new URI("/api/sales-teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sales-teams/:id} : Updates an existing salesTeam.
     *
     * @param id the id of the salesTeamDTO to save.
     * @param salesTeamDTO the salesTeamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salesTeamDTO,
     * or with status {@code 400 (Bad Request)} if the salesTeamDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salesTeamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sales-teams/{id}")
    public ResponseEntity<SalesTeamDTO> updateSalesTeam(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SalesTeamDTO salesTeamDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SalesTeam : {}, {}", id, salesTeamDTO);
        if (salesTeamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salesTeamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salesTeamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SalesTeamDTO result = salesTeamService.update(salesTeamDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salesTeamDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sales-teams/:id} : Partial updates given fields of an existing salesTeam, field will ignore if it is null
     *
     * @param id the id of the salesTeamDTO to save.
     * @param salesTeamDTO the salesTeamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salesTeamDTO,
     * or with status {@code 400 (Bad Request)} if the salesTeamDTO is not valid,
     * or with status {@code 404 (Not Found)} if the salesTeamDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the salesTeamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sales-teams/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SalesTeamDTO> partialUpdateSalesTeam(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SalesTeamDTO salesTeamDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SalesTeam partially : {}, {}", id, salesTeamDTO);
        if (salesTeamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salesTeamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salesTeamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SalesTeamDTO> result = salesTeamService.partialUpdate(salesTeamDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salesTeamDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sales-teams} : get all the salesTeams.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salesTeams in body.
     */
    @GetMapping("/sales-teams")
    public List<SalesTeamDTO> getAllSalesTeams() {
        log.debug("REST request to get all SalesTeams");
        return salesTeamService.findAll();
    }

    /**
     * {@code GET  /sales-teams/:id} : get the "id" salesTeam.
     *
     * @param id the id of the salesTeamDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salesTeamDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sales-teams/{id}")
    public ResponseEntity<SalesTeamDTO> getSalesTeam(@PathVariable Long id) {
        log.debug("REST request to get SalesTeam : {}", id);
        Optional<SalesTeamDTO> salesTeamDTO = salesTeamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salesTeamDTO);
    }

    /**
     * {@code DELETE  /sales-teams/:id} : delete the "id" salesTeam.
     *
     * @param id the id of the salesTeamDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sales-teams/{id}")
    public ResponseEntity<Void> deleteSalesTeam(@PathVariable Long id) {
        log.debug("REST request to delete SalesTeam : {}", id);
        salesTeamService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
