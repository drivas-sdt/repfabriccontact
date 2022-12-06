package com.rebfabric.poc.contact.service;

import com.rebfabric.poc.contact.domain.CompanyType;
import com.rebfabric.poc.contact.repository.CompanyTypeRepository;
import com.rebfabric.poc.contact.service.dto.CompanyTypeDTO;
import com.rebfabric.poc.contact.service.mapper.CompanyTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompanyType}.
 */
@Service
@Transactional
public class CompanyTypeService {

    private final Logger log = LoggerFactory.getLogger(CompanyTypeService.class);

    private final CompanyTypeRepository companyTypeRepository;

    private final CompanyTypeMapper companyTypeMapper;

    public CompanyTypeService(CompanyTypeRepository companyTypeRepository, CompanyTypeMapper companyTypeMapper) {
        this.companyTypeRepository = companyTypeRepository;
        this.companyTypeMapper = companyTypeMapper;
    }

    /**
     * Save a companyType.
     *
     * @param companyTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyTypeDTO save(CompanyTypeDTO companyTypeDTO) {
        log.debug("Request to save CompanyType : {}", companyTypeDTO);
        CompanyType companyType = companyTypeMapper.toEntity(companyTypeDTO);
        companyType = companyTypeRepository.save(companyType);
        return companyTypeMapper.toDto(companyType);
    }

    /**
     * Update a companyType.
     *
     * @param companyTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyTypeDTO update(CompanyTypeDTO companyTypeDTO) {
        log.debug("Request to update CompanyType : {}", companyTypeDTO);
        CompanyType companyType = companyTypeMapper.toEntity(companyTypeDTO);
        companyType = companyTypeRepository.save(companyType);
        return companyTypeMapper.toDto(companyType);
    }

    /**
     * Partially update a companyType.
     *
     * @param companyTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyTypeDTO> partialUpdate(CompanyTypeDTO companyTypeDTO) {
        log.debug("Request to partially update CompanyType : {}", companyTypeDTO);

        return companyTypeRepository
            .findById(companyTypeDTO.getId())
            .map(existingCompanyType -> {
                companyTypeMapper.partialUpdate(existingCompanyType, companyTypeDTO);

                return existingCompanyType;
            })
            .map(companyTypeRepository::save)
            .map(companyTypeMapper::toDto);
    }

    /**
     * Get all the companyTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CompanyTypeDTO> findAll() {
        log.debug("Request to get all CompanyTypes");
        return companyTypeRepository.findAll().stream().map(companyTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one companyType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyTypeDTO> findOne(Long id) {
        log.debug("Request to get CompanyType : {}", id);
        return companyTypeRepository.findById(id).map(companyTypeMapper::toDto);
    }

    /**
     * Delete the companyType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CompanyType : {}", id);
        companyTypeRepository.deleteById(id);
    }
}
