package com.nnroad.datacenter.domain;

import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 导入跳过关键字对象 dc_table_skip_keywords_config
 *
 * @author nnroad
 * @date 2024-12-05
 */
public class DCTableSkipKeywordsConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 关联字段tableId 如果为空则为默认跳过 */
    @Excel(name = "关联字段tableId 如果为空则为默认跳过")
    private Long tableId;

    /** 跳过关键字 */
    @Excel(name = "跳过关键字")
    private String keyWord;

    /** 关联字段tableType 如果为空则为默认跳过 */
    @Excel(name = "关联字段tableType 如果为空则为默认跳过")
    private Long tableType;

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
    public void setKeyWord(String keyWord)
    {
        this.keyWord = keyWord;
    }

    public String getKeyWord()
    {
        return keyWord;
    }
    public void setTableType(Long tableType)
    {
        this.tableType = tableType;
    }

    public Long getTableType()
    {
        return tableType;
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
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tableId", getTableId())
                .append("keyWord", getKeyWord())
                .append("tableType", getTableType())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
