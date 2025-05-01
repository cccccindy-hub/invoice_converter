package com.nnroad.datacenter.mapper;

import com.nnroad.common.annotation.DataSource;
import com.nnroad.common.enums.DataSourceType;
import com.nnroad.datacenter.domain.DCTableSkipKeywordsConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 导入跳过关键字Mapper接口
 *
 * @author nnroad
 * @date 2024-12-05
 */
public interface DCTableSkipKeywordsConfigMapper
{
    /**
     * 查询导入跳过关键字
     *
     * @param id 导入跳过关键字ID
     * @return 导入跳过关键字
     */
    public DCTableSkipKeywordsConfig selectDCTableSkipKeywordsConfigById(Long id);

    /**
     * 查询导入跳过关键字列表
     *
     * @param dCTableSkipKeywordsConfig 导入跳过关键字
     * @return 导入跳过关键字集合
     */
    public List<DCTableSkipKeywordsConfig> selectDCTableSkipKeywordsConfigList(DCTableSkipKeywordsConfig dCTableSkipKeywordsConfig);

    /**
     * 新增导入跳过关键字
     *
     * @param dCTableSkipKeywordsConfig 导入跳过关键字
     * @return 结果
     */
    public int insertDCTableSkipKeywordsConfig(DCTableSkipKeywordsConfig dCTableSkipKeywordsConfig);

    /**
     * 修改导入跳过关键字
     *
     * @param dCTableSkipKeywordsConfig 导入跳过关键字
     * @return 结果
     */
    public int updateDCTableSkipKeywordsConfig(DCTableSkipKeywordsConfig dCTableSkipKeywordsConfig);

    /**
     * 删除导入跳过关键字
     *
     * @param id 导入跳过关键字ID
     * @return 结果
     */
    public int deleteDCTableSkipKeywordsConfigById(Long id);

    /**
     * 批量删除导入跳过关键字
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDCTableSkipKeywordsConfigByIds(String[] ids);

    List<String> selectSkipKeyWordsByTableId(@Param("tableId") Long tableId,@Param("tableType") Long tableType);
}
