package com.rebfabric.poc.contact.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebfabric.poc.contact.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RFUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RFUser.class);
        RFUser rFUser1 = new RFUser();
        rFUser1.setId(1L);
        RFUser rFUser2 = new RFUser();
        rFUser2.setId(rFUser1.getId());
        assertThat(rFUser1).isEqualTo(rFUser2);
        rFUser2.setId(2L);
        assertThat(rFUser1).isNotEqualTo(rFUser2);
        rFUser1.setId(null);
        assertThat(rFUser1).isNotEqualTo(rFUser2);
    }
}
