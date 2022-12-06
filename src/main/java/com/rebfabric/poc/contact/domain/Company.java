package com.rebfabric.poc.contact.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "priv_team")
    private String privTeam;

    @ManyToOne
    @JsonIgnoreProperties(value = { "companies" }, allowSetters = true)
    private SalesTeam salesTeam;

    @ManyToOne
    @JsonIgnoreProperties(value = { "companies" }, allowSetters = true)
    private CompanyType companyType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Company id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Company name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrivTeam() {
        return this.privTeam;
    }

    public Company privTeam(String privTeam) {
        this.setPrivTeam(privTeam);
        return this;
    }

    public void setPrivTeam(String privTeam) {
        this.privTeam = privTeam;
    }

    public SalesTeam getSalesTeam() {
        return this.salesTeam;
    }

    public void setSalesTeam(SalesTeam salesTeam) {
        this.salesTeam = salesTeam;
    }

    public Company salesTeam(SalesTeam salesTeam) {
        this.setSalesTeam(salesTeam);
        return this;
    }

    public CompanyType getCompanyType() {
        return this.companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public Company companyType(CompanyType companyType) {
        this.setCompanyType(companyType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", privTeam='" + getPrivTeam() + "'" +
            "}";
    }
}
