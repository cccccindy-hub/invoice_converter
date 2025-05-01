package com.nnroad.datacenter.service;

import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.datacenter.domain.DCReport;

import java.util.List;
import java.util.Map;

/**
 * 配置表Service接口
 * 
 * @author Hrone
 * @date 2021-03-10
 */
public interface IDCReportService
{
    /**
     * check表名
     *
     * @param tableName 配置表
     * @return 结果
     */
    public Integer checkDCTableNameOrReportNameIsExist(String tableName);



}
