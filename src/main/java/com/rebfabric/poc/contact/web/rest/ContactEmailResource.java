package com.rebfabric.poc.contact.web.rest;

import com.rebfabric.poc.contact.repository.ContactEmailRepository;
import com.rebfabric.poc.contact.service.ContactEmailService;
import com.rebfabric.poc.contact.service.dto.ContactEmailDTO;
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
 * REST controller for managing {@link com.rebfabric.poc.contact.domain.ContactEmail}.
 */
@RestController
@RequestMapping("/api")
public class ContactEmailResource {

    private final Logger log = LoggerFactory.getLogger(ContactEmailResource.class);

    private static final String ENTITY_NAME = "repfabricContactContactEmail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactEmailService contactEmailService;

    private final ContactEmailRepository contactEmailRepository;

    public ContactEmailResource(ContactEmailService contactEmailService, ContactEmailRepository contactEmailRepository) {
        this.contactEmailService = contactEmailService;
        this.contactEmailRepository = contactEmailRepository;
    }

    /**
     * {@code POST  /contact-emails} : Create a new contactEmail.
     *
     * @param contactEmailDTO the contactEmailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactEmailDTO, or with status {@code 400 (Bad Request)} if the contactEmail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-emails")
    public ResponseEntity<ContactEmailDTO> createContactEmail(@Valid @RequestBody ContactEmailDTO contactEmailDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactEmail : {}", contactEmailDTO);
        if (contactEmailDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactEmail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactEmailDTO result = contactEmailService.save(contactEmailDTO);
        return ResponseEntity
            .created(new URI("/api/contact-emails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-emails/:id} : Updates an existing contactEmail.
     *
     * @param id the id of the contactEmailDTO to save.
     * @param contactEmailDTO the contactEmailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactEmailDTO,
     * or with status {@code 400 (Bad Request)} if the contactEmailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactEmailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-emails/{id}")
    public ResponseEntity<ContactEmailDTO> updateContactEmail(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactEmailDTO contactEmailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactEmail : {}, {}", id, contactEmailDTO);
        if (contactEmailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactEmailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactEmailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactEmailDTO result = contactEmailService.update(contactEmailDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactEmailDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contact-emails/:id} : Partial updates given fields of an existing contactEmail, field will ignore if it is null
     *
     * @param id the id of the contactEmailDTO to save.
     * @param contactEmailDTO the contactEmailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactEmailDTO,
     * or with status {@code 400 (Bad Request)} if the contactEmailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactEmailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactEmailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contact-emails/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactEmailDTO> partialUpdateContactEmail(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactEmailDTO contactEmailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactEmail partially : {}, {}", id, contactEmailDTO);
        if (contactEmailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactEmailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactEmailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactEmailDTO> result = contactEmailService.partialUpdate(contactEmailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactEmailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-emails} : get all the contactEmails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactEmails in body.
     */
    @GetMapping("/contact-emails")
    public List<ContactEmailDTO> getAllContactEmails() {
        log.debug("REST request to get all ContactEmails");
        return contactEmailService.findAll();
    }

    /**
     * {@code GET  /contact-emails/:id} : get the "id" contactEmail.
     *
     * @param id the id of the contactEmailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactEmailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-emails/{id}")
    public ResponseEntity<ContactEmailDTO> getContactEmail(@PathVariable Long id) {
        log.debug("REST request to get ContactEmail : {}", id);
        Optional<ContactEmailDTO> contactEmailDTO = contactEmailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactEmailDTO);
    }

    /**
     * {@code DELETE  /contact-emails/:id} : delete the "id" contactEmail.
     *
     * @param id the id of the contactEmailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-emails/{id}")
    public ResponseEntity<Void> deleteContactEmail(@PathVariable Long id) {
        log.debug("REST request to delete ContactEmail : {}", id);
        contactEmailService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
