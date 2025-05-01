package com.nnroad.employee.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nnroad.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nnroad.common.annotation.Excel;

/**
 * Employee object for sys_employee
 * 
 * Represents an employee associated with a client and vendor.
 * 
 * @author nick
 * @date 2024-10-11
 */
public class SysEmployee extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Employee ID */
    private Long id;

    /** Client ID (foreign key linking to client table) */
    @Excel(name = "Client ID")
    private Long clientId;
    
    private String clientName;

    private String companyCode;

    private String employeeCode;
    
    public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	private String vendorName;
	
	private Object extraData;

    /** Employee's name */
    @Excel(name = "Employee Name")
    private String employeeName;

    /** Employee's email address */
    @Excel(name = "Employee Email")
    private String employeeEmail;

    /** Employee's phone number */
    @Excel(name = "Employee Phone")
    private String employeePhone;

    /** Vendor ID (foreign key linking to vendor table) */
    @Excel(name = "Vendor ID")
    private Long vendorId;

    /** Employee's work location */
    @Excel(name = "Location")
    private String location;

    @Excel(name = "Active")
    private Integer employeeActive;

    /** Status indicating if the vendor is active (1 for active, 0 for inactive) */

    /** Contract start date */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Contract Start Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date contractStartDate;

    /** Contract end date */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Contract End Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date contractEndDate;

    /** Record creation timestamp */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Created At", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;
    
    private String remark;

    private int businessTypeId;

    private String businessType;

	/** Record modification timestamp */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Modified At", width = 30, dateFormat = "yyyy-MM-dd")
    private Date modifiedAt;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    // Getter and setter methods
    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    
    public void setClientId(Long clientId) 
    {
        this.clientId = clientId;
    }

    public Long getClientId() 
    {
        return clientId;
    }

    public void setEmployeeName(String employeeName) 
    {
        this.employeeName = employeeName;
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

	public String getEmployeeName() 
    {
        return employeeName;
    }

    public void setEmployeeEmail(String employeeEmail) 
    {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeeEmail() 
    {
        return employeeEmail;
    }

    public void setEmployeePhone(String employeePhone) 
    {
        this.employeePhone = employeePhone;
    }

    public String getEmployeePhone() 
    {
        return employeePhone;
    }

    public void setVendorId(Long vendorId) 
    {
        this.vendorId = vendorId;
    }

    public Long getVendorId() 
    {
        return vendorId;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setContractStartDate(Date contractStartDate) 
    {
        this.contractStartDate = contractStartDate;
    }

    public Date getContractStartDate() 
    {
        return contractStartDate;
    }

    public void setContractEndDate(Date contractEndDate) 
    {
        this.contractEndDate = contractEndDate;
    }

    public Date getContractEndDate() 
    {
        return contractEndDate;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public int getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(int businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    public void setModifiedAt(Date modifiedAt) 
    {
        this.modifiedAt = modifiedAt;
    }

    public Date getModifiedAt() 
    {
        return modifiedAt;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Integer getEmployeeActive() {
        return employeeActive;
    }

    public void setEmployeeActive(Integer employeeActive) {
        this.employeeActive = employeeActive;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("clientId", getClientId())
            .append("employeeName", getEmployeeName())
            .append("employeeEmail", getEmployeeEmail())
            .append("employeePhone", getEmployeePhone())
            .append("vendorId", getVendorId())
            .append("location", getLocation())
            .append("contractStartDate", getContractStartDate())
            .append("contractEndDate", getContractEndDate())
            .append("createdAt", getCreatedAt())
            .append("modifiedAt", getModifiedAt())
                .append("employeeActive", getEmployeeActive())
            .toString();
    }

}
