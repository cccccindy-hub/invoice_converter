package com.nnroad.vendor.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents a vendor entity in the system.
 * 
 * @author nick
 * @date 2024-10-11
 */
public class SysVendor {
    private static final long serialVersionUID = 1L;

    /** Unique identifier for the vendor */
    private Long id;

    /** Name of the vendor */
    @Excel(name = "Vendor Name")
    private String vendorName;

    /** Location of the vendor */
    private String location;

    /** Name of the contact person */
    @Excel(name = "Contact Person Name")
    private String contactPersonName;

    /** Phone number of the contact person */
    @Excel(name = "Contact Person Phone")
    private String contactPersonPhone;

    /** Email of the contact person */
    @Excel(name = "Contact Person Email")
    private String contactPersonEmail;

    /** Website of the vendor company */
    @Excel(name = "Company Website")
    private String companyWebsite;

    /** Phone number of the company */
    @Excel(name = "Company Phone")
    private String companyPhone;

    /** Email of the company */
    @Excel(name = "Company Email")
    private String companyEmail;

    /** City where the office is located */
    @Excel(name = "Office City")
    private String officeCity;

    /** Status indicating if the vendor is active (1 for active, 0 for inactive) */
    @Excel(name = "vendorActive")
    private Integer vendorActive;

    /** Start date of the vendor's engagement */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Start Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** End date of the vendor's engagement */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "End Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;
    
    private Object extraData;

    private String businessTypeId;

    private String businessType;

    /** Timestamp for when the record was created */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Created At", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** Timestamp for when the record was last modified */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Modified At", width = 30, dateFormat = "yyyy-MM-dd")
    private Date modifiedAt;

    // Getter and setter methods

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setOfficeCity(String officeCity) {
        this.officeCity = officeCity;
    }

    public String getOfficeCity() {
        return officeCity;
    }

    public void setVendorActive(Integer vendorActive) {
        this.vendorActive = vendorActive;
    }

    public Integer getVendorActive() {
        return vendorActive;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
	 * @return the extraData
	 */
	public Object getExtraData() {
		return extraData;
	}

	/**
	 * @param extraData the extraData to set
	 */
	public void setExtraData(Object extraData) {
		this.extraData = extraData;
	}

	public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("vendorName", getVendorName())
            .append("location", getLocation())
            .append("contactPersonName", getContactPersonName())
            .append("contactPersonPhone", getContactPersonPhone())
            .append("contactPersonEmail", getContactPersonEmail())
            .append("companyWebsite", getCompanyWebsite())
            .append("companyPhone", getCompanyPhone())
            .append("companyEmail", getCompanyEmail())
            .append("officeCity", getOfficeCity())
            .append("vendorActive", getVendorActive())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("createdAt", getCreatedAt())
            .append("modifiedAt", getModifiedAt())
            .toString();
    }
}
