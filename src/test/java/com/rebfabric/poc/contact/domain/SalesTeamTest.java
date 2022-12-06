package com.rebfabric.poc.contact.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebfabric.poc.contact.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalesTeamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesTeam.class);
        SalesTeam salesTeam1 = new SalesTeam();
        salesTeam1.setId(1L);
        SalesTeam salesTeam2 = new SalesTeam();
        salesTeam2.setId(salesTeam1.getId());
        assertThat(salesTeam1).isEqualTo(salesTeam2);
        salesTeam2.setId(2L);
        assertThat(salesTeam1).isNotEqualTo(salesTeam2);
        salesTeam1.setId(null);
        assertThat(salesTeam1).isNotEqualTo(salesTeam2);
    }
}
