package com.rebfabric.poc.contact.web.rest;

import com.rebfabric.poc.contact.repository.CompanyTypeRepository;
import com.rebfabric.poc.contact.service.CompanyTypeService;
import com.rebfabric.poc.contact.service.dto.CompanyTypeDTO;
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
 * REST controller for managing {@link com.rebfabric.poc.contact.domain.CompanyType}.
 */
@RestController
@RequestMapping("/api")
public class CompanyTypeResource {

    private final Logger log = LoggerFactory.getLogger(CompanyTypeResource.class);

    private static final String ENTITY_NAME = "repfabricContactCompanyType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyTypeService companyTypeService;

    private final CompanyTypeRepository companyTypeRepository;

    public CompanyTypeResource(CompanyTypeService companyTypeService, CompanyTypeRepository companyTypeRepository) {
        this.companyTypeService = companyTypeService;
        this.companyTypeRepository = companyTypeRepository;
    }

    /**
     * {@code POST  /company-types} : Create a new companyType.
     *
     * @param companyTypeDTO the companyTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyTypeDTO, or with status {@code 400 (Bad Request)} if the companyType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/company-types")
    public ResponseEntity<CompanyTypeDTO> createCompanyType(@RequestBody CompanyTypeDTO companyTypeDTO) throws URISyntaxException {
        log.debug("REST request to save CompanyType : {}", companyTypeDTO);
        if (companyTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new companyType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyTypeDTO result = companyTypeService.save(companyTypeDTO);
        return ResponseEntity
            .created(new URI("/api/company-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /company-types/:id} : Updates an existing companyType.
     *
     * @param id the id of the companyTypeDTO to save.
     * @param companyTypeDTO the companyTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyTypeDTO,
     * or with status {@code 400 (Bad Request)} if the companyTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/company-types/{id}")
    public ResponseEntity<CompanyTypeDTO> updateCompanyType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyTypeDTO companyTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompanyType : {}, {}", id, companyTypeDTO);
        if (companyTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompanyTypeDTO result = companyTypeService.update(companyTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /company-types/:id} : Partial updates given fields of an existing companyType, field will ignore if it is null
     *
     * @param id the id of the companyTypeDTO to save.
     * @param companyTypeDTO the companyTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyTypeDTO,
     * or with status {@code 400 (Bad Request)} if the companyTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the companyTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/company-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompanyTypeDTO> partialUpdateCompanyType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyTypeDTO companyTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompanyType partially : {}, {}", id, companyTypeDTO);
        if (companyTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompanyTypeDTO> result = companyTypeService.partialUpdate(companyTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /company-types} : get all the companyTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyTypes in body.
     */
    @GetMapping("/company-types")
    public List<CompanyTypeDTO> getAllCompanyTypes() {
        log.debug("REST request to get all CompanyTypes");
        return companyTypeService.findAll();
    }

    /**
     * {@code GET  /company-types/:id} : get the "id" companyType.
     *
     * @param id the id of the companyTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/company-types/{id}")
    public ResponseEntity<CompanyTypeDTO> getCompanyType(@PathVariable Long id) {
        log.debug("REST request to get CompanyType : {}", id);
        Optional<CompanyTypeDTO> companyTypeDTO = companyTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyTypeDTO);
    }

    /**
     * {@code DELETE  /company-types/:id} : delete the "id" companyType.
     *
     * @param id the id of the companyTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/company-types/{id}")
    public ResponseEntity<Void> deleteCompanyType(@PathVariable Long id) {
        log.debug("REST request to delete CompanyType : {}", id);
        companyTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
