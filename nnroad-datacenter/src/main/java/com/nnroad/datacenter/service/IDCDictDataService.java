package com.nnroad.datacenter.service;

import com.nnroad.datacenter.domain.DCDictData;

import java.util.List;
import java.util.Map;

/**
 * 字典 业务层
 * 
 * @author Hrone
 */
public interface IDCDictDataService
{
    /**
     * 根据条件分页查询字典数据
     * 
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    public List<DCDictData> selectDictDataList(DCDictData dictData);

    /**
     * 根据字典类型和字典键值查询字典数据信息
     * 
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String selectDictLabel(String dictType, String dictValue);

    /**
     * 根据字典数据ID查询信息
     * 
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    public DCDictData selectDictDataById(Long dictCode);

    /**
     * 批量删除字典数据
     * 
     * @param ids 需要删除的数据
     * @return 结果
     */
    public int deleteDictDataByIds(String ids);

    /**
     * 新增保存字典数据信息
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int insertDictData(DCDictData dictData);

    Map<String, List<DCDictData>> selectByTypes(List<String> dictTypes);
    /**
     * 修改保存字典数据信息
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int updateDictData(DCDictData dictData);

    public List<DCDictData> selectDictDataByType(String type);
}
