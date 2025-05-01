package com.nnroad.payroll.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;

/**
 * 计算结果对象 ps_result
 * 
 * @author sheng
 * @date 2024-11-05
 */
public class PsResult extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 期间 */
    @Excel(name = "期间")
    private String duration;

    /** 工号 */
    @Excel(name = "工号")
    private String idNo;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    private Object extraData;


    /** 权限组 */
    @Excel(name = "权限组")
    private String groupIds;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDuration(String duration) 
    {
        this.duration = duration;
    }

    public String getDuration() 
    {
        return duration;
    }
    public void setIdNo(String idNo) 
    {
        this.idNo = idNo;
    }

    public String getIdNo() 
    {
        return idNo;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setGroupIds(String groupIds) 
    {
        this.groupIds = groupIds;
    }

    public String getGroupIds() 
    {
        return groupIds;
    }

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("duration", getDuration())
            .append("idNo", getIdNo())
            .append("name", getName())
            .append("extraData", getExtraData())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("groupIds", getGroupIds())
            .toString();
    }
}
