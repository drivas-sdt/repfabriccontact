package com.rebfabric.poc.contact.service;

import com.rebfabric.poc.contact.domain.RFUser;
import com.rebfabric.poc.contact.repository.RFUserRepository;
import com.rebfabric.poc.contact.service.dto.RFUserDTO;
import com.rebfabric.poc.contact.service.mapper.RFUserMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RFUser}.
 */
@Service
@Transactional
public class RFUserService {

    private final Logger log = LoggerFactory.getLogger(RFUserService.class);

    private final RFUserRepository rFUserRepository;

    private final RFUserMapper rFUserMapper;

    public RFUserService(RFUserRepository rFUserRepository, RFUserMapper rFUserMapper) {
        this.rFUserRepository = rFUserRepository;
        this.rFUserMapper = rFUserMapper;
    }

    /**
     * Save a rFUser.
     *
     * @param rFUserDTO the entity to save.
     * @return the persisted entity.
     */
    public RFUserDTO save(RFUserDTO rFUserDTO) {
        log.debug("Request to save RFUser : {}", rFUserDTO);
        RFUser rFUser = rFUserMapper.toEntity(rFUserDTO);
        rFUser = rFUserRepository.save(rFUser);
        return rFUserMapper.toDto(rFUser);
    }

    /**
     * Update a rFUser.
     *
     * @param rFUserDTO the entity to save.
     * @return the persisted entity.
     */
    public RFUserDTO update(RFUserDTO rFUserDTO) {
        log.debug("Request to update RFUser : {}", rFUserDTO);
        RFUser rFUser = rFUserMapper.toEntity(rFUserDTO);
        rFUser = rFUserRepository.save(rFUser);
        return rFUserMapper.toDto(rFUser);
    }

    /**
     * Partially update a rFUser.
     *
     * @param rFUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RFUserDTO> partialUpdate(RFUserDTO rFUserDTO) {
        log.debug("Request to partially update RFUser : {}", rFUserDTO);

        return rFUserRepository
            .findById(rFUserDTO.getId())
            .map(existingRFUser -> {
                rFUserMapper.partialUpdate(existingRFUser, rFUserDTO);

                return existingRFUser;
            })
            .map(rFUserRepository::save)
            .map(rFUserMapper::toDto);
    }

    /**
     * Get all the rFUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RFUserDTO> findAll() {
        log.debug("Request to get all RFUsers");
        return rFUserRepository.findAll().stream().map(rFUserMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one rFUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RFUserDTO> findOne(Long id) {
        log.debug("Request to get RFUser : {}", id);
        return rFUserRepository.findById(id).map(rFUserMapper::toDto);
    }

    /**
     * Delete the rFUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RFUser : {}", id);
        rFUserRepository.deleteById(id);
    }
}
