package com.rebfabric.poc.contact.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebfabric.poc.contact.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactPhoneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactPhone.class);
        ContactPhone contactPhone1 = new ContactPhone();
        contactPhone1.setId(1L);
        ContactPhone contactPhone2 = new ContactPhone();
        contactPhone2.setId(contactPhone1.getId());
        assertThat(contactPhone1).isEqualTo(contactPhone2);
        contactPhone2.setId(2L);
        assertThat(contactPhone1).isNotEqualTo(contactPhone2);
        contactPhone1.setId(null);
        assertThat(contactPhone1).isNotEqualTo(contactPhone2);
    }
}
