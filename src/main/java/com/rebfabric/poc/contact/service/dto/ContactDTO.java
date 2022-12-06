package com.rebfabric.poc.contact.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.rebfabric.poc.contact.domain.Contact} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactDTO implements Serializable {

    private Long id;

    private String title;

    private String firstName;

    private String middleName;

    private String lastName;

    private String suffix;

    private String fullName;

    private String jobTitle;

    private String dept;

    private String location;

    private String managerName;

    private String assistantName;

    private String referredBy;

    private String context;

    private Boolean isPrimary;

    private Boolean isDelete;

    private String imagePAth;

    private String notes;

    private LocalDate createDate;

    private LocalDate updateDate;

    private String refNo;

    private Boolean isGlobalContact;

    private Boolean isBounce;

    private Boolean isActive;

    private Boolean isSync;

    private CompanyDTO company;

    private RFUserDTO user;

    private SalesTeamDTO salesTeam;

    private RFUserDTO createrUser;

    private RFUserDTO updateUser;

    private CompanyDTO parentCompany;

    private CompanyRegionDTO region;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getImagePAth() {
        return imagePAth;
    }

    public void setImagePAth(String imagePAth) {
        this.imagePAth = imagePAth;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public Boolean getIsGlobalContact() {
        return isGlobalContact;
    }

    public void setIsGlobalContact(Boolean isGlobalContact) {
        this.isGlobalContact = isGlobalContact;
    }

    public Boolean getIsBounce() {
        return isBounce;
    }

    public void setIsBounce(Boolean isBounce) {
        this.isBounce = isBounce;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsSync() {
        return isSync;
    }

    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public RFUserDTO getUser() {
        return user;
    }

    public void setUser(RFUserDTO user) {
        this.user = user;
    }

    public SalesTeamDTO getSalesTeam() {
        return salesTeam;
    }

    public void setSalesTeam(SalesTeamDTO salesTeam) {
        this.salesTeam = salesTeam;
    }

    public RFUserDTO getCreaterUser() {
        return createrUser;
    }

    public void setCreaterUser(RFUserDTO createrUser) {
        this.createrUser = createrUser;
    }

    public RFUserDTO getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(RFUserDTO updateUser) {
        this.updateUser = updateUser;
    }

    public CompanyDTO getParentCompany() {
        return parentCompany;
    }

    public void setParentCompany(CompanyDTO parentCompany) {
        this.parentCompany = parentCompany;
    }

    public CompanyRegionDTO getRegion() {
        return region;
    }

    public void setRegion(CompanyRegionDTO region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactDTO)) {
            return false;
        }

        ContactDTO contactDTO = (ContactDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", suffix='" + getSuffix() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", dept='" + getDept() + "'" +
            ", location='" + getLocation() + "'" +
            ", managerName='" + getManagerName() + "'" +
            ", assistantName='" + getAssistantName() + "'" +
            ", referredBy='" + getReferredBy() + "'" +
            ", context='" + getContext() + "'" +
            ", isPrimary='" + getIsPrimary() + "'" +
            ", isDelete='" + getIsDelete() + "'" +
            ", imagePAth='" + getImagePAth() + "'" +
            ", notes='" + getNotes() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", refNo='" + getRefNo() + "'" +
            ", isGlobalContact='" + getIsGlobalContact() + "'" +
            ", isBounce='" + getIsBounce() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isSync='" + getIsSync() + "'" +
            ", company=" + getCompany() +
            ", user=" + getUser() +
            ", salesTeam=" + getSalesTeam() +
            ", createrUser=" + getCreaterUser() +
            ", updateUser=" + getUpdateUser() +
            ", parentCompany=" + getParentCompany() +
            ", region=" + getRegion() +
            "}";
    }
}
