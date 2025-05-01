package com.nnroad.datacenter.domain;



import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;

import java.util.List;

public class DCTableDataPullReportConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;
    /** 业务表id */

    private Long configId;
    /** 业务表id */
    @Excel(name = "业务表id")
    private Long tableId;

    /** 报表id */
    @Excel(name = "报表id")
    private Long reportId;

    /** 配置状态 0未使用 1使用中 */
    @Excel(name = "配置状态 0未使用 1使用中")
    private Long status;
    /** 权限组 */
    private String groupIds;

    /** 查看权限组 */
    private String readIds;

    /** 操作权限组 */
    private String operationIds;

    private String userId;

    private Long dataSourceId;
    private String dataSourceName;
    /** 报表名称 */
    @Excel(name = "报表名称")
    private String reportName;

    /** 表名英文名 */
    @Excel(name = "表名英文名")
    private String reportEnName;
    private String reportDbname;
    private List<DCTableDataPullReportConfigDetail> configDetailList;


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
    public void setReportId(Long reportId)
    {
        this.reportId = reportId;
    }

    public Long getReportId()
    {
        return reportId;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
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

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportEnName() {
        return reportEnName;
    }

    public void setReportEnName(String reportEnName) {
        this.reportEnName = reportEnName;
    }

    public String getReportDbname() {
        return reportDbname;
    }

    public void setReportDbname(String reportDbname) {
        this.reportDbname = reportDbname;
    }

    @Override
    public String toString() {
        return "DCTableDataPullReportConfig{" +
                "id=" + id +
                ", configId=" + configId +
                ", tableId=" + tableId +
                ", reportId=" + reportId +
                ", status=" + status +
                ", groupIds='" + groupIds + '\'' +
                ", readIds='" + readIds + '\'' +
                ", operationIds='" + operationIds + '\'' +
                ", userId='" + userId + '\'' +
                ", dataSourceId=" + dataSourceId +
                ", dataSourceName='" + dataSourceName + '\'' +
                ", reportName='" + reportName + '\'' +
                ", reportEnName='" + reportEnName + '\'' +
                ", reportDbname='" + reportDbname + '\'' +
                '}';
    }
}