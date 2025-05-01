package com.nnroad.datacenter.service;

import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.datacenter.domain.DCTableImportLog;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * 工资单信息操作记录Service接口
 * 
 * @author Hrone
 * @date 2021-04-09
 */
public interface IDCTableImportLogService
{
    /**
     * 查询信息操作记录
     * 
     * @param opId 信息操作记录ID
     * @return 信息操作记录
     */
    public DCTableImportLog selectDCTableImportLogById(Long opId);

    /**
     * 查询信息操作记录列表
     * 
     * @param dcTableImportLog 信息操作记录
     * @return 信息操作记录集合
     */
    public List<DCTableImportLog> selectDCTableImportLogList(DCTableImportLog dcTableImportLog);

}
