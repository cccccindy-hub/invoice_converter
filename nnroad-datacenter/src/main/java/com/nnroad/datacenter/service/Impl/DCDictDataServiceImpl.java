package com.nnroad.datacenter.service.Impl;

import cn.hutool.core.collection.CollUtil;
import com.nnroad.common.core.text.Convert;
import com.nnroad.common.utils.DictUtils;
import com.nnroad.datacenter.domain.DCDictData;
import com.nnroad.datacenter.service.IDCDictDataService;
import com.nnroad.dict.mapper.DCDictDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典 业务层处理
 *
 * @author Hrone
 */
@Service
public class DCDictDataServiceImpl implements IDCDictDataService {
    @Autowired
    private DCDictDataMapper dictDataMapper;

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<DCDictData> selectDictDataList(DCDictData dictData) {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public DCDictData selectDictDataById(Long dictCode) {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据
     *
     * @param ids 需要删除的数据
     * @return 结果
     */
    @Override
    public int deleteDictDataByIds(String ids) {
        int row = dictDataMapper.deleteDictDataByIds(Convert.toStrArray(ids));
        if (row > 0) {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(DCDictData dictData) {
        int row = dictDataMapper.insertDictData(dictData);
        if (row > 0) {
            DictUtils.clearDictCache();
        }
        return row;
    }

    @Override
    public Map<String, List<DCDictData>> selectByTypes(List<String> dictTypes) {
        if (CollUtil.isEmpty(dictTypes)) {
            return Collections.emptyMap();
        }

        return dictDataMapper.selectByTypes(dictTypes).stream()
                .collect(Collectors.groupingBy(DCDictData::getDictType));
    }

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(DCDictData dictData) {
        int row = dictDataMapper.updateDictData(dictData);
        if (row > 0) {
            DictUtils.clearDictCache();
        }
        return row;
    }

    @Override
    public List<DCDictData> selectDictDataByType(String type) {
        return dictDataMapper.selectDictDataByType(type);
    }

}
