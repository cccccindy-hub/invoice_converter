package com.nnroad.datacenter.mapper;


import com.nnroad.datacenter.domain.DCTableTemp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DCTableTempMapper {
    /**
     * 查询配置表
     *
     * @param id 配置表ID
     * @return 配置表
     */
    public int selectDCTableTempById(Long id);

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
     * 删除配置表
     *
     * @param id 配置表ID
     * @return 结果
     */
    public int deleteDCTableTempById(Long id);

    /**
     * 批量删除配置表
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDCTableTempByIds(String[] ids);

    /**
     *根据tableId查询临时表
     *
     * @param tableId 需要删除的数据tableId
     * @return 结果
     */

    DCTableTemp selectDCTableTempByTableId(@Param("tableId") Long tableId);
    /**
     *根据tableId删除临时表中信息
     *
     * @param tableId 需要删除的数据tableId
     * @return 结果
     */
    int deleteDCTableTempByTableId(@Param("tableId")Long tableId);
}