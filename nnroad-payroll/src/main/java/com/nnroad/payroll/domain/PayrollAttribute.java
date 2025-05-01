package com.nnroad.payroll.domain;

import com.nnroad.datacenter.domain.DCTableConfigTemp;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;

import java.util.List;

/**
 * Formula Confiig对象 payroll_attribute
 * 
 * @author Sheng
 * @date 2025-02-08
 */
public class PayrollAttribute extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 配置表ID */
    @Excel(name = "配置表ID")
    private Long otherTableId;

    /** 父ID */
    @Excel(name = "父ID")
    private Long fieldParentId;

    /** 字段名 */
    @Excel(name = "字段名")
    private String fieldName;

    /** 字段公式 */
    @Excel(name = "字段公式")
    private String fieldFormula;

    /** 排序 */
    @Excel(name = "排序")
    private Long sortOrder;

    /** 基本表ID */
    @Excel(name = "基本表ID")
    private Long baseTableId;

    /** 是否是表头 */
    @Excel(name = "是否是表头")
    private Integer header;

    /** 搜索 */
    @Excel(name = "搜索")
    private Integer searchable;

    /** 下级目录 */
    private List<PayrollAttribute> childList;


    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOtherTableId(Long otherTableId) 
    {
        this.otherTableId = otherTableId;
    }

    public Long getOtherTableId() 
    {
        return otherTableId;
    }
    public void setFieldParentId(Long fieldParentId) 
    {
        this.fieldParentId = fieldParentId;
    }

    public Long getFieldParentId() 
    {
        return fieldParentId;
    }
    public void setFieldName(String fieldName) 
    {
        this.fieldName = fieldName;
    }

    public String getFieldName() 
    {
        return fieldName;
    }
    public void setFieldFormula(String fieldFormula) 
    {
        this.fieldFormula = fieldFormula;
    }

    public String getFieldFormula() 
    {
        return fieldFormula;
    }
    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
    }
    public void setSearchable(Integer searchable) 
    {
        this.searchable = searchable;
    }

    public Integer getSearchable() 
    {
        return searchable;
    }
    public void setBaseTableId(Long baseTableId) 
    {
        this.baseTableId = baseTableId;
    }

    public Long getBaseTableId() 
    {
        return baseTableId;
    }

    public Integer getHeader() {
        return header;
    }

    public void setHeader(Integer header) {
        this.header = header;
    }

    public List<PayrollAttribute> getChildList() {
        return childList;
    }

    public void setChildList(List<PayrollAttribute> childList) {
        this.childList = childList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("otherTableId", getOtherTableId())
            .append("baseTableId", getBaseTableId())
            .append("fieldParentId", getFieldParentId())
            .append("fieldName", getFieldName())
            .append("fieldFormula", getFieldFormula())
            .append("sortOrder", getSortOrder())
            .append("header", getHeader())
            .append("searchable", getSearchable())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
