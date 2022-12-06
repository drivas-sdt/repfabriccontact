package com.rebfabric.poc.contact.service;

import com.rebfabric.poc.contact.domain.ContactPhone;
import com.rebfabric.poc.contact.repository.ContactPhoneRepository;
import com.rebfabric.poc.contact.service.dto.ContactPhoneDTO;
import com.rebfabric.poc.contact.service.mapper.ContactPhoneMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContactPhone}.
 */
@Service
@Transactional
public class ContactPhoneService {

    private final Logger log = LoggerFactory.getLogger(ContactPhoneService.class);

    private final ContactPhoneRepository contactPhoneRepository;

    private final ContactPhoneMapper contactPhoneMapper;

    public ContactPhoneService(ContactPhoneRepository contactPhoneRepository, ContactPhoneMapper contactPhoneMapper) {
        this.contactPhoneRepository = contactPhoneRepository;
        this.contactPhoneMapper = contactPhoneMapper;
    }

    /**
     * Save a contactPhone.
     *
     * @param contactPhoneDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactPhoneDTO save(ContactPhoneDTO contactPhoneDTO) {
        log.debug("Request to save ContactPhone : {}", contactPhoneDTO);
        ContactPhone contactPhone = contactPhoneMapper.toEntity(contactPhoneDTO);
        contactPhone = contactPhoneRepository.save(contactPhone);
        return contactPhoneMapper.toDto(contactPhone);
    }

    /**
     * Update a contactPhone.
     *
     * @param contactPhoneDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactPhoneDTO update(ContactPhoneDTO contactPhoneDTO) {
        log.debug("Request to update ContactPhone : {}", contactPhoneDTO);
        ContactPhone contactPhone = contactPhoneMapper.toEntity(contactPhoneDTO);
        contactPhone = contactPhoneRepository.save(contactPhone);
        return contactPhoneMapper.toDto(contactPhone);
    }

    /**
     * Partially update a contactPhone.
     *
     * @param contactPhoneDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactPhoneDTO> partialUpdate(ContactPhoneDTO contactPhoneDTO) {
        log.debug("Request to partially update ContactPhone : {}", contactPhoneDTO);

        return contactPhoneRepository
            .findById(contactPhoneDTO.getId())
            .map(existingContactPhone -> {
                contactPhoneMapper.partialUpdate(existingContactPhone, contactPhoneDTO);

                return existingContactPhone;
            })
            .map(contactPhoneRepository::save)
            .map(contactPhoneMapper::toDto);
    }

    /**
     * Get all the contactPhones.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ContactPhoneDTO> findAll() {
        log.debug("Request to get all ContactPhones");
        return contactPhoneRepository.findAll().stream().map(contactPhoneMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one contactPhone by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactPhoneDTO> findOne(Long id) {
        log.debug("Request to get ContactPhone : {}", id);
        return contactPhoneRepository.findById(id).map(contactPhoneMapper::toDto);
    }

    /**
     * Delete the contactPhone by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactPhone : {}", id);
        contactPhoneRepository.deleteById(id);
    }
}
