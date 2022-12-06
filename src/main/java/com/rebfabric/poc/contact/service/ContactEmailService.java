package com.rebfabric.poc.contact.service;

import com.rebfabric.poc.contact.domain.ContactEmail;
import com.rebfabric.poc.contact.repository.ContactEmailRepository;
import com.rebfabric.poc.contact.service.dto.ContactEmailDTO;
import com.rebfabric.poc.contact.service.mapper.ContactEmailMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContactEmail}.
 */
@Service
@Transactional
public class ContactEmailService {

    private final Logger log = LoggerFactory.getLogger(ContactEmailService.class);

    private final ContactEmailRepository contactEmailRepository;

    private final ContactEmailMapper contactEmailMapper;

    public ContactEmailService(ContactEmailRepository contactEmailRepository, ContactEmailMapper contactEmailMapper) {
        this.contactEmailRepository = contactEmailRepository;
        this.contactEmailMapper = contactEmailMapper;
    }

    /**
     * Save a contactEmail.
     *
     * @param contactEmailDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactEmailDTO save(ContactEmailDTO contactEmailDTO) {
        log.debug("Request to save ContactEmail : {}", contactEmailDTO);
        ContactEmail contactEmail = contactEmailMapper.toEntity(contactEmailDTO);
        contactEmail = contactEmailRepository.save(contactEmail);
        return contactEmailMapper.toDto(contactEmail);
    }

    /**
     * Update a contactEmail.
     *
     * @param contactEmailDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactEmailDTO update(ContactEmailDTO contactEmailDTO) {
        log.debug("Request to update ContactEmail : {}", contactEmailDTO);
        ContactEmail contactEmail = contactEmailMapper.toEntity(contactEmailDTO);
        contactEmail = contactEmailRepository.save(contactEmail);
        return contactEmailMapper.toDto(contactEmail);
    }

    /**
     * Partially update a contactEmail.
     *
     * @param contactEmailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactEmailDTO> partialUpdate(ContactEmailDTO contactEmailDTO) {
        log.debug("Request to partially update ContactEmail : {}", contactEmailDTO);

        return contactEmailRepository
            .findById(contactEmailDTO.getId())
            .map(existingContactEmail -> {
                contactEmailMapper.partialUpdate(existingContactEmail, contactEmailDTO);

                return existingContactEmail;
            })
            .map(contactEmailRepository::save)
            .map(contactEmailMapper::toDto);
    }

    /**
     * Get all the contactEmails.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ContactEmailDTO> findAll() {
        log.debug("Request to get all ContactEmails");
        return contactEmailRepository.findAll().stream().map(contactEmailMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one contactEmail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactEmailDTO> findOne(Long id) {
        log.debug("Request to get ContactEmail : {}", id);
        return contactEmailRepository.findById(id).map(contactEmailMapper::toDto);
    }

    /**
     * Delete the contactEmail by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactEmail : {}", id);
        contactEmailRepository.deleteById(id);
    }
}
