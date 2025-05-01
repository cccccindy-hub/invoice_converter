package com.nnroad.payroll.service.impl;

import java.util.List;
import com.nnroad.common.utils.DateUtils;
import com.nnroad.utils.ExtraAttributeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnroad.payroll.mapper.PsResultMapper;
import com.nnroad.payroll.domain.PsResult;
import com.nnroad.payroll.service.IPsResultService;

/**
 * 计算结果Service业务层处理
 * 
 * @author sheng
 * @date 2024-11-05
 */
@Service
public class PsResultServiceImpl implements IPsResultService 
{
    @Autowired
    private PsResultMapper psResultMapper;

    @Autowired
    private ExtraAttributeUtils extraAttributeUtils;

    /**
     * 查询计算结果
     * 
     * @param id 计算结果主键
     * @return 计算结果
     */
    @Override
    public PsResult selectPsResultById(Long id)
    {
        return psResultMapper.selectPsResultById(id);
    }

    /**
     * 查询计算结果列表
     * 
     * @param psResult 计算结果
     * @return 计算结果
     */
    @Override
    public List<PsResult> selectPsResultList(PsResult psResult)
    {
        return psResultMapper.selectPsResultList(psResult);
    }

    /**
     * 新增计算结果
     * 
     * @param psResult 计算结果
     * @return 结果
     */
    @Override
    public int insertPsResult(PsResult psResult)
    {

        if (extraAttributeUtils.validateData(psResult.getExtraData(), "ps_result")) {
            psResult.setCreateTime(DateUtils.getNowDate());
            return psResultMapper.insertPsResult(psResult);
        }
        else {
            throw new IllegalArgumentException("Invalid input data for extra attributes");
        }

//        return psResultMapper.insertPsResult(psResult);
    }

    /**
     * 修改计算结果
     * 
     * @param psResult 计算结果
     * @return 结果
     */
    @Override
    public int updatePsResult(PsResult psResult)
    {
        psResult.setUpdateTime(DateUtils.getNowDate());
        return psResultMapper.updatePsResult(psResult);
    }

    /**
     * 批量删除计算结果
     * 
     * @param ids 需要删除的计算结果主键
     * @return 结果
     */
    @Override
    public int deletePsResultByIds(Long[] ids)
    {
        return psResultMapper.deletePsResultByIds(ids);
    }

    /**
     * 删除计算结果信息
     * 
     * @param id 计算结果主键
     * @return 结果
     */
    @Override
    public int deletePsResultById(Long id)
    {
        return psResultMapper.deletePsResultById(id);
    }
}
