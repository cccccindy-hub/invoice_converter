package com.nnroad.datacenter.mapper;

import com.nnroad.datacenter.domain.DCTableImportLog;

import java.util.List;

/**
 * 工资单信息操作记录Mapper接口
 * 
 * @author Hrone
 * @date 2021-04-09
 */
public interface DCTableImportLogMapper
{
    /**
     * 查询工资单信息操作记录
     * 
     * @param opId 工资单信息操作记录ID
     * @return 工资单信息操作记录
     */
    public DCTableImportLog selectDCTableImportLogById(Long opId);

    /**
     * 查询工资单信息操作记录列表
     * 
     * @param dcTableImportLog 工资单信息操作记录
     * @return 工资单信息操作记录集合
     */
    public List<DCTableImportLog> selectDCTableImportLogList(DCTableImportLog dcTableImportLog);

    /**
     * 新增工资单信息操作记录
     * 
     * @param dcTableImportLog 工资单信息操作记录
     * @return 结果
     */
    public int insertDCTableImportLog(DCTableImportLog dcTableImportLog);

    /**
     * 修改工资单信息操作记录
     * 
     * @param dcTableImportLog 工资单信息操作记录
     * @return 结果
     */
    public int updateDCTableImportLog(DCTableImportLog dcTableImportLog);

    /**
     * 删除工资单信息操作记录
     * 
     * @param opId 工资单信息操作记录ID
     * @return 结果
     */
    public int deleteDCTableImportLogById(Long opId);

    /**
     * 批量删除工资单信息操作记录
     * 
     * @param opIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDCTableImportLogByIds(String[] opIds);

    void updateByBatchID(String batchId);
}
