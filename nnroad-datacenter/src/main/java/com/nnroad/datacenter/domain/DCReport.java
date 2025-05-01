package com.nnroad.datacenter.domain;

import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 配置表对象 dc_report
 * 
 * @author Hrone
 * @date 2021-03-10
 */
public class DCReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long reportId;

    /** 使用范围: 1 通用  2 数据拉取 3薪资报表 */
    private Long used_for;

    /** 报表名称 */
    @Excel(name = "报表名称")
    private String reportName;

    /** 表名英文名 */
    @Excel(name = "表名英文名")
    private String reportEnName;

    /** 数据库配置生效时间 */
    @Excel(name = "数据库配置生效时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date tableLiveTime;

    /** 报表数据库view名称 */
    @Excel(name = "报表数据库view名称")
    private String reportDbname;


    /** 备注 */
    @Excel(name = "备注")
    private String tableRemark;

    /** 状态 （0.未同步  1.同步） */
    @Excel(name = "状态 ", readConverterExp = "0=.未同步,1=.同步")
    private Integer tableStatus;

    /** 表是否已生成（0 未生成 1。已生成） */
    @Excel(name = "表是否已生成", readConverterExp = "0=,未=生成,1=。已生成")
    private Integer tableIsgen;

    /** 模块是否同步  0未同步 1.同步 */
    @Excel(name = "模块是否同步  0未同步 1.同步")
    private Integer tableSyn;

    /** 主表所在数据库 */
    @Excel(name = "主表所在数据库")
    private String mainTableDbname;
    /** 主表表名 */
    @Excel(name = "主表表名")
    private String mainTablename;
    /** 附表所在数据库 */
    @Excel(name = "附表所在数据库")
    private String secondTableDbname;
    /** 附表名称 */
    @Excel(name = "附表名称")
    private String secondaryTablename;

    /** 主附表关联条件   on------ */
    @Excel(name = "主附表关联条件   on------")
    private String onCondition;

    /** 分组条件 groupBy */
    @Excel(name = "分组条件 groupBy")
    private String groupbyCondition;

    /** 附加条件 where */
    @Excel(name = "附加条件 where")
    private String whereCondition;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    /** 权限组 */
    @Excel(name = "权限组")
    private String groupIds;
    /** 0不可见 1可见 */
    @Excel(name = "0不可见 1可见")
    private Integer tableShow;
    /** 业务表所属公司 */
    @Excel(name = "业务表所属公司")
    private Long dataSourceId;
    /** 主次表关联关系 （0,left join 1.right join 2.full out join） */
    @Excel(name = "主次表关联关系 ", readConverterExp = "0=,left,j=oin,1=.right,j=oin,2=.full,o=ut,j=oin")
    private Long tableJoinType;

    /** 查看权限组 */
    private String readIds;

    /** 操作权限组 */
    private String operationIds;

    public String getMainTableDbname() {
        return mainTableDbname;
    }

    public void setMainTableDbname(String mainTableDbname) {
        this.mainTableDbname = mainTableDbname;
    }

    public String getSecondTableDbname() {
        return secondTableDbname;
    }

    public void setSecondTableDbname(String secondTableDbname) {
        this.secondTableDbname = secondTableDbname;
    }

    public Long getDataSourceId() {
        return dataSourceId;
    }

    public Long getTableJoinType() {
        return tableJoinType;
    }

    public void setTableJoinType(Long tableJoinType) {
        this.tableJoinType = tableJoinType;
    }

    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public Integer getTableShow() {
        return tableShow;
    }

    public void setTableShow(Integer tableShow) {
        this.tableShow = tableShow;
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

    public void setReportId(Long reportId)
    {
        this.reportId = reportId;
    }

    public Long getReportId()
    {
        return reportId;
    }

    public Long getUsed_for() {
        return used_for;
    }

    public void setUsed_for(Long used_for) {
        this.used_for = used_for;
    }

    public void setReportName(String reportName)
    {
        this.reportName = reportName;
    }

    public String getReportName()
    {
        return reportName;
    }
    public void setTableLiveTime(Date tableLiveTime)
    {
        this.tableLiveTime = tableLiveTime;
    }

    public Date getTableLiveTime()
    {
        return tableLiveTime;
    }
    public void setReportDbname(String reportDbname)
    {
        this.reportDbname = reportDbname;
    }

    public String getReportDbname()
    {
        return reportDbname;
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
    public void setMainTablename(String mainTablename)
    {
        this.mainTablename = mainTablename;
    }

    public String getMainTablename()
    {
        return mainTablename;
    }
    public void setSecondaryTablename(String secondaryTablename)
    {
        this.secondaryTablename = secondaryTablename;
    }

    public String getSecondaryTablename()
    {
        return secondaryTablename;
    }
    public void setOnCondition(String onCondition)
    {
        this.onCondition = onCondition;
    }

    public String getOnCondition()
    {
        return onCondition;
    }
    public void setGroupbyCondition(String groupbyCondition)
    {
        this.groupbyCondition = groupbyCondition;
    }

    public String getGroupbyCondition()
    {
        return groupbyCondition;
    }
    public void setWhereCondition(String whereCondition)
    {
        this.whereCondition = whereCondition;
    }

    public String getWhereCondition()
    {
        return whereCondition;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("reportId", getReportId())
                .append("used_for", getUsed_for())
                .append("reportName", getReportName())
                .append("tableLiveTime", getTableLiveTime())
                .append("reportDbname", getReportDbname())
                .append("tableRemark", getTableRemark())
                .append("tableStatus", getTableStatus())
                .append("mainTablename", getMainTablename())
                .append("secondaryTablename", getSecondaryTablename())
                .append("onCondition", getOnCondition())
                .append("groupbyCondition", getGroupbyCondition())
                .append("whereCondition", getWhereCondition())
                .append("createBy", getCreateBy())
                .append("updateBy", getUpdateBy())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .append("readIds", getReadIds())
                .append("operationIds", getOperationIds())
                .append("reportEnName", getReportEnName())
                .toString();
    }

    public String getReportEnName() {
        return reportEnName;
    }

    public void setReportEnName(String reportEnName) {
        this.reportEnName = reportEnName;
    }
}