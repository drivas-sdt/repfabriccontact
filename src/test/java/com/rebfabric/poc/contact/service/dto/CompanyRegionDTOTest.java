package com.rebfabric.poc.contact.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebfabric.poc.contact.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyRegionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyRegionDTO.class);
        CompanyRegionDTO companyRegionDTO1 = new CompanyRegionDTO();
        companyRegionDTO1.setId(1L);
        CompanyRegionDTO companyRegionDTO2 = new CompanyRegionDTO();
        assertThat(companyRegionDTO1).isNotEqualTo(companyRegionDTO2);
        companyRegionDTO2.setId(companyRegionDTO1.getId());
        assertThat(companyRegionDTO1).isEqualTo(companyRegionDTO2);
        companyRegionDTO2.setId(2L);
        assertThat(companyRegionDTO1).isNotEqualTo(companyRegionDTO2);
        companyRegionDTO1.setId(null);
        assertThat(companyRegionDTO1).isNotEqualTo(companyRegionDTO2);
    }
}
