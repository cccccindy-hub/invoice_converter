package com.nnroad.datacenter.domain;

import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 【请填写功能名称】对象 dc_table_temp

 */
public class DCTableTemp extends BaseEntity

{
    private static final long serialVersionUID = 1L;
    /** id */
    private Long id;
    /** id */
    private Long tableId;

    /** 表名 */
    @Excel(name = "表名")
    private String tableName;

    @Excel(name = "表名英文名")
    private String tableEnName;

    /** 表数据库名 */
    @Excel(name = "表数据库名")
    private String tableDbName;

    /** 数据库配置生效时间 */
    @Excel(name = "数据库配置生效时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date tableLiveTime;

    /** 备注 */
    @Excel(name = "备注")
    private String tableRemark;

    /** 状态 （0.未同步  1.同步） */
    @Excel(name = "状态 ", readConverterExp = "0=.未同步,1=.同步")
    private Integer tableStatus;

    /** 模块是否同步  0未同步 1.同步 */
    @Excel(name = "模块是否同步  0未同步 1.同步")
    private Integer tableSyn;

    private Long tableType;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private String leaderId;

    private String userId;
    /** 权限组 */
    @Excel(name = "权限组")
    private String groupIds;

    /** 导入开始行 */
    @Excel(name = "导入开始行")
    private Long importStart;

    /** 导入模式 0普通模式 1特殊模式 */
    @Excel(name = "导入模式 0普通模式 1特殊模式")
    private Long importType;

    /** 导入特殊字段 */
    @Excel(name = "导入特殊字段")
    private String importSpecialColumn;

    /** 导入特殊字段 位置 1文件名   2表格特殊位置 */
    @Excel(name = "导入特殊字段 位置 1文件名   2表格特殊位置")
    private Integer importSpecialColumnType;

    /** 表格特殊位置   几行几列 */
    @Excel(name = "表格特殊位置   几行几列")
    private String importSpecialColumnWhere;
    private Integer hasImportSpecialColumn ;
    /** 导入必填字段 */
    @Excel(name = "导入必填字段")
    private String importRequireColumn;
    /** 导入表格名称 */
    @Excel(name = "导入表格名称")
    private String importSheetName;
    /** 表数据是否带权限(0否 1是) */
    @Excel(name = "表数据是否带权限(0否 1是)")
    private Integer tableDataHasAuth;

    /** 业务表所属公司 */
    @Excel(name = "业务表所属公司")
    private Long dataSourceId;
    /** 业务表是否有自动引入(0否 1是) */
    @Excel(name = "业务表是否有自动引入(0否 1是)")
    private Long tableAutomaticEntry;
    /** 业务表自动数据来源 */
    @Excel(name = "业务表自动数据来源")
    private String quickEntryTableId;

    public String getTableEnName() {
        return tableEnName;
    }

    public void setTableEnName(String tableEnName) {
        this.tableEnName = tableEnName;
    }



    public String getQuickEntryTableId() {
        return quickEntryTableId;
    }

    public void setQuickEntryTableId(String quickEntryTableId) {
        this.quickEntryTableId = quickEntryTableId;
    }
    public Long getTableAutomaticEntry() {
        return tableAutomaticEntry;
    }

    public void setTableAutomaticEntry(Long tableAutomaticEntry) {
        this.tableAutomaticEntry = tableAutomaticEntry;
    }

    public Long getDataSourceId() {
        return dataSourceId;
    }
    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public Integer getTableDataHasAuth() {
        return tableDataHasAuth;
    }

    public void setTableDataHasAuth(Integer tableDataHasAuth) {
        this.tableDataHasAuth = tableDataHasAuth;
    }
    public String getImportSheetName() {
        return importSheetName;
    }

    public void setImportSheetName(String importSheetName) {
        this.importSheetName = importSheetName;
    }
    public String getImportRequireColumn() {
        return importRequireColumn;
    }

    public void setImportRequireColumn(String importRequireColumn) {
        this.importRequireColumn = importRequireColumn;
    }
    public Integer getHasImportSpecialColumn() {
        return hasImportSpecialColumn;
    }

    public void setHasImportSpecialColumn(Integer hasImportSpecialColumn) {
        this.hasImportSpecialColumn = hasImportSpecialColumn;
    }

    public Long getImportStart() {
        return importStart;
    }

    public void setImportStart(Long importStart) {
        this.importStart = importStart;
    }

    public Long getImportType() {
        return importType;
    }

    public void setImportType(Long importType) {
        this.importType = importType;
    }

    public String getImportSpecialColumn() {
        return importSpecialColumn;
    }

    public void setImportSpecialColumn(String importSpecialColumn) {
        this.importSpecialColumn = importSpecialColumn;
    }

    public Integer getImportSpecialColumnType() {
        return importSpecialColumnType;
    }

    public void setImportSpecialColumnType(Integer importSpecialColumnType) {
        this.importSpecialColumnType = importSpecialColumnType;
    }

    public String getImportSpecialColumnWhere() {
        return importSpecialColumnWhere;
    }

    public void setImportSpecialColumnWhere(String importSpecialColumnWhere) {
        this.importSpecialColumnWhere = importSpecialColumnWhere;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public void setTableDbName(String tableDbName)
    {
        this.tableDbName = tableDbName;
    }

    public String getTableDbName()
    {
        return tableDbName;
    }
    public void setTableLiveTime(Date tableLiveTime)
    {
        this.tableLiveTime = tableLiveTime;
    }

    public Date getTableLiveTime()
    {
        return tableLiveTime;
    }
    public void setTableRemark(String tableRemark)
    {
        this.tableRemark = tableRemark;
    }

    public String getTableRemark()
    {
        return tableRemark;
    }
    public void setTableStatus(Integer tableStatus)
    {
        this.tableStatus = tableStatus;
    }

    public Integer getTableStatus()
    {
        return tableStatus;
    }
    public void setTableSyn(Integer tableSyn)
    {
        this.tableSyn = tableSyn;
    }

    public Integer getTableSyn()
    {
        return tableSyn;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public Long getTableType() {
        return tableType;
    }

    public void setTableType(Long tableType) {
        this.tableType = tableType;
    }


    @Override
    public String toString() {
        return "DCTableTemp{" +
                "id=" + id +
                ", tableId=" + tableId +
                ", tableName='" + tableName + '\'' +
                ", tableEnName='" + tableEnName + '\'' +
                ", tableDbName='" + tableDbName + '\'' +
                ", tableLiveTime=" + tableLiveTime +
                ", tableRemark='" + tableRemark + '\'' +
                ", tableStatus=" + tableStatus +
                ", tableSyn=" + tableSyn +
                ", tableType=" + tableType +
                ", delFlag='" + delFlag + '\'' +
                ", leaderId='" + leaderId + '\'' +
                ", userId='" + userId + '\'' +
                ", groupIds='" + groupIds + '\'' +
                ", importStart=" + importStart +
                ", importType=" + importType +
                ", importSpecialColumn='" + importSpecialColumn + '\'' +
                ", importSpecialColumnType=" + importSpecialColumnType +
                ", importSpecialColumnWhere='" + importSpecialColumnWhere + '\'' +
                ", hasImportSpecialColumn=" + hasImportSpecialColumn +
                ", importRequireColumn='" + importRequireColumn + '\'' +
                ", importSheetName='" + importSheetName + '\'' +
                ", tableDataHasAuth=" + tableDataHasAuth +
                ", dataSourceId=" + dataSourceId +
                ", tableAutomaticEntry=" + tableAutomaticEntry +
                ", quickEntryTableId='" + quickEntryTableId + '\'' +
                '}';
    }
}