package com.rebfabric.poc.contact.web.rest;

import com.rebfabric.poc.contact.repository.CompanyRegionRepository;
import com.rebfabric.poc.contact.service.CompanyRegionService;
import com.rebfabric.poc.contact.service.dto.CompanyRegionDTO;
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
 * REST controller for managing {@link com.rebfabric.poc.contact.domain.CompanyRegion}.
 */
@RestController
@RequestMapping("/api")
public class CompanyRegionResource {

    private final Logger log = LoggerFactory.getLogger(CompanyRegionResource.class);

    private static final String ENTITY_NAME = "repfabricContactCompanyRegion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyRegionService companyRegionService;

    private final CompanyRegionRepository companyRegionRepository;

    public CompanyRegionResource(CompanyRegionService companyRegionService, CompanyRegionRepository companyRegionRepository) {
        this.companyRegionService = companyRegionService;
        this.companyRegionRepository = companyRegionRepository;
    }

    /**
     * {@code POST  /company-regions} : Create a new companyRegion.
     *
     * @param companyRegionDTO the companyRegionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyRegionDTO, or with status {@code 400 (Bad Request)} if the companyRegion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/company-regions")
    public ResponseEntity<CompanyRegionDTO> createCompanyRegion(@RequestBody CompanyRegionDTO companyRegionDTO) throws URISyntaxException {
        log.debug("REST request to save CompanyRegion : {}", companyRegionDTO);
        if (companyRegionDTO.getId() != null) {
            throw new BadRequestAlertException("A new companyRegion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyRegionDTO result = companyRegionService.save(companyRegionDTO);
        return ResponseEntity
            .created(new URI("/api/company-regions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /company-regions/:id} : Updates an existing companyRegion.
     *
     * @param id the id of the companyRegionDTO to save.
     * @param companyRegionDTO the companyRegionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyRegionDTO,
     * or with status {@code 400 (Bad Request)} if the companyRegionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyRegionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/company-regions/{id}")
    public ResponseEntity<CompanyRegionDTO> updateCompanyRegion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyRegionDTO companyRegionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompanyRegion : {}, {}", id, companyRegionDTO);
        if (companyRegionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyRegionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyRegionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompanyRegionDTO result = companyRegionService.update(companyRegionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyRegionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /company-regions/:id} : Partial updates given fields of an existing companyRegion, field will ignore if it is null
     *
     * @param id the id of the companyRegionDTO to save.
     * @param companyRegionDTO the companyRegionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyRegionDTO,
     * or with status {@code 400 (Bad Request)} if the companyRegionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the companyRegionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyRegionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/company-regions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompanyRegionDTO> partialUpdateCompanyRegion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyRegionDTO companyRegionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompanyRegion partially : {}, {}", id, companyRegionDTO);
        if (companyRegionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyRegionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyRegionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompanyRegionDTO> result = companyRegionService.partialUpdate(companyRegionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyRegionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /company-regions} : get all the companyRegions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyRegions in body.
     */
    @GetMapping("/company-regions")
    public List<CompanyRegionDTO> getAllCompanyRegions() {
        log.debug("REST request to get all CompanyRegions");
        return companyRegionService.findAll();
    }

    /**
     * {@code GET  /company-regions/:id} : get the "id" companyRegion.
     *
     * @param id the id of the companyRegionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyRegionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/company-regions/{id}")
    public ResponseEntity<CompanyRegionDTO> getCompanyRegion(@PathVariable Long id) {
        log.debug("REST request to get CompanyRegion : {}", id);
        Optional<CompanyRegionDTO> companyRegionDTO = companyRegionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyRegionDTO);
    }

    /**
     * {@code DELETE  /company-regions/:id} : delete the "id" companyRegion.
     *
     * @param id the id of the companyRegionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/company-regions/{id}")
    public ResponseEntity<Void> deleteCompanyRegion(@PathVariable Long id) {
        log.debug("REST request to delete CompanyRegion : {}", id);
        companyRegionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
