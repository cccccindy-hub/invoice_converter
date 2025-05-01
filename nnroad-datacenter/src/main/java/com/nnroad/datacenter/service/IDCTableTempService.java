package com.nnroad.datacenter.service;



import com.nnroad.datacenter.domain.DCTableTemp;

import java.util.List;

public interface IDCTableTempService {
    /**
     * 查询配置表
     *
     * @param tableId 配置表ID
     * @return 配置表
     */
    public int selectDCTableTempById(Long tableId);

    /**
     * 查询配置表列表
     *
     * @param dcTableTemp 配置表
     * @return 配置表集合
     */
    public List<DCTableTemp> selectDCTableTempList(DCTableTemp dcTableTemp);

    /**
     * 新增配置表
     *
     * @param dcTableTemp 配置表
     * @return 结果
     */
    public int insertDCTableTemp(DCTableTemp dcTableTemp);

    /**
     * 修改配置表
     *
     * @param dcTableTemp 配置表
     * @return 结果
     */
    public int updateDCTableTemp(DCTableTemp dcTableTemp);

    /**
     * 批量删除配置表
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDCTableTempByIds(String ids);

    /**
     * 删除配置表信息
     *
     * @param tableId 配置表ID
     * @return 结果
     */
    public int deleteDCTableTempById(Long tableId);
    /**
     * 根据tableId查询配置表查询该表数据 如果没有则新增再返回
     *
     * @param tableId 配置表ID
     * @return 结果
     */
    DCTableTemp selectDCTableTempByTableId(Long tableId);
    /**
     * 还原至使用中配置
     */
    int rollBackTable(Long tableId);
}