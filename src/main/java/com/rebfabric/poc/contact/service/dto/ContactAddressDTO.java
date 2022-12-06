package com.rebfabric.poc.contact.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.rebfabric.poc.contact.domain.ContactAddress} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactAddressDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer addressType;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    private String country;

    private String formattedAddress;

    private String poBox;

    private ContactDTO contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAddressType() {
        return addressType;
    }

    public void setAddressType(Integer addressType) {
        this.addressType = addressType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getPoBox() {
        return poBox;
    }

    public void setPoBox(String poBox) {
        this.poBox = poBox;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactAddressDTO)) {
            return false;
        }

        ContactAddressDTO contactAddressDTO = (ContactAddressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactAddressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactAddressDTO{" +
            "id=" + getId() +
            ", addressType=" + getAddressType() +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", country='" + getCountry() + "'" +
            ", formattedAddress='" + getFormattedAddress() + "'" +
            ", poBox='" + getPoBox() + "'" +
            ", contact=" + getContact() +
            "}";
    }
}
