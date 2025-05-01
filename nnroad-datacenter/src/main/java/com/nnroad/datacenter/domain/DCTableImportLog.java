package com.nnroad.datacenter.domain;
;
import com.nnroad.common.annotation.Excel;
import com.nnroad.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 工资单信息操作记录对象 dc_table_import_log
 * 
 * @author Hrone
 * @date 2021-04-09
 */
public class DCTableImportLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 操作ID */
    private Long opId;

    /** 角色名称 */
    @Excel(name = "角色名称")
    private String roleName;

    /** 导入文件名 */
    @Excel(name = "导入文件名")
    private String inputFileName;

    /** 操作类型 */
    @Excel(name = "操作类型")
    private String opType;

    /** 操作状态 */
    @Excel(name = "操作状态")
    private String status;

    /** 文件路径 */
    private String file_path;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 权限组 */
    private String groupIds;
    /** batchId */
    private String batchId;
    /** batchCode */
    private String batchCode;
    /** 错误信息 */
    private String errorMsg;
    /** 可回滚数据日志的id */
    private List<Long> ids;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public void setOpId(Long opId)
    {
        this.opId = opId;
    }

    public Long getOpId() 
    {
        return opId;
    }
    public void setRoleName(String roleName) 
    {
        this.roleName = roleName;
    }

    public String getRoleName() 
    {
        return roleName;
    }
    public void setInputFileName(String inputFileName)
    {
        this.inputFileName = inputFileName;
    }

    public String getInputFileName()
    {
        return inputFileName;
    }
    public void setOpType(String opType) 
    {
        this.opType = opType;
    }

    public String getOpType() 
    {
        return opType;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getDelFlag()
    {
        return delFlag;
    }
    public void setGroupIds(String groupIds) 
    {
        this.groupIds = groupIds;
    }

    public String getGroupIds() 
    {
        return groupIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("opId", getOpId())
            .append("roleName", getRoleName())
            .append("inputFileName", getInputFileName())
            .append("opType", getOpType())
            .append("delFlag", getDelFlag())
            .append("status", getStatus())
            .append("file_path", getFile_path())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("groupIds", getGroupIds())
            .toString();
    }
}
