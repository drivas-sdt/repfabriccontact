package com.rebfabric.poc.contact.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.rebfabric.poc.contact.domain.Company} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompanyDTO implements Serializable {

    private Long id;

    private String name;

    private String privTeam;

    private SalesTeamDTO salesTeam;

    private CompanyTypeDTO companyType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrivTeam() {
        return privTeam;
    }

    public void setPrivTeam(String privTeam) {
        this.privTeam = privTeam;
    }

    public SalesTeamDTO getSalesTeam() {
        return salesTeam;
    }

    public void setSalesTeam(SalesTeamDTO salesTeam) {
        this.salesTeam = salesTeam;
    }

    public CompanyTypeDTO getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyTypeDTO companyType) {
        this.companyType = companyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyDTO)) {
            return false;
        }

        CompanyDTO companyDTO = (CompanyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", privTeam='" + getPrivTeam() + "'" +
            ", salesTeam=" + getSalesTeam() +
            ", companyType=" + getCompanyType() +
            "}";
    }
}
