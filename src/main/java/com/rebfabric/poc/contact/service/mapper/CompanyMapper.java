package com.rebfabric.poc.contact.service.mapper;

import com.rebfabric.poc.contact.domain.Company;
import com.rebfabric.poc.contact.domain.CompanyType;
import com.rebfabric.poc.contact.domain.SalesTeam;
import com.rebfabric.poc.contact.service.dto.CompanyDTO;
import com.rebfabric.poc.contact.service.dto.CompanyTypeDTO;
import com.rebfabric.poc.contact.service.dto.SalesTeamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Mapping(target = "salesTeam", source = "salesTeam", qualifiedByName = "salesTeamId")
    @Mapping(target = "companyType", source = "companyType", qualifiedByName = "companyTypeId")
    CompanyDTO toDto(Company s);

    @Named("salesTeamId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SalesTeamDTO toDtoSalesTeamId(SalesTeam salesTeam);

    @Named("companyTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyTypeDTO toDtoCompanyTypeId(CompanyType companyType);
}
