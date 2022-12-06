package com.rebfabric.poc.contact.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ContactPhone.
 */
@Entity
@Table(name = "contact_phone")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactPhone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "phone_type", nullable = false)
    private Integer phoneType;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "company", "user", "salesTeam", "createrUser", "updateUser", "parentCompany", "region" },
        allowSetters = true
    )
    private Contact contact;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContactPhone id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPhoneType() {
        return this.phoneType;
    }

    public ContactPhone phoneType(Integer phoneType) {
        this.setPhoneType(phoneType);
        return this;
    }

    public void setPhoneType(Integer phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhone() {
        return this.phone;
    }

    public ContactPhone phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ContactPhone contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactPhone)) {
            return false;
        }
        return id != null && id.equals(((ContactPhone) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactPhone{" +
            "id=" + getId() +
            ", phoneType=" + getPhoneType() +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
