package com.nnroad.payroll.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;

/**
 * payroll对象 payroll_other_table
 * 
 * @author Sheng
 * @date 2025-02-06
 */
public class PayrollOtherTable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 衍生表ID */
    private Long id;

    /** 基本表ID */
    @Excel(name = "基本表ID")
    private Long tableId;

    /** 基本表名 */
    @Excel(name = "基本表名")
    private String tableName;

    /** 衍生表名 */
    @Excel(name = "衍生表名")
    private String formulaTableName;

    /** 配置数据 */
    @Excel(name = "配置数据")
    private String data;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTableId(Long tableId) 
    {
        this.tableId = tableId;
    }

    public Long getTableId() 
    {
        return tableId;
    }
    public void setTableName(String tableName) 
    {
        this.tableName = tableName;
    }

    public String getTableName() 
    {
        return tableName;
    }
    public void setFormulaTableName(String formulaTableName) 
    {
        this.formulaTableName = formulaTableName;
    }

    public String getFormulaTableName() 
    {
        return formulaTableName;
    }
    public void setData(String data) 
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tableId", getTableId())
            .append("tableName", getTableName())
            .append("formulaTableName", getFormulaTableName())
            .append("data", getData())
            .append("remark", getRemark())
            .toString();
    }
}
