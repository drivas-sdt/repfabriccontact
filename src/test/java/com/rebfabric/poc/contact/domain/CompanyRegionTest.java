package com.rebfabric.poc.contact.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebfabric.poc.contact.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyRegionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyRegion.class);
        CompanyRegion companyRegion1 = new CompanyRegion();
        companyRegion1.setId(1L);
        CompanyRegion companyRegion2 = new CompanyRegion();
        companyRegion2.setId(companyRegion1.getId());
        assertThat(companyRegion1).isEqualTo(companyRegion2);
        companyRegion2.setId(2L);
        assertThat(companyRegion1).isNotEqualTo(companyRegion2);
        companyRegion1.setId(null);
        assertThat(companyRegion1).isNotEqualTo(companyRegion2);
    }
}
