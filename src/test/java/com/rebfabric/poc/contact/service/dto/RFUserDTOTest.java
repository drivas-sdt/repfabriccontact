package com.rebfabric.poc.contact.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebfabric.poc.contact.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RFUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RFUserDTO.class);
        RFUserDTO rFUserDTO1 = new RFUserDTO();
        rFUserDTO1.setId(1L);
        RFUserDTO rFUserDTO2 = new RFUserDTO();
        assertThat(rFUserDTO1).isNotEqualTo(rFUserDTO2);
        rFUserDTO2.setId(rFUserDTO1.getId());
        assertThat(rFUserDTO1).isEqualTo(rFUserDTO2);
        rFUserDTO2.setId(2L);
        assertThat(rFUserDTO1).isNotEqualTo(rFUserDTO2);
        rFUserDTO1.setId(null);
        assertThat(rFUserDTO1).isNotEqualTo(rFUserDTO2);
    }
}
