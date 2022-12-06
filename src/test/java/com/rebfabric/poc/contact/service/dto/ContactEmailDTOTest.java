package com.rebfabric.poc.contact.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebfabric.poc.contact.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactEmailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactEmailDTO.class);
        ContactEmailDTO contactEmailDTO1 = new ContactEmailDTO();
        contactEmailDTO1.setId(1L);
        ContactEmailDTO contactEmailDTO2 = new ContactEmailDTO();
        assertThat(contactEmailDTO1).isNotEqualTo(contactEmailDTO2);
        contactEmailDTO2.setId(contactEmailDTO1.getId());
        assertThat(contactEmailDTO1).isEqualTo(contactEmailDTO2);
        contactEmailDTO2.setId(2L);
        assertThat(contactEmailDTO1).isNotEqualTo(contactEmailDTO2);
        contactEmailDTO1.setId(null);
        assertThat(contactEmailDTO1).isNotEqualTo(contactEmailDTO2);
    }
}
