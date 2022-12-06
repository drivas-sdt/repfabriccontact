package com.rebfabric.poc.contact.service.mapper;

import com.rebfabric.poc.contact.domain.Contact;
import com.rebfabric.poc.contact.domain.ContactPhone;
import com.rebfabric.poc.contact.service.dto.ContactDTO;
import com.rebfabric.poc.contact.service.dto.ContactPhoneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactPhone} and its DTO {@link ContactPhoneDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactPhoneMapper extends EntityMapper<ContactPhoneDTO, ContactPhone> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "contactId")
    ContactPhoneDTO toDto(ContactPhone s);

    @Named("contactId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactDTO toDtoContactId(Contact contact);
}
