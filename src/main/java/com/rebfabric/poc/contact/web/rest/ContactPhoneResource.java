package com.rebfabric.poc.contact.web.rest;

import com.rebfabric.poc.contact.repository.ContactPhoneRepository;
import com.rebfabric.poc.contact.service.ContactPhoneService;
import com.rebfabric.poc.contact.service.dto.ContactPhoneDTO;
import com.rebfabric.poc.contact.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.rebfabric.poc.contact.domain.ContactPhone}.
 */
@RestController
@RequestMapping("/api")
public class ContactPhoneResource {

    private final Logger log = LoggerFactory.getLogger(ContactPhoneResource.class);

    private static final String ENTITY_NAME = "repfabricContactContactPhone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactPhoneService contactPhoneService;

    private final ContactPhoneRepository contactPhoneRepository;

    public ContactPhoneResource(ContactPhoneService contactPhoneService, ContactPhoneRepository contactPhoneRepository) {
        this.contactPhoneService = contactPhoneService;
        this.contactPhoneRepository = contactPhoneRepository;
    }

    /**
     * {@code POST  /contact-phones} : Create a new contactPhone.
     *
     * @param contactPhoneDTO the contactPhoneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactPhoneDTO, or with status {@code 400 (Bad Request)} if the contactPhone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-phones")
    public ResponseEntity<ContactPhoneDTO> createContactPhone(@Valid @RequestBody ContactPhoneDTO contactPhoneDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactPhone : {}", contactPhoneDTO);
        if (contactPhoneDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactPhone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactPhoneDTO result = contactPhoneService.save(contactPhoneDTO);
        return ResponseEntity
            .created(new URI("/api/contact-phones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-phones/:id} : Updates an existing contactPhone.
     *
     * @param id the id of the contactPhoneDTO to save.
     * @param contactPhoneDTO the contactPhoneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactPhoneDTO,
     * or with status {@code 400 (Bad Request)} if the contactPhoneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactPhoneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-phones/{id}")
    public ResponseEntity<ContactPhoneDTO> updateContactPhone(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactPhoneDTO contactPhoneDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactPhone : {}, {}", id, contactPhoneDTO);
        if (contactPhoneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactPhoneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactPhoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactPhoneDTO result = contactPhoneService.update(contactPhoneDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactPhoneDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contact-phones/:id} : Partial updates given fields of an existing contactPhone, field will ignore if it is null
     *
     * @param id the id of the contactPhoneDTO to save.
     * @param contactPhoneDTO the contactPhoneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactPhoneDTO,
     * or with status {@code 400 (Bad Request)} if the contactPhoneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactPhoneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactPhoneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contact-phones/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactPhoneDTO> partialUpdateContactPhone(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactPhoneDTO contactPhoneDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactPhone partially : {}, {}", id, contactPhoneDTO);
        if (contactPhoneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactPhoneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactPhoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactPhoneDTO> result = contactPhoneService.partialUpdate(contactPhoneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactPhoneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-phones} : get all the contactPhones.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactPhones in body.
     */
    @GetMapping("/contact-phones")
    public List<ContactPhoneDTO> getAllContactPhones() {
        log.debug("REST request to get all ContactPhones");
        return contactPhoneService.findAll();
    }

    /**
     * {@code GET  /contact-phones/:id} : get the "id" contactPhone.
     *
     * @param id the id of the contactPhoneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactPhoneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-phones/{id}")
    public ResponseEntity<ContactPhoneDTO> getContactPhone(@PathVariable Long id) {
        log.debug("REST request to get ContactPhone : {}", id);
        Optional<ContactPhoneDTO> contactPhoneDTO = contactPhoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactPhoneDTO);
    }

    /**
     * {@code DELETE  /contact-phones/:id} : delete the "id" contactPhone.
     *
     * @param id the id of the contactPhoneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-phones/{id}")
    public ResponseEntity<Void> deleteContactPhone(@PathVariable Long id) {
        log.debug("REST request to delete ContactPhone : {}", id);
        contactPhoneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
