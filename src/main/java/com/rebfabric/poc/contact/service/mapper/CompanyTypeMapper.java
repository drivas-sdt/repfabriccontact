package com.rebfabric.poc.contact.service.mapper;

import com.rebfabric.poc.contact.domain.CompanyType;
import com.rebfabric.poc.contact.service.dto.CompanyTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompanyType} and its DTO {@link CompanyTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyTypeMapper extends EntityMapper<CompanyTypeDTO, CompanyType> {}
