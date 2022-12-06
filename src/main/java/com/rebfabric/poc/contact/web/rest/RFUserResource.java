package com.rebfabric.poc.contact.web.rest;

import com.rebfabric.poc.contact.repository.RFUserRepository;
import com.rebfabric.poc.contact.service.RFUserService;
import com.rebfabric.poc.contact.service.dto.RFUserDTO;
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
 * REST controller for managing {@link com.rebfabric.poc.contact.domain.RFUser}.
 */
@RestController
@RequestMapping("/api")
public class RFUserResource {

    private final Logger log = LoggerFactory.getLogger(RFUserResource.class);

    private static final String ENTITY_NAME = "repfabricContactRfUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RFUserService rFUserService;

    private final RFUserRepository rFUserRepository;

    public RFUserResource(RFUserService rFUserService, RFUserRepository rFUserRepository) {
        this.rFUserService = rFUserService;
        this.rFUserRepository = rFUserRepository;
    }

    /**
     * {@code POST  /rf-users} : Create a new rFUser.
     *
     * @param rFUserDTO the rFUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rFUserDTO, or with status {@code 400 (Bad Request)} if the rFUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rf-users")
    public ResponseEntity<RFUserDTO> createRFUser(@RequestBody RFUserDTO rFUserDTO) throws URISyntaxException {
        log.debug("REST request to save RFUser : {}", rFUserDTO);
        if (rFUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new rFUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RFUserDTO result = rFUserService.save(rFUserDTO);
        return ResponseEntity
            .created(new URI("/api/rf-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rf-users/:id} : Updates an existing rFUser.
     *
     * @param id the id of the rFUserDTO to save.
     * @param rFUserDTO the rFUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rFUserDTO,
     * or with status {@code 400 (Bad Request)} if the rFUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rFUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rf-users/{id}")
    public ResponseEntity<RFUserDTO> updateRFUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RFUserDTO rFUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RFUser : {}, {}", id, rFUserDTO);
        if (rFUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rFUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rFUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RFUserDTO result = rFUserService.update(rFUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rFUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rf-users/:id} : Partial updates given fields of an existing rFUser, field will ignore if it is null
     *
     * @param id the id of the rFUserDTO to save.
     * @param rFUserDTO the rFUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rFUserDTO,
     * or with status {@code 400 (Bad Request)} if the rFUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rFUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rFUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rf-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RFUserDTO> partialUpdateRFUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RFUserDTO rFUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RFUser partially : {}, {}", id, rFUserDTO);
        if (rFUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rFUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rFUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RFUserDTO> result = rFUserService.partialUpdate(rFUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rFUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rf-users} : get all the rFUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rFUsers in body.
     */
    @GetMapping("/rf-users")
    public List<RFUserDTO> getAllRFUsers() {
        log.debug("REST request to get all RFUsers");
        return rFUserService.findAll();
    }

    /**
     * {@code GET  /rf-users/:id} : get the "id" rFUser.
     *
     * @param id the id of the rFUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rFUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rf-users/{id}")
    public ResponseEntity<RFUserDTO> getRFUser(@PathVariable Long id) {
        log.debug("REST request to get RFUser : {}", id);
        Optional<RFUserDTO> rFUserDTO = rFUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rFUserDTO);
    }

    /**
     * {@code DELETE  /rf-users/:id} : delete the "id" rFUser.
     *
     * @param id the id of the rFUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rf-users/{id}")
    public ResponseEntity<Void> deleteRFUser(@PathVariable Long id) {
        log.debug("REST request to delete RFUser : {}", id);
        rFUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
