package com.nnroad.datacenter.domain;


import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 dc_table_config_default
 *
 */
public class DCTableConfigDefault extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long defultId;

    /** 表类型 */
    @Excel(name = "表类型")
    private Long tableType;

    /** 上级菜单名 */
    @Excel(name = "上级菜单名")
    private Long columnParentDbname;

    /** 类别 （0.基本项目  1.表头项目） */
    @Excel(name = "类别 ", readConverterExp = "0=.基本项目,1=.表头项目")
    private Long columnType;

    /** 显示名称 */
    @Excel(name = "显示名称")
    private String columnName;
    /** 显示英文名称 */
    @Excel(name = "显示英文名称")
    private String columnEnName;
    /** 排序 */
    @Excel(name = "排序")
    private Long columnSort;

    /** 数据库字段名称 */
    @Excel(name = "数据库字段名称")
    private String columnDbname;

    /** 数据库数据类型（1.数字  0.字符 2.日期） */
    @Excel(name = "数据库数据类型", readConverterExp = "1=.数字,0=.字符,2=.日期")
    private Long columnDbtype;

    /** 数据库长度 */
    @Excel(name = "数据库长度")
    private Long columnDblength;

    /** 录入方式（0.输入 1 选择） */
    @Excel(name = "录入方式", readConverterExp = "0=.输入,1=,选=择")
    private Long columnInputMethod;

    /** 数据格式化 */
    @Excel(name = "数据格式化")
    private String columnFormate;

    /** 字典表id */
    @Excel(name = "字典表id")
    private String columnDictid;

    /** 字典表选择字段（0。 dict_label 1.dict_value） */
    @Excel(name = "字典表选择字段", readConverterExp = "0=。,d=ict_label,1=.dict_value")
    private Long columnDictchosetype;

    /** 数据默认值 */
    @Excel(name = "数据默认值")
    private String columnDefault;

    /** 数据公式 */
    @Excel(name = "数据公式")
    private String columnFormula;

    /** 是否必填 （0 否 1.是） */
    @Excel(name = "是否必填 ", readConverterExp = "0=,否=,1=.是")
    private Long columnIsrequired;

    /** 是否有索引 （0 否 1.是） */
    @Excel(name = "是否有索引 ", readConverterExp = "0=,否=,1=.是")
    private Long columnIsindex;

    /** 是否查询条件（0 否 1.是） */
    @Excel(name = "是否查询条件", readConverterExp = "0=,否=,1=.是")
    private Long columnIsquery;

    /** 查询状态（0.= 1.> 2.< 3.between） */
    @Excel(name = "查询状态", readConverterExp = "0=.=,1=.>,2=.<,3=.between")
    private Long columnQueryaction;

    /** 判断唯一标识  0否 1是 */
    @Excel(name = "判断唯一标识  0否 1是")
    private Long columnIsonly;

    @Excel(name = "Hide Column From Template", readConverterExp = "0=,否=,1=.是")
    private Integer columnHideFromTempl;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

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

    public void setDefultId(Long defultId)
    {
        this.defultId = defultId;
    }

    public Long getDefultId()
    {
        return defultId;
    }
    public void setTableType(Long tableType)
    {
        this.tableType = tableType;
    }

    public Long getTableType()
    {
        return tableType;
    }
    public void setColumnParentDbname(Long columnParentDbname)
    {
        this.columnParentDbname = columnParentDbname;
    }

    public Long getColumnParentDbname()
    {
        return columnParentDbname;
    }
    public void setColumnType(Long columnType)
    {
        this.columnType = columnType;
    }

    public Long getColumnType()
    {
        return columnType;
    }
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getColumnName()
    {
        return columnName;
    }
    public void setColumnSort(Long columnSort)
    {
        this.columnSort = columnSort;
    }

    public Long getColumnSort()
    {
        return columnSort;
    }
    public void setColumnDbname(String columnDbname)
    {
        this.columnDbname = columnDbname;
    }

    public String getColumnDbname()
    {
        return columnDbname;
    }
    public void setColumnDbtype(Long columnDbtype)
    {
        this.columnDbtype = columnDbtype;
    }

    public Long getColumnDbtype()
    {
        return columnDbtype;
    }
    public void setColumnDblength(Long columnDblength)
    {
        this.columnDblength = columnDblength;
    }

    public Long getColumnDblength()
    {
        return columnDblength;
    }
    public void setColumnInputMethod(Long columnInputMethod)
    {
        this.columnInputMethod = columnInputMethod;
    }

    public Long getColumnInputMethod()
    {
        return columnInputMethod;
    }
    public void setColumnFormate(String columnFormate)
    {
        this.columnFormate = columnFormate;
    }

    public String getColumnFormate()
    {
        return columnFormate;
    }
    public void setColumnDictid(String columnDictid)
    {
        this.columnDictid = columnDictid;
    }

    public String getColumnDictid()
    {
        return columnDictid;
    }
    public void setColumnDictchosetype(Long columnDictchosetype)
    {
        this.columnDictchosetype = columnDictchosetype;
    }

    public Long getColumnDictchosetype()
    {
        return columnDictchosetype;
    }
    public void setColumnFormula(String columnFormula)
    {
        this.columnFormula = columnFormula;
    }

    public String getColumnFormula()
    {
        return columnFormula;
    }
    public void setColumnIsrequired(Long columnIsrequired)
    {
        this.columnIsrequired = columnIsrequired;
    }

    public Long getColumnIsrequired()
    {
        return columnIsrequired;
    }
    public void setColumnIsindex(Long columnIsindex)
    {
        this.columnIsindex = columnIsindex;
    }

    public Long getColumnIsindex()
    {
        return columnIsindex;
    }
    public void setColumnIsquery(Long columnIsquery)
    {
        this.columnIsquery = columnIsquery;
    }

    public Long getColumnIsquery()
    {
        return columnIsquery;
    }
    public void setColumnQueryaction(Long columnQueryaction)
    {
        this.columnQueryaction = columnQueryaction;
    }

    public Long getColumnQueryaction()
    {
        return columnQueryaction;
    }
    public void setColumnIsonly(Long columnIsonly)
    {
        this.columnIsonly = columnIsonly;
    }

    public Long getColumnIsonly()
    {
        return columnIsonly;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public Integer getColumnHideFromTempl() {
        return columnHideFromTempl;
    }

    public void setColumnHideFromTempl(Integer columnHideFromTempl) {
        this.columnHideFromTempl = columnHideFromTempl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("defultId", getDefultId())
            .append("tableType", getTableType())
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
            .append("columnQueryaction", getColumnQueryaction())
            .append("columnIsonly", getColumnIsonly())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
