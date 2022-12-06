package com.rebfabric.poc.contact.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A SalesTeam.
 */
@Entity
@Table(name = "sales_team")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalesTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "salesTeam")
    @JsonIgnoreProperties(value = { "salesTeam", "companyType" }, allowSetters = true)
    private Set<Company> companies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SalesTeam id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SalesTeam name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Company> getCompanies() {
        return this.companies;
    }

    public void setCompanies(Set<Company> companies) {
        if (this.companies != null) {
            this.companies.forEach(i -> i.setSalesTeam(null));
        }
        if (companies != null) {
            companies.forEach(i -> i.setSalesTeam(this));
        }
        this.companies = companies;
    }

    public SalesTeam companies(Set<Company> companies) {
        this.setCompanies(companies);
        return this;
    }

    public SalesTeam addCompany(Company company) {
        this.companies.add(company);
        company.setSalesTeam(this);
        return this;
    }

    public SalesTeam removeCompany(Company company) {
        this.companies.remove(company);
        company.setSalesTeam(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalesTeam)) {
            return false;
        }
        return id != null && id.equals(((SalesTeam) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalesTeam{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
