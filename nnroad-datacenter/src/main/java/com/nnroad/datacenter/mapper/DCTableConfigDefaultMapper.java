package com.nnroad.datacenter.mapper;

import com.nnroad.datacenter.domain.DCTableConfigDefault;
import org.apache.ibatis.annotations.Param;
import com.nnroad.datacenter.domain.DCTableConfig;

import java.util.List;


/**
 *  表字段配置默认配置Mapper接口
 */
public interface DCTableConfigDefaultMapper
{
    /**
     * 通过表格类型查询默认表头配置
     * 
     * @param tableType 表格类型
     * @param taleId
     * @return List<DCTableConfig>
     */
    List<DCTableConfig> selectTitleByTableType(@Param("tableType") Long tableType, @Param("taleId") Long taleId);
    /**
     * 通过表格类型查询默认表头配置
     *
     * @param tableType 格类型
     * @return List<DcTableConfigDefault>
     */
    List<DCTableConfigDefault> selectByTableType(@Param("tableType")Long tableType);
    /**
     * 通过表格类型查询默认表头配置
     *
     * @param idList 配置id列表

     * @return List<DCTableConfig>
     */
    List<DCTableConfig> selectByIds(@Param("ids") List<String> idList);
}
