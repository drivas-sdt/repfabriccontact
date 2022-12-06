package com.rebfabric.poc.contact.service;

import com.rebfabric.poc.contact.domain.CompanyRegion;
import com.rebfabric.poc.contact.repository.CompanyRegionRepository;
import com.rebfabric.poc.contact.service.dto.CompanyRegionDTO;
import com.rebfabric.poc.contact.service.mapper.CompanyRegionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompanyRegion}.
 */
@Service
@Transactional
public class CompanyRegionService {

    private final Logger log = LoggerFactory.getLogger(CompanyRegionService.class);

    private final CompanyRegionRepository companyRegionRepository;

    private final CompanyRegionMapper companyRegionMapper;

    public CompanyRegionService(CompanyRegionRepository companyRegionRepository, CompanyRegionMapper companyRegionMapper) {
        this.companyRegionRepository = companyRegionRepository;
        this.companyRegionMapper = companyRegionMapper;
    }

    /**
     * Save a companyRegion.
     *
     * @param companyRegionDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyRegionDTO save(CompanyRegionDTO companyRegionDTO) {
        log.debug("Request to save CompanyRegion : {}", companyRegionDTO);
        CompanyRegion companyRegion = companyRegionMapper.toEntity(companyRegionDTO);
        companyRegion = companyRegionRepository.save(companyRegion);
        return companyRegionMapper.toDto(companyRegion);
    }

    /**
     * Update a companyRegion.
     *
     * @param companyRegionDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyRegionDTO update(CompanyRegionDTO companyRegionDTO) {
        log.debug("Request to update CompanyRegion : {}", companyRegionDTO);
        CompanyRegion companyRegion = companyRegionMapper.toEntity(companyRegionDTO);
        companyRegion = companyRegionRepository.save(companyRegion);
        return companyRegionMapper.toDto(companyRegion);
    }

    /**
     * Partially update a companyRegion.
     *
     * @param companyRegionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyRegionDTO> partialUpdate(CompanyRegionDTO companyRegionDTO) {
        log.debug("Request to partially update CompanyRegion : {}", companyRegionDTO);

        return companyRegionRepository
            .findById(companyRegionDTO.getId())
            .map(existingCompanyRegion -> {
                companyRegionMapper.partialUpdate(existingCompanyRegion, companyRegionDTO);

                return existingCompanyRegion;
            })
            .map(companyRegionRepository::save)
            .map(companyRegionMapper::toDto);
    }

    /**
     * Get all the companyRegions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CompanyRegionDTO> findAll() {
        log.debug("Request to get all CompanyRegions");
        return companyRegionRepository.findAll().stream().map(companyRegionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one companyRegion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyRegionDTO> findOne(Long id) {
        log.debug("Request to get CompanyRegion : {}", id);
        return companyRegionRepository.findById(id).map(companyRegionMapper::toDto);
    }

    /**
     * Delete the companyRegion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CompanyRegion : {}", id);
        companyRegionRepository.deleteById(id);
    }
}
