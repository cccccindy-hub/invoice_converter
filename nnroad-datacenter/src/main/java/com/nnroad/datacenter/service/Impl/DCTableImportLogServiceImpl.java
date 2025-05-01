package com.nnroad.datacenter.service.Impl;

import com.nnroad.datacenter.domain.DCTableImportLog;
import com.nnroad.datacenter.mapper.DCTableImportLogMapper;
import com.nnroad.datacenter.mapper.DCTableMapper;
import com.nnroad.datacenter.service.IDCTableImportLogService;
import com.nnroad.datacenter.service.IDCTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 工资单信息操作记录Service业务层处理
 * 
 * @author Hrone
 * @date 2024-12-09
 */
@Service
public class DCTableImportLogServiceImpl implements IDCTableImportLogService
{
    @Autowired
    private DCTableImportLogMapper dcTableImportLogMapper;
    @Autowired
    private DCTableMapper dcTableMapper;
    @Autowired
    private IDCTableService dcTableService;



    /**
     * 查询工资单信息操作记录
     * 
     * @param opId 工资单信息操作记录ID
     * @return 工资单信息操作记录
     */
    @Override
    public DCTableImportLog selectDCTableImportLogById(Long opId)
    {
        return dcTableImportLogMapper.selectDCTableImportLogById(opId);
    }

    /**
     * 查询工资单信息操作记录列表
     * 
     * @param dcTableImportLog 工资单信息操作记录
     * @return 工资单信息操作记录
     */
    @Override
    public List<DCTableImportLog> selectDCTableImportLogList(DCTableImportLog dcTableImportLog)
    {
        return dcTableImportLogMapper.selectDCTableImportLogList(dcTableImportLog);
    }




}
