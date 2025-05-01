package com.nnroad.datacenter.mapper;

import com.nnroad.datacenter.domain.DCTableConfig;
import com.nnroad.datacenter.domain.DCTableConfigTemp;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 表字段配置Mapper接口
 */

public interface DCTableConfigMapper
{
    /**
     * 查询表字段配置
     * 
     * @param columnId 表字段配置ID
     * @return 表字段配置
     */
    public DCTableConfig selectDCTableConfigById(Long columnId);

    /**
     * 查询表字段配置列表
     * 
     * @param dcTableConfig 表字段配置
     * @return 表字段配置集合
     */
    public List<DCTableConfig> selectDCTableConfigList(DCTableConfig dcTableConfig);

    /**
     * 新增表字段配置
     * 
     * @param dcTableConfig 表字段配置
     * @return 结果
     */
    public int insertDCTableConfig(DCTableConfig dcTableConfig);

    /**
     * 修改表字段配置
     * 
     * @param dcTableConfig 表字段配置
     * @return 结果
     */
    public int updateDCTableConfig(DCTableConfig dcTableConfig);

    /**
     * 删除表字段配置
     * 
     * @param columnId 表字段配置ID
     * @return 结果
     */
    public int deleteDCTableConfigById(Long columnId);

    /**
     * 批量删除表字段配置
     * 
     * @param columnIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDCTableConfigByIds(String[] columnIds);
    /**
     * 通过tableId查询所有配置
     *
     * @param tableId 需要删除的数据ID
     * @return 结果
     */
    List<DCTableConfig> selectByTableId(Long tableId);
    /**
     * 查询该对象是否有子节点
     *
     * @param columnId 需要删除的数据ID
     * @return 结果
     */
    int selectCountByParentId(Long columnId);
    /**
     * 查询是否在该数据
     *
     * @param dcTableConfig 需要删除的数据ID
     * @return 结果
     */
    DCTableConfig selectParamIsExist(DCTableConfig dcTableConfig);

    List<DCTableConfig> selectTableColumnsByTableId(Long tableId);

    List<DCTableConfig> selectAllResultByTableId(Long tableId);


    void insertBatch(@Param("list")List<DCTableConfigTemp> list);
    void insertBatchWithId(@Param("list")List<DCTableConfigTemp> list);
    int deleteDCTableConfigBytableId(Long tableId);


}
