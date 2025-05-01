package com.nnroad.system.domain;

import com.nnroad.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 应用管理对象 sys_app
 * 
 * @author ruoyi
 * @date 2023-05-25
 */
public class SysApp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 应用id */
    private String appId;

    /** 应用名称 */
    private String appName;

    /** appKey */
    private String appKey;

    /** secretKey */
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /** 备注 */
    private String fdesc;

    public void setAppId(String appId) 
    {
        this.appId = appId;
    }

    public String getAppId() 
    {
        return appId;
    }
    public void setAppName(String appName) 
    {
        this.appName = appName;
    }

    public String getAppName() 
    {
        return appName;
    }
    public void setAppKey(String appKey) 
    {
        this.appKey = appKey;
    }

    public String getAppKey() 
    {
        return appKey;
    }
    public void setFdesc(String fdesc) 
    {
        this.fdesc = fdesc;
    }

    public String getFdesc() 
    {
        return fdesc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("appId", getAppId())
            .append("appName", getAppName())
            .append("appKey", getAppKey())
            .append("secretKey", getSecretKey())
            .append("fdesc", getFdesc())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
