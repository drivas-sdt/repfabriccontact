package com.rebfabric.poc.contact.service.mapper;

import com.rebfabric.poc.contact.domain.RFUser;
import com.rebfabric.poc.contact.service.dto.RFUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RFUser} and its DTO {@link RFUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface RFUserMapper extends EntityMapper<RFUserDTO, RFUser> {}
