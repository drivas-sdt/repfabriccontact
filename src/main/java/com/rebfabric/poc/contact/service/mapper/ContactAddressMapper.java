package com.rebfabric.poc.contact.service.mapper;

import com.rebfabric.poc.contact.domain.Contact;
import com.rebfabric.poc.contact.domain.ContactAddress;
import com.rebfabric.poc.contact.service.dto.ContactAddressDTO;
import com.rebfabric.poc.contact.service.dto.ContactDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactAddress} and its DTO {@link ContactAddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactAddressMapper extends EntityMapper<ContactAddressDTO, ContactAddress> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "contactId")
    ContactAddressDTO toDto(ContactAddress s);

    @Named("contactId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactDTO toDtoContactId(Contact contact);
}
