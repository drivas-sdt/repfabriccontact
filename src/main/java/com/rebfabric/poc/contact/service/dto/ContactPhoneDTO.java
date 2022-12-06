package com.rebfabric.poc.contact.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.rebfabric.poc.contact.domain.ContactPhone} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactPhoneDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer phoneType;

    @NotNull
    private String phone;

    private ContactDTO contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(Integer phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        if (!(o instanceof ContactPhoneDTO)) {
            return false;
        }

        ContactPhoneDTO contactPhoneDTO = (ContactPhoneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactPhoneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactPhoneDTO{" +
            "id=" + getId() +
            ", phoneType=" + getPhoneType() +
            ", phone='" + getPhone() + "'" +
            ", contact=" + getContact() +
            "}";
    }
}
