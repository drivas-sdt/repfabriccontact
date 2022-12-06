package com.rebfabric.poc.contact.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.rebfabric.poc.contact.domain.RFUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RFUserDTO implements Serializable {

    private Long id;

    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RFUserDTO)) {
            return false;
        }

        RFUserDTO rFUserDTO = (RFUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rFUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RFUserDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
