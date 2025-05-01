package com.nnroad.client.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nnroad.common.annotation.Excel;

import com.nnroad.employee.domain.SysEmployee;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author nick
 * @date 2024-10-10
 */
public class SysClient implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String clientName;

    private String companyCode;

    private String clientLocation;

    private List<String> serviceCountry;
    
	private String industry;

    private String primaryServiceType;

    private String secondaryServiceType;

    private List<String> otherServiceTypes;

    private String serviceTypeRemark;

    private String contactPersonName;

    private String contactPersonEmail;

    private String contactPhoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Contract Start Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Contract Start Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;
    
    private String remark;
    
    
    private Object extraData;

    private Integer clientActive;

    public Object getExtraData() {
		return extraData;
	}

	public void setExtraData(Object extraData) {
		this.extraData = extraData;
	}

	private Date createdAt;

    private Date modifiedAt;
    
    // List of associated Employees
    private List<SysEmployee> employees;

    
	public List<SysEmployee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<SysEmployee> employees) {
		this.employees = employees;
	}

	public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setClientName(String clientName) 
    {
        this.clientName = clientName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getClientName() 
    {
        return clientName;
    }
    public void setClientLocation(String clientLocation) 
    {
        this.clientLocation = clientLocation;
    }

    public String getClientLocation() 
    {
        return clientLocation;
    }
    
	/**
	 * @param serviceCountry the serviceCountry to set
	 */
	public void setServiceCountry(List<String> serviceCountry) {
		this.serviceCountry = serviceCountry;
	}
	
	public List<String> getServiceCountry() 
    {
        return serviceCountry;
    }

    public String getIndustry() 
    {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setPrimaryServiceType(String primaryServiceType)
    {
        this.primaryServiceType = primaryServiceType;
    }

    public String getPrimaryServiceType() 
    {
        return primaryServiceType;
    }
    public void setSecondaryServiceType(String secondaryServiceType) 
    {
        this.secondaryServiceType = secondaryServiceType;
    }

    public String getSecondaryServiceType() 
    {
        return secondaryServiceType;
    }
    public void setOtherServiceTypes(List<String> otherServiceTypes) 
    {
        this.otherServiceTypes = otherServiceTypes;
    }

    public List<String> getOtherServiceTypes() 
    {
        return otherServiceTypes;
    }
    public void setServiceTypeRemark(String serviceTypeRemark) 
    {
        this.serviceTypeRemark = serviceTypeRemark;
    }

    public String getServiceTypeRemark() 
    {
        return serviceTypeRemark;
    }
    public void setContactPersonName(String contactPersonName) 
    {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonName() 
    {
        return contactPersonName;
    }
    public void setContactPersonEmail(String contactPersonEmail) 
    {
        this.contactPersonEmail = contactPersonEmail;
    }

    public String getContactPersonEmail() 
    {
        return contactPersonEmail;
    }
    public void setContactPhoneNumber(String contactPhoneNumber) 
    {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getContactPhoneNumber() 
    {
        return contactPhoneNumber;
    }
    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }
    
    public Date getStartDate() 
    {
        return startDate;
    }
    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }
    
    public void setRemark(String remark) {
    	this.remark = remark;
    }
    
    public String getRemark() {
    	return this.remark;
    }
    
    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
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

    // Getter for 'active'
    public Integer getClientActive() {
        return clientActive;
    }

    // Setter for 'active'
    public void setClientActive(Integer clientActive) {
        this.clientActive = clientActive;
    }
//need to add actice
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("clientName", getClientName())
            .append("clientLocation", getClientLocation())
            .append("serviceCountry", getServiceCountry())
            .append("industry", getIndustry())
            .append("primaryServiceType", getPrimaryServiceType())
            .append("secondaryServiceType", getSecondaryServiceType())
            .append("otherServiceTypes", getOtherServiceTypes())
            .append("serviceTypeRemark", getServiceTypeRemark())
            .append("contactPersonName", getContactPersonName())
            .append("contactPersonEmail", getContactPersonEmail())
            .append("contactPhoneNumber", getContactPhoneNumber())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("remark", getRemark())
            .append("createdAt", getCreatedAt())
            .append("modifiedAt", getModifiedAt())
                .append("clientActive", getClientActive())
                .toString();
    }
}
