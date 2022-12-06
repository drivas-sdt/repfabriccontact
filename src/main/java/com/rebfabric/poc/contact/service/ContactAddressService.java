package com.rebfabric.poc.contact.service;

import com.rebfabric.poc.contact.domain.ContactAddress;
import com.rebfabric.poc.contact.repository.ContactAddressRepository;
import com.rebfabric.poc.contact.service.dto.ContactAddressDTO;
import com.rebfabric.poc.contact.service.mapper.ContactAddressMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContactAddress}.
 */
@Service
@Transactional
public class ContactAddressService {

    private final Logger log = LoggerFactory.getLogger(ContactAddressService.class);

    private final ContactAddressRepository contactAddressRepository;

    private final ContactAddressMapper contactAddressMapper;

    public ContactAddressService(ContactAddressRepository contactAddressRepository, ContactAddressMapper contactAddressMapper) {
        this.contactAddressRepository = contactAddressRepository;
        this.contactAddressMapper = contactAddressMapper;
    }

    /**
     * Save a contactAddress.
     *
     * @param contactAddressDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactAddressDTO save(ContactAddressDTO contactAddressDTO) {
        log.debug("Request to save ContactAddress : {}", contactAddressDTO);
        ContactAddress contactAddress = contactAddressMapper.toEntity(contactAddressDTO);
        contactAddress = contactAddressRepository.save(contactAddress);
        return contactAddressMapper.toDto(contactAddress);
    }

    /**
     * Update a contactAddress.
     *
     * @param contactAddressDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactAddressDTO update(ContactAddressDTO contactAddressDTO) {
        log.debug("Request to update ContactAddress : {}", contactAddressDTO);
        ContactAddress contactAddress = contactAddressMapper.toEntity(contactAddressDTO);
        contactAddress = contactAddressRepository.save(contactAddress);
        return contactAddressMapper.toDto(contactAddress);
    }

    /**
     * Partially update a contactAddress.
     *
     * @param contactAddressDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactAddressDTO> partialUpdate(ContactAddressDTO contactAddressDTO) {
        log.debug("Request to partially update ContactAddress : {}", contactAddressDTO);

        return contactAddressRepository
            .findById(contactAddressDTO.getId())
            .map(existingContactAddress -> {
                contactAddressMapper.partialUpdate(existingContactAddress, contactAddressDTO);

                return existingContactAddress;
            })
            .map(contactAddressRepository::save)
            .map(contactAddressMapper::toDto);
    }

    /**
     * Get all the contactAddresses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ContactAddressDTO> findAll() {
        log.debug("Request to get all ContactAddresses");
        return contactAddressRepository
            .findAll()
            .stream()
            .map(contactAddressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one contactAddress by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactAddressDTO> findOne(Long id) {
        log.debug("Request to get ContactAddress : {}", id);
        return contactAddressRepository.findById(id).map(contactAddressMapper::toDto);
    }

    /**
     * Delete the contactAddress by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactAddress : {}", id);
        contactAddressRepository.deleteById(id);
    }
}
