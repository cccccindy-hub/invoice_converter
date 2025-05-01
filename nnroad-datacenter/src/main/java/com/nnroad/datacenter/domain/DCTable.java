package com.nnroad.datacenter.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;

/**
 * tableDefintion对象 dc_table
 * 
 * @author Sheng
 * @date 2024-11-11
 */
public class DCTable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long tableId;

    /** 表名 */
    @Excel(name = "表名")
    private String tableName;

    /** 表英文名 */
    @Excel(name = "表英文名")
    private String tableEnName;

    /** 表数据库名 */
    @Excel(name = "表数据库名")
    private String tableDbName;

    /** 业务表所属 */
    @Excel(name = "业务表所属")
    private Long dataSourceId;

    /** 数据库配置生效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "数据库配置生效时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date tableLiveTime;

    /** 备注 */
    @Excel(name = "备注")
    private String tableRemark;

    /** 表类型 */
    @Excel(name = "表类型")
    private Long tableType;

    /** 表是否已生成（0 未生成 1。已生成） */
    @Excel(name = "表是否已生成", readConverterExp = "0=,未=生成,1=。已生成")
    private Integer tableIsgen;

    /** 状态 （0.未同步  1.同步） */
    @Excel(name = "状态 ", readConverterExp = "0=.未同步,1=.同步")
    private Integer tableStatus;

    /** 模块是否同步  0未同步 1.同步 */
    @Excel(name = "模块是否同步  0未同步 1.同步")
    private Integer tableSyn;

    /** 表数据是否带权限(0否 1是) */
    @Excel(name = "表数据是否带权限(0否 1是)")
    private Long tableDataHasAuth;

    /** 业务表是否有自动引入(0否 1是) */
    @Excel(name = "业务表是否有自动引入(0否 1是)")
    private Long tableAutomaticEntry;

    /** 业务表自动数据来源 */
    @Excel(name = "业务表自动数据来源")
    private String quickEntryTableId;

    /** 权限组 */
    @Excel(name = "权限组")
    private String groupIds;

    /** 查看权限组 */
    @Excel(name = "查看权限组")
    private String readIds;

    /** 操作权限组 */
    @Excel(name = "操作权限组")
    private String operationIds;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

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
    private Long importSpecialColumnType;

    /** 表格特殊位置   几行几列 */
    @Excel(name = "表格特殊位置   几行几列")
    private String importSpecialColumnWhere;

    /** 导入必填字段 */
    @Excel(name = "导入必填字段")
    private String importRequireColumn;

    /** 导入表格名称 */
    @Excel(name = "导入表格名称")
    private String importSheetName;

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
    public void setTableEnName(String tableEnName) 
    {
        this.tableEnName = tableEnName;
    }

    public String getTableEnName() 
    {
        return tableEnName;
    }
    public void setTableDbName(String tableDbName) 
    {
        this.tableDbName = tableDbName;
    }

    public String getTableDbName() 
    {
        return tableDbName;
    }
    public void setDataSourceId(Long dataSourceId) 
    {
        this.dataSourceId = dataSourceId;
    }

    public Long getDataSourceId() 
    {
        return dataSourceId;
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
    public void setTableType(Long tableType) 
    {
        this.tableType = tableType;
    }

    public Long getTableType() 
    {
        return tableType;
    }

    public Integer getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(Integer tableStatus) {
        this.tableStatus = tableStatus;
    }

    public Integer getTableIsgen() {
        return tableIsgen;
    }

    public void setTableIsgen(Integer tableIsgen) {
        this.tableIsgen = tableIsgen;
    }

    public Integer getTableSyn() {
        return tableSyn;
    }

    public void setTableSyn(Integer tableSyn) {
        this.tableSyn = tableSyn;
    }

    public void setTableDataHasAuth(Long tableDataHasAuth)
    {
        this.tableDataHasAuth = tableDataHasAuth;
    }

    public Long getTableDataHasAuth() 
    {
        return tableDataHasAuth;
    }
    public void setTableAutomaticEntry(Long tableAutomaticEntry) 
    {
        this.tableAutomaticEntry = tableAutomaticEntry;
    }

    public Long getTableAutomaticEntry() 
    {
        return tableAutomaticEntry;
    }
    public void setQuickEntryTableId(String quickEntryTableId) 
    {
        this.quickEntryTableId = quickEntryTableId;
    }

    public String getQuickEntryTableId() 
    {
        return quickEntryTableId;
    }
    public void setGroupIds(String groupIds) 
    {
        this.groupIds = groupIds;
    }

    public String getGroupIds() 
    {
        return groupIds;
    }
    public void setReadIds(String readIds) 
    {
        this.readIds = readIds;
    }

    public String getReadIds() 
    {
        return readIds;
    }
    public void setOperationIds(String operationIds) 
    {
        this.operationIds = operationIds;
    }

    public String getOperationIds() 
    {
        return operationIds;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    public void setImportStart(Long importStart) 
    {
        this.importStart = importStart;
    }

    public Long getImportStart() 
    {
        return importStart;
    }
    public void setImportType(Long importType) 
    {
        this.importType = importType;
    }

    public Long getImportType() 
    {
        return importType;
    }
    public void setImportSpecialColumn(String importSpecialColumn) 
    {
        this.importSpecialColumn = importSpecialColumn;
    }

    public String getImportSpecialColumn() 
    {
        return importSpecialColumn;
    }
    public void setImportSpecialColumnType(Long importSpecialColumnType) 
    {
        this.importSpecialColumnType = importSpecialColumnType;
    }

    public Long getImportSpecialColumnType() 
    {
        return importSpecialColumnType;
    }
    public void setImportSpecialColumnWhere(String importSpecialColumnWhere) 
    {
        this.importSpecialColumnWhere = importSpecialColumnWhere;
    }

    public String getImportSpecialColumnWhere() 
    {
        return importSpecialColumnWhere;
    }
    public void setImportRequireColumn(String importRequireColumn) 
    {
        this.importRequireColumn = importRequireColumn;
    }

    public String getImportRequireColumn() 
    {
        return importRequireColumn;
    }
    public void setImportSheetName(String importSheetName) 
    {
        this.importSheetName = importSheetName;
    }

    public String getImportSheetName() 
    {
        return importSheetName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("tableId", getTableId())
            .append("tableName", getTableName())
            .append("tableEnName", getTableEnName())
            .append("tableDbName", getTableDbName())
            .append("dataSourceId", getDataSourceId())
            .append("tableLiveTime", getTableLiveTime())
            .append("tableRemark", getTableRemark())
            .append("tableType", getTableType())
            .append("tableIsgen", getTableIsgen())
            .append("tableStatus", getTableStatus())
            .append("tableSyn", getTableSyn())
            .append("tableDataHasAuth", getTableDataHasAuth())
            .append("tableAutomaticEntry", getTableAutomaticEntry())
            .append("quickEntryTableId", getQuickEntryTableId())
            .append("groupIds", getGroupIds())
            .append("readIds", getReadIds())
            .append("operationIds", getOperationIds())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("importStart", getImportStart())
            .append("importType", getImportType())
            .append("importSpecialColumn", getImportSpecialColumn())
            .append("importSpecialColumnType", getImportSpecialColumnType())
            .append("importSpecialColumnWhere", getImportSpecialColumnWhere())
            .append("importRequireColumn", getImportRequireColumn())
            .append("importSheetName", getImportSheetName())
            .toString();
    }
}
