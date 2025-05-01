package com.nnroad.datacenter.domain;


import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;

/**
 * 数据回滚对象 dc_table_result_log
 *
 */
public class DCTableDataOperateLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 关联数据表id */
    @Excel(name = "关联数据表id")
    private Long tableId;

    /** 操作类型（1.新增，2.修改,3.删除） */
    @Excel(name = "操作类型", readConverterExp = "1=.新增，2.修改,3.删除")
    private Integer operateType;

    /** 操作数据表id */
    @Excel(name = "操作数据表id")
    private Long dataId;

    /** 操作前数据 */
    @Excel(name = "操作前数据")
    private String before;

    /** 操作后数据 */
    @Excel(name = "操作后数据")
    private String after;

    /** batchId */
    @Excel(name = "batchId")
    private String batchId;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

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

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public void setBefore(String before)
    {
        this.before = before;
    }

    public String getBefore()
    {
        return before;
    }
    public void setAfter(String after)
    {
        this.after = after;
    }

    public String getAfter()
    {
        return after;
    }
    public void setBatchId(String batchId)
    {
        this.batchId = batchId;
    }

    public String getBatchId()
    {
        return batchId;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return "DcTableDataOperateLog{" +
                "id=" + id +
                ", tableId=" + tableId +
                ", operateType=" + operateType +
                ", dataId=" + dataId +
                ", before='" + before + '\'' +
                ", after='" + after + '\'' +
                ", batchId='" + batchId + '\'' +
                ", delFlag='" + delFlag + '\'' +
                '}';
    }
}
