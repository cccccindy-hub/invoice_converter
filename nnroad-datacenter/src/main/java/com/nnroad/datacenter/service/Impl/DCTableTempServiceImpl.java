package com.nnroad.datacenter.service.Impl;


import com.nnroad.common.core.text.Convert;
import com.nnroad.common.utils.DateUtils;
import com.nnroad.datacenter.domain.DCTable;
import com.nnroad.datacenter.domain.DCTableTemp;
import com.nnroad.datacenter.domain.DCTableConfig;
import com.nnroad.datacenter.mapper.DCTableConfigMapper;
import com.nnroad.datacenter.mapper.DCTableConfigTempMapper;
import com.nnroad.datacenter.mapper.DCTableMapper;
import com.nnroad.datacenter.mapper.DCTableTempMapper;
import com.nnroad.datacenter.service.IDCTableTempService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 配置表Service业务层处理
 *
 * @author Hrone
 * @date 2021-03-24
 */
@Service
public class DCTableTempServiceImpl implements IDCTableTempService
{
    @Autowired
    private DCTableTempMapper dcTableTempMapper;
    @Autowired
    private DCTableMapper dcTableMapper;
    @Autowired
    private DCTableConfigTempMapper dcTableConfigTempMapper;
    @Autowired
    private DCTableConfigMapper dcTableConfigMapper;
    /**
     * 查询配置表
     *
     * @param tableId 配置表ID
     * @return 配置表
     */
    @Override
    public int selectDCTableTempById(Long tableId)
    {
        return dcTableTempMapper.selectDCTableTempById(tableId);
    }

    /**
     * 查询配置表列表
     *
     * @param dcTableTemp 配置表
     * @return 配置表
     */
    @Override
    public List<DCTableTemp> selectDCTableTempList(DCTableTemp dcTableTemp)
    {
        return dcTableTempMapper.selectDCTableTempList(dcTableTemp);
    }

    /**
     * 新增配置表
     *
     * @param dcTableTemp 配置表
     * @return 结果
     */
    @Override
    public int insertDCTableTemp(DCTableTemp dcTableTemp)
    {
        dcTableTemp.setCreateTime(DateUtils.getNowDate());
        return dcTableTempMapper.insertDCTableTemp(dcTableTemp);
    }

    /**
     * 修改配置表
     *
     * @param dcTableTemp 配置表
     * @return 结果
     */
    @Override
    public int updateDCTableTemp(DCTableTemp dcTableTemp)
    {
        Long tableId = dcTableTemp.getTableId();
        DCTable dcTable = new DCTable();
        dcTable.setTableId(tableId);
        dcTable.setTableSyn(0);
        dcTableMapper.updateDCTable(dcTable);
        dcTableTemp.setUpdateTime(DateUtils.getNowDate());
        return dcTableTempMapper.updateDCTableTemp(dcTableTemp);
    }

    /**
     * 删除配置表对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDCTableTempByIds(String ids)
    {
        return dcTableTempMapper.deleteDCTableTempByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除配置表信息
     *
     * @param tableId 配置表ID
     * @return 结果
     */
    @Override
    public int deleteDCTableTempById(Long tableId)
    {
        return dcTableTempMapper.deleteDCTableTempById(tableId);
    }

    /**
     * 根据tableId查询配置表查询该表数据 如果没有则新增再返回
     *
     * @param tableId 配置表ID
     * @return 结果
     */
    @Override
    public DCTableTemp selectDCTableTempByTableId(Long tableId) {
        DCTableTemp dcTableTemp = dcTableTempMapper.selectDCTableTempByTableId(tableId);
        if (dcTableTemp !=null){
            return dcTableTemp;
        }else {
            DCTable dcTable = dcTableMapper.selectDcTableByTableId(tableId);
            if (dcTable !=null){
                DCTableTemp dcTableTemp1 = new DCTableTemp();
                BeanUtils.copyProperties(dcTable, dcTableTemp1);
                int i = dcTableTempMapper.insertDCTableTemp(dcTableTemp1);
                return dcTableTemp1;
            }
        }
        return null;
    }
    /**
     * 还原至使用中配置
     */
    @Override
    public int rollBackTable(Long tableId) {
        //删除临时表中数据
        int i = dcTableTempMapper.deleteDCTableTempById(tableId);
        int j = dcTableConfigTempMapper.deleteDCTableConfigTempByTableID(tableId);
        List<DCTableConfig> list = dcTableConfigMapper.selectByTableId(tableId);
        if (list != null && !list.isEmpty()) {
            dcTableConfigTempMapper.insertBatchWithID(list);
        }
        DCTable dcTable = new DCTable();
        dcTable.setTableSyn(1);
        dcTable.setTableId(tableId);
        int i1 = dcTableMapper.updateDCTable(dcTable);
        return i+j+i1;
    }
}