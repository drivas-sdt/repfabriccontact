package com.rebfabric.poc.contact.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebfabric.poc.contact.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactPhoneDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactPhoneDTO.class);
        ContactPhoneDTO contactPhoneDTO1 = new ContactPhoneDTO();
        contactPhoneDTO1.setId(1L);
        ContactPhoneDTO contactPhoneDTO2 = new ContactPhoneDTO();
        assertThat(contactPhoneDTO1).isNotEqualTo(contactPhoneDTO2);
        contactPhoneDTO2.setId(contactPhoneDTO1.getId());
        assertThat(contactPhoneDTO1).isEqualTo(contactPhoneDTO2);
        contactPhoneDTO2.setId(2L);
        assertThat(contactPhoneDTO1).isNotEqualTo(contactPhoneDTO2);
        contactPhoneDTO1.setId(null);
        assertThat(contactPhoneDTO1).isNotEqualTo(contactPhoneDTO2);
    }
}
