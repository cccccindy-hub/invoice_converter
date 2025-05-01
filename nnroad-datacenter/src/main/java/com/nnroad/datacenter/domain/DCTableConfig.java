package com.nnroad.datacenter.domain;


import cn.hutool.core.util.StrUtil;
import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.TreeEntity;
import com.nnroad.common.core.domain.entity.SysDictData;
import com.nnroad.common.enums.NumEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 表字段配置对象 dc_table_config
 */
public class DCTableConfig extends TreeEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long columnId;
    private Long defultId;


    /**
     * 表id
     */
    @Excel(name = "表id")
    private Long tableId;

    /**
     * 上级菜单名
     */
    @Excel(name = "上级菜单名")
    private Long columnParentDbname;

    /**
     * 类别 （0.基本项目  1.表头项目）
     */
    @Excel(name = "类别 ", readConverterExp = "0=.基本项目,1=.表头项目")
    private Integer columnType;

    /**
     * 显示名称
     */
    @Excel(name = "显示名称")
    private String columnName;

    /**
     * 显示英文名称
     */
    @Excel(name = "显示英文名称")
    private String columnEnName;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Long columnSort;

    /**
     * 数据库字段名称
     */
    @Excel(name = "数据库字段名称")
    private String columnDbname;

    /**
     * 数据库数据类型（1.数字  0.字符 2.日期）
     */
    @Excel(name = "数据库数据类型", readConverterExp = "1=.数字,0=.字符,2=.日期")
    private Integer columnDbtype;

    /**
     * 数据库长度
     */
    @Excel(name = "数据库长度")
    private Integer columnDblength;

    /**
     * 录入方式（0.输入 1 选择）
     */
    @Excel(name = "录入方式", readConverterExp = "0=.输入,1=,选=择")
    private Integer columnInputMethod;

    /**
     * 数据默认值
     */
    @Excel(name = "数据默认值")
    private String columnDefault;

    /**
     * 数据格式化
     */
    @Excel(name = "数据格式化")
    private String columnFormate;

    /**
     * 字典表id
     */
    @Excel(name = "字典表id")
    private String columnDictid;
    /**
     * 字典表选择字段（0。 dict_label 1.dict_value）
     */
    @Excel(name = "字典表选择字段", readConverterExp = "0=。,d=ict_label,1=.dict_value")
    private Long columnDictchosetype;

    private List<SysDictData> dictList;
    /**
     * 数据公式
     */
    @Excel(name = "数据公式")
    private String columnFormula;

    /**
     * 是否必填 （0 否 1.是）
     */
    @Excel(name = "是否必填 ", readConverterExp = "0=,否=,1=.是")
    private Integer columnIsrequired;

    /**
     * 是否有索引 （0 否 1.是）
     */
    @Excel(name = "是否有索引 ", readConverterExp = "0=,否=,1=.是")
    private Integer columnIsindex;

    /**
     * 是否查询条件（0 否 1.是）
     */
    @Excel(name = "是否查询条件", readConverterExp = "0=,否=,1=.是")
    private Integer columnIsquery;

    /**
     * 查询方式（0 否 1.是）
     */
    @Excel(name = "查询方式", readConverterExp = "0=,单选=,1=.多选")
    private Integer columnChooseType;

    /**
     * 查询状态（0.= 1.> 2.< 3.between）
     */
    @Excel(name = "查询状态", readConverterExp = "0=.=,1=.>,2=.<,3=.between")
    private Integer columnQueryaction;

    private String parentDbName;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    private List<DCTableConfig> childList;
    private Integer columnCount;
    private Integer columnLevel;
    private Integer maxLevel;
    /**
     * 是否判断唯一标志（0 否 1.是）
     */
    @Excel(name = "是否判断唯一标志", readConverterExp = "0=,否=,1=.是")
    private Integer columnIsonly;

    @Excel(name = "Hide Column From Template", readConverterExp = "0=,否=,1=.是")
    private Integer columnHideFromTempl;

    public Long getDefultId() {
        return defultId;
    }

    public void setDefultId(Long defultId) {
        this.defultId = defultId;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getColumnEnName() {
        return columnEnName;
    }

    public void setColumnEnName(String columnEnName) {
        this.columnEnName = columnEnName;
    }

    public Integer getColumnIsonly() {
        return columnIsonly;
    }

    public void setColumnIsonly(Integer columnIsonly) {
        this.columnIsonly = columnIsonly;
    }

    public String getParentDbName() {
        return parentDbName;
    }

    public void setParentDbName(String parentDbName) {
        this.parentDbName = parentDbName;
    }

    public Integer getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }

    public List<DCTableConfig> getChildList() {
        return childList;
    }

    public void setChildList(List<DCTableConfig> childList) {
        this.childList = childList;
    }

    public Integer getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(Integer columnCount) {
        this.columnCount = columnCount;
    }

    public Integer getColumnLevel() {
        return columnLevel;
    }

    public void setColumnLevel(Integer columnLevel) {
        this.columnLevel = columnLevel;
    }

    public Integer getColumnQueryaction() {
        return columnQueryaction;
    }

    public void setColumnQueryaction(Integer columnQueryaction) {
        this.columnQueryaction = columnQueryaction;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public List<SysDictData> getDictList() {
        return dictList;
    }

    public void setDictList(List<SysDictData> dictList) {
        this.dictList = dictList;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setColumnParentDbname(Long columnParentDbname) {
        this.columnParentDbname = columnParentDbname;
    }

    public Long getColumnParentDbname() {
        return columnParentDbname;
    }

    public Integer getColumnType() {
        return columnType;
    }

    public void setColumnType(Integer columnType) {
        this.columnType = columnType;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnSort(Long columnSort) {
        this.columnSort = columnSort;
    }

    public Long getColumnSort() {
        return columnSort;
    }

    public void setColumnDbname(String columnDbname) {
        this.columnDbname = columnDbname;
    }

    public String getColumnDbname() {
        return columnDbname;
    }

    public void setColumnDbtype(Integer columnDbtype) {
        this.columnDbtype = columnDbtype;
    }

    public Integer getColumnDbtype() {
        return columnDbtype;
    }

    public void setColumnDblength(Integer columnDblength) {
        this.columnDblength = columnDblength;
    }

    public Integer getColumnDblength() {
        return columnDblength;
    }

    public void setColumnInputMethod(Integer columnInputMethod) {
        this.columnInputMethod = columnInputMethod;
    }

    public Integer getColumnInputMethod() {
        return columnInputMethod;
    }

    public void setColumnFormate(String columnFormate) {
        this.columnFormate = columnFormate;
    }

    public String getColumnFormate() {
        return columnFormate;
    }

    public void setColumnDictid(String columnDictid) {
        this.columnDictid = columnDictid;
    }

    public String getColumnDictid() {
        return columnDictid;
    }

    public void setColumnDictchosetype(Long columnDictchosetype) {
        this.columnDictchosetype = columnDictchosetype;
    }

    public Long getColumnDictchosetype() {
        return columnDictchosetype;
    }

    public void setColumnFormula(String columnFormula) {
        this.columnFormula = columnFormula;
    }

    public String getColumnFormula() {
        return columnFormula;
    }

    public void setColumnIsrequired(Integer columnIsrequired) {
        this.columnIsrequired = columnIsrequired;
    }

    public Integer getColumnIsrequired() {
        return columnIsrequired;
    }

    public void setColumnIsindex(Integer columnIsindex) {
        this.columnIsindex = columnIsindex;
    }

    public Integer getColumnIsindex() {
        return columnIsindex;
    }

    public void setColumnIsquery(Integer columnIsquery) {
        this.columnIsquery = columnIsquery;
    }

    public Integer getColumnIsquery() {
        return columnIsquery;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public Integer getColumnChooseType() {
        return columnChooseType;
    }

    public void setColumnChooseType(Integer columnChooseType) {
        this.columnChooseType = columnChooseType;
    }

    public Integer getColumnHideFromTempl() {
        return columnHideFromTempl;
    }

    public void setColumnHideFromTempl(Integer columnHideFromTempl) {
        this.columnHideFromTempl = columnHideFromTempl;
    }

    public boolean isDict() {

        return NumEnum.ONE.getNum().equals(this.columnInputMethod) && StrUtil.isNotBlank(this.columnDictid);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("columnId", getColumnId())
                .append("tableId", getTableId())
                .append("columnParentDbname", getColumnParentDbname())
                .append("columnType", getColumnType())
                .append("columnName", getColumnName())
                .append("columnSort", getColumnSort())
                .append("columnDbname", getColumnDbname())
                .append("columnDbtype", getColumnDbtype())
                .append("columnDblength", getColumnDblength())
                .append("columnInputMethod", getColumnInputMethod())
                .append("columnFormate", getColumnFormate())
                .append("columnDictid", getColumnDictid())
                .append("columnDictchosetype", getColumnDictchosetype())
                .append("columnFormula", getColumnFormula())
                .append("columnIsrequired", getColumnIsrequired())
                .append("columnIsindex", getColumnIsindex())
                .append("columnIsquery", getColumnIsquery())
                .append("columnChooseType", getColumnChooseType())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}