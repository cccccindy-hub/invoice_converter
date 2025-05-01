package com.nnroad.vendor.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 sys_service_fee
 * 
 * @author nnroad
 * @date 2024-10-29
 */
public class SysServiceFee extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long clientId;

    private String clientName;
    /**  */
    @Excel(name = "")
    private Long vendorId;
    
    private String vendorName;
    /**  */
    @Excel(name = "")
    private String currency;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String oneTimeSetupType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private BigDecimal oneTimeSetupValue;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String oneTimeSetupUnit;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String recurringManagementType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private BigDecimal recurringManagementValue;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String recurringManagementUnit;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String minimumChargeType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private BigDecimal minimumChargeValue;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String minimumChargeUnit;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String depositType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private BigDecimal depositValue;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String depositUnit;

    /**  */
    @Excel(name = "")
    private String serviceType;

    /**  */
    @Excel(name = "")
    private String other;
    
    private Object extraData;

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
    /**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * @return the vendorName
	 */
	public String getVendorName() {
		return vendorName;
	}

	/**
	 * @param vendorName the vendorName to set
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public void setVendorId(Long vendorId) 
    {
        this.vendorId = vendorId;
    }

    public Long getVendorId() 
    {
        return vendorId;
    }
    public void setCurrency(String currency) 
    {
        this.currency = currency;
    }

    public String getCurrency() 
    {
        return currency;
    }
    public void setOneTimeSetupType(String oneTimeSetupType) 
    {
        this.oneTimeSetupType = oneTimeSetupType;
    }

    public String getOneTimeSetupType() 
    {
        return oneTimeSetupType;
    }
    public void setOneTimeSetupValue(BigDecimal oneTimeSetupValue) 
    {
        this.oneTimeSetupValue = oneTimeSetupValue;
    }

    public BigDecimal getOneTimeSetupValue() 
    {
        return oneTimeSetupValue;
    }
    public void setOneTimeSetupUnit(String oneTimeSetupUnit) 
    {
        this.oneTimeSetupUnit = oneTimeSetupUnit;
    }

    public String getOneTimeSetupUnit() 
    {
        return oneTimeSetupUnit;
    }
    public void setRecurringManagementType(String recurringManagementType) 
    {
        this.recurringManagementType = recurringManagementType;
    }

    public String getRecurringManagementType() 
    {
        return recurringManagementType;
    }
    public void setRecurringManagementValue(BigDecimal recurringManagementValue) 
    {
        this.recurringManagementValue = recurringManagementValue;
    }

    public BigDecimal getRecurringManagementValue() 
    {
        return recurringManagementValue;
    }
    public void setRecurringManagementUnit(String recurringManagementUnit) 
    {
        this.recurringManagementUnit = recurringManagementUnit;
    }

    public String getRecurringManagementUnit() 
    {
        return recurringManagementUnit;
    }
    public void setMinimumChargeType(String minimumChargeType) 
    {
        this.minimumChargeType = minimumChargeType;
    }

    public String getMinimumChargeType() 
    {
        return minimumChargeType;
    }
    public void setMinimumChargeValue(BigDecimal minimumChargeValue) 
    {
        this.minimumChargeValue = minimumChargeValue;
    }

    public BigDecimal getMinimumChargeValue() 
    {
        return minimumChargeValue;
    }
    public void setMinimumChargeUnit(String minimumChargeUnit) 
    {
        this.minimumChargeUnit = minimumChargeUnit;
    }

    public String getMinimumChargeUnit() 
    {
        return minimumChargeUnit;
    }
    public void setDepositType(String depositType) 
    {
        this.depositType = depositType;
    }

    public String getDepositType() 
    {
        return depositType;
    }
    public void setDepositValue(BigDecimal depositValue) 
    {
        this.depositValue = depositValue;
    }

    public BigDecimal getDepositValue() 
    {
        return depositValue;
    }
    public void setDepositUnit(String depositUnit) 
    {
        this.depositUnit = depositUnit;
    }

    public String getDepositUnit() 
    {
        return depositUnit;
    }
    public void setServiceType(String serviceType) 
    {
        this.serviceType = serviceType;
    }

    public String getServiceType() 
    {
        return serviceType;
    }
    public void setOther(String other) 
    {
        this.other = other;
    }

    public String getOther() 
    {
        return other;
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

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("clientId", getClientId())
            .append("vendorId", getVendorId())
            .append("currency", getCurrency())
            .append("oneTimeSetupType", getOneTimeSetupType())
            .append("oneTimeSetupValue", getOneTimeSetupValue())
            .append("oneTimeSetupUnit", getOneTimeSetupUnit())
            .append("recurringManagementType", getRecurringManagementType())
            .append("recurringManagementValue", getRecurringManagementValue())
            .append("recurringManagementUnit", getRecurringManagementUnit())
            .append("minimumChargeType", getMinimumChargeType())
            .append("minimumChargeValue", getMinimumChargeValue())
            .append("minimumChargeUnit", getMinimumChargeUnit())
            .append("depositType", getDepositType())
            .append("depositValue", getDepositValue())
            .append("depositUnit", getDepositUnit())
            .append("serviceType", getServiceType())
            .append("other", getOther())
            .toString();
    }
}
