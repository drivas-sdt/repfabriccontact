package com.rebfabric.poc.contact.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ContactEmail.
 */
@Entity
@Table(name = "contact_email")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "email_type", nullable = false)
    private Integer emailType;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

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

    public ContactEmail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEmailType() {
        return this.emailType;
    }

    public ContactEmail emailType(Integer emailType) {
        this.setEmailType(emailType);
        return this;
    }

    public void setEmailType(Integer emailType) {
        this.emailType = emailType;
    }

    public String getEmail() {
        return this.email;
    }

    public ContactEmail email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ContactEmail contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactEmail)) {
            return false;
        }
        return id != null && id.equals(((ContactEmail) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactEmail{" +
            "id=" + getId() +
            ", emailType=" + getEmailType() +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
