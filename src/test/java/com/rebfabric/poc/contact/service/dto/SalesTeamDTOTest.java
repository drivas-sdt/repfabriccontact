package com.rebfabric.poc.contact.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebfabric.poc.contact.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalesTeamDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesTeamDTO.class);
        SalesTeamDTO salesTeamDTO1 = new SalesTeamDTO();
        salesTeamDTO1.setId(1L);
        SalesTeamDTO salesTeamDTO2 = new SalesTeamDTO();
        assertThat(salesTeamDTO1).isNotEqualTo(salesTeamDTO2);
        salesTeamDTO2.setId(salesTeamDTO1.getId());
        assertThat(salesTeamDTO1).isEqualTo(salesTeamDTO2);
        salesTeamDTO2.setId(2L);
        assertThat(salesTeamDTO1).isNotEqualTo(salesTeamDTO2);
        salesTeamDTO1.setId(null);
        assertThat(salesTeamDTO1).isNotEqualTo(salesTeamDTO2);
    }
}
