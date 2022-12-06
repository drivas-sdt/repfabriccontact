package com.rebfabric.poc.contact.service.mapper;

import com.rebfabric.poc.contact.domain.Contact;
import com.rebfabric.poc.contact.domain.ContactEmail;
import com.rebfabric.poc.contact.service.dto.ContactDTO;
import com.rebfabric.poc.contact.service.dto.ContactEmailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactEmail} and its DTO {@link ContactEmailDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactEmailMapper extends EntityMapper<ContactEmailDTO, ContactEmail> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "contactId")
    ContactEmailDTO toDto(ContactEmail s);

    @Named("contactId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactDTO toDtoContactId(Contact contact);
}
