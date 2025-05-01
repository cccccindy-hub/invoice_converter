package com.nnroad.datacenter.domain;

import com.nnroad.common.annotation.Excel;

import java.util.List;

/**
 * 快速导入 dc_table_data_pull_config
 */
public class DCTableDataPullConfig extends DCTableConfig {
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 业务表id */
    @Excel(name = "业务表id")
    private Long tableId;

    /** 配置状态 0未使用 1使用中 */
    @Excel(name = "配置状态 0未使用 1使用中")
    private Long status;
    private String tableName;
    private String tableEnName;
    private String tableDbName;
    private Long dataSourceId;
    private String dataSourceName;
    private List<DCTableDataPullReportConfig> reportConfigList;
    private List<DCTableDataPullReportConfigDetail> configDetailList;
    /** 权限组 */
    private String groupIds;

    /** 查看权限组 */
    private String readIds;

    /** 操作权限组 */
    private String operationIds;

    private String userId;

    public List<DCTableDataPullReportConfig> getReportConfigList() {
        return reportConfigList;
    }

    public void setReportConfigList(List<DCTableDataPullReportConfig> reportConfigList) {
        this.reportConfigList = reportConfigList;
    }

    public List<DCTableDataPullReportConfigDetail> getConfigDetailList() {
        return configDetailList;
    }

    public void setConfigDetailList(List<DCTableDataPullReportConfigDetail> configDetailList) {
        this.configDetailList = configDetailList;
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
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableEnName() {
        return tableEnName;
    }

    public void setTableEnName(String tableEnName) {
        this.tableEnName = tableEnName;
    }

    public String getTableDbName() {
        return tableDbName;
    }

    public void setTableDbName(String tableDbName) {
        this.tableDbName = tableDbName;
    }

    public Long getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public String getReadIds() {
        return readIds;
    }

    public void setReadIds(String readIds) {
        this.readIds = readIds;
    }

    public String getOperationIds() {
        return operationIds;
    }

    public void setOperationIds(String operationIds) {
        this.operationIds = operationIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "DCTableDataPullConfig{" +
                "id=" + id +
                ", tableId=" + tableId +
                ", status=" + status +
                ", tableName='" + tableName + '\'' +
                ", tableEnName='" + tableEnName + '\'' +
                ", tableDbName='" + tableDbName + '\'' +
                ", dataSourceId=" + dataSourceId +
                ", dataSourceName='" + dataSourceName + '\'' +
                ", groupIds='" + groupIds + '\'' +
                ", readIds='" + readIds + '\'' +
                ", operationIds='" + operationIds + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
