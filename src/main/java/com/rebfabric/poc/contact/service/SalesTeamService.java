package com.rebfabric.poc.contact.service;

import com.rebfabric.poc.contact.domain.SalesTeam;
import com.rebfabric.poc.contact.repository.SalesTeamRepository;
import com.rebfabric.poc.contact.service.dto.SalesTeamDTO;
import com.rebfabric.poc.contact.service.mapper.SalesTeamMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SalesTeam}.
 */
@Service
@Transactional
public class SalesTeamService {

    private final Logger log = LoggerFactory.getLogger(SalesTeamService.class);

    private final SalesTeamRepository salesTeamRepository;

    private final SalesTeamMapper salesTeamMapper;

    public SalesTeamService(SalesTeamRepository salesTeamRepository, SalesTeamMapper salesTeamMapper) {
        this.salesTeamRepository = salesTeamRepository;
        this.salesTeamMapper = salesTeamMapper;
    }

    /**
     * Save a salesTeam.
     *
     * @param salesTeamDTO the entity to save.
     * @return the persisted entity.
     */
    public SalesTeamDTO save(SalesTeamDTO salesTeamDTO) {
        log.debug("Request to save SalesTeam : {}", salesTeamDTO);
        SalesTeam salesTeam = salesTeamMapper.toEntity(salesTeamDTO);
        salesTeam = salesTeamRepository.save(salesTeam);
        return salesTeamMapper.toDto(salesTeam);
    }

    /**
     * Update a salesTeam.
     *
     * @param salesTeamDTO the entity to save.
     * @return the persisted entity.
     */
    public SalesTeamDTO update(SalesTeamDTO salesTeamDTO) {
        log.debug("Request to update SalesTeam : {}", salesTeamDTO);
        SalesTeam salesTeam = salesTeamMapper.toEntity(salesTeamDTO);
        salesTeam = salesTeamRepository.save(salesTeam);
        return salesTeamMapper.toDto(salesTeam);
    }

    /**
     * Partially update a salesTeam.
     *
     * @param salesTeamDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SalesTeamDTO> partialUpdate(SalesTeamDTO salesTeamDTO) {
        log.debug("Request to partially update SalesTeam : {}", salesTeamDTO);

        return salesTeamRepository
            .findById(salesTeamDTO.getId())
            .map(existingSalesTeam -> {
                salesTeamMapper.partialUpdate(existingSalesTeam, salesTeamDTO);

                return existingSalesTeam;
            })
            .map(salesTeamRepository::save)
            .map(salesTeamMapper::toDto);
    }

    /**
     * Get all the salesTeams.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SalesTeamDTO> findAll() {
        log.debug("Request to get all SalesTeams");
        return salesTeamRepository.findAll().stream().map(salesTeamMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one salesTeam by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SalesTeamDTO> findOne(Long id) {
        log.debug("Request to get SalesTeam : {}", id);
        return salesTeamRepository.findById(id).map(salesTeamMapper::toDto);
    }

    /**
     * Delete the salesTeam by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SalesTeam : {}", id);
        salesTeamRepository.deleteById(id);
    }
}
