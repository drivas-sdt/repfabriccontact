package com.rebfabric.poc.contact.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ContactAddress.
 */
@Entity
@Table(name = "contact_address")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "address_type", nullable = false)
    private Integer addressType;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "country")
    private String country;

    @Column(name = "formatted_address")
    private String formattedAddress;

    @Column(name = "po_box")
    private String poBox;

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

    public ContactAddress id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAddressType() {
        return this.addressType;
    }

    public ContactAddress addressType(Integer addressType) {
        this.setAddressType(addressType);
        return this;
    }

    public void setAddressType(Integer addressType) {
        this.addressType = addressType;
    }

    public String getAddress() {
        return this.address;
    }

    public ContactAddress address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public ContactAddress city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public ContactAddress state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public ContactAddress zipCode(String zipCode) {
        this.setZipCode(zipCode);
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return this.country;
    }

    public ContactAddress country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFormattedAddress() {
        return this.formattedAddress;
    }

    public ContactAddress formattedAddress(String formattedAddress) {
        this.setFormattedAddress(formattedAddress);
        return this;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getPoBox() {
        return this.poBox;
    }

    public ContactAddress poBox(String poBox) {
        this.setPoBox(poBox);
        return this;
    }

    public void setPoBox(String poBox) {
        this.poBox = poBox;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ContactAddress contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactAddress)) {
            return false;
        }
        return id != null && id.equals(((ContactAddress) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactAddress{" +
            "id=" + getId() +
            ", addressType=" + getAddressType() +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", country='" + getCountry() + "'" +
            ", formattedAddress='" + getFormattedAddress() + "'" +
            ", poBox='" + getPoBox() + "'" +
            "}";
    }
}
