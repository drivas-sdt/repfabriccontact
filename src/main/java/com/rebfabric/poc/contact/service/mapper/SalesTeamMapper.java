package com.rebfabric.poc.contact.service.mapper;

import com.rebfabric.poc.contact.domain.SalesTeam;
import com.rebfabric.poc.contact.service.dto.SalesTeamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SalesTeam} and its DTO {@link SalesTeamDTO}.
 */
@Mapper(componentModel = "spring")
public interface SalesTeamMapper extends EntityMapper<SalesTeamDTO, SalesTeam> {}
