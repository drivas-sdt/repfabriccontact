package com.rebfabric.poc.contact.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebfabric.poc.contact.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactEmailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactEmail.class);
        ContactEmail contactEmail1 = new ContactEmail();
        contactEmail1.setId(1L);
        ContactEmail contactEmail2 = new ContactEmail();
        contactEmail2.setId(contactEmail1.getId());
        assertThat(contactEmail1).isEqualTo(contactEmail2);
        contactEmail2.setId(2L);
        assertThat(contactEmail1).isNotEqualTo(contactEmail2);
        contactEmail1.setId(null);
        assertThat(contactEmail1).isNotEqualTo(contactEmail2);
    }
}
