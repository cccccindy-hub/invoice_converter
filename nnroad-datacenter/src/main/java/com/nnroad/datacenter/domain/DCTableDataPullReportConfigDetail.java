package com.nnroad.datacenter.domain;


import com.nnroad.common.annotation.Excel;

/**
 * 快速导入 dc_table_data_pull_config
 *
 * @author Hrone
 * @date 2021-05-18
 */
public class DCTableDataPullReportConfigDetail extends DCTableConfig {
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 业务表id */
    @Excel(name = "业务表id")
    private Long tableId;
    private Long configId;
    private Long configReportId;
    /** 业务表字段 */
    @Excel(name = "业务表字段")
    private String columnName;

    /** 快速引入字段 */
    @Excel(name = "快速引入字段")
    private String importDataName;

    /** 字段筛选类型（
     0.该字段非筛选字段，
     1.该字段输入筛选，
     2.该字段选择筛选，
     3.该字段日期筛选， */
    private Long columnQueryType;

    /** 匹配字典表字段 */
    @Excel(name = "匹配字典表字段")
    private String columnDict;

    public Long getColumnQueryType() {
        return columnQueryType;
    }

    public void setColumnQueryType(Long columnQueryType) {
        this.columnQueryType = columnQueryType;
    }

    public String getColumnDict() {
        return columnDict;
    }

    public void setColumnDict(String columnDict) {
        this.columnDict = columnDict;
    }

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
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getColumnName()
    {
        return columnName;
    }
    public void setImportDataName(String importDataName)
    {
        this.importDataName = importDataName;
    }

    public String getImportDataName()
    {
        return importDataName;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public Long getConfigReportId() {
        return configReportId;
    }

    public void setConfigReportId(Long configReportId) {
        this.configReportId = configReportId;
    }

    @Override
    public String toString() {
        return "DCTableDataPullReportConfigDetail{" +
                "id=" + id +
                ", tableId=" + tableId +
                ", configId=" + configId +
                ", configReportId=" + configReportId +
                ", columnName='" + columnName + '\'' +
                ", importDataName='" + importDataName + '\'' +
                ", columnQueryType=" + columnQueryType +
                ", columnDict='" + columnDict + '\'' +
                '}';
    }
}
