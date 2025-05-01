package com.nnroad.datacenter.service;


import com.nnroad.datacenter.domain.DCTable;

import java.util.Map;


/**
 * 数据回滚Service接口
 * 
 * @author Sheng
 * @date 2024-11-26
 */
public interface IDCTableDataOperateLogService
{
    /**
     * 业务表日志变数据插入  类型-新增单条
     * @param map 数据
     * @param tableId 表字段配置ID
     * @return 结果
     */
    void insertByOne(Map<String, String> map, Long tableId,String batchId);
    /**
     * 业务表日志变数据插入  类型-修改单条
     * @param map 数据
     * @param table 配置表信息
     * @return 结果
     */
    void updateByOne(Map<String, String> map, DCTable table, String batchId);

    void deleteById(Long id, Long tableId,String batchId);
}
