package com.nnroad.datacenter.domain;

import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * dataPull对象 dc_table_data_pull
 */
public class DCTableDataPull extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 表id */
    @Excel(name = "表id")
    private Long tableId;

    /** 表数据库名 */
    @Excel(name = "表数据库名")
    private String tableDbName;

    /** 表名 */
    @Excel(name = "表名")
    private String tableName;

    /** 报表数据库名 */
    @Excel(name = "报表数据库名")
    private String reportDbName;

    /** 报表名 */
    @Excel(name = "报表名")
    private String reportName;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

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
    public void setTableDbName(String tableDbName)
    {
        this.tableDbName = tableDbName;
    }

    public String getTableDbName()
    {
        return tableDbName;
    }
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableName()
    {
        return tableName;
    }
    public void setReportDbName(String reportDbName)
    {
        this.reportDbName = reportDbName;
    }

    public String getReportDbName()
    {
        return reportDbName;
    }
    public void setReportName(String reportName)
    {
        this.reportName = reportName;
    }

    public String getReportName()
    {
        return reportName;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tableId", getTableId())
            .append("tableDbName", getTableDbName())
            .append("tableName", getTableName())
            .append("reportDbName", getReportDbName())
            .append("reportName", getReportName())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
