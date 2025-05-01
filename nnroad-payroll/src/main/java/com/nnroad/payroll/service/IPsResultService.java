package com.nnroad.payroll.service;

import java.util.List;
import com.nnroad.payroll.domain.PsResult;

/**
 * 计算结果Service接口
 * 
 * @author sheng
 * @date 2024-11-05
 */
public interface IPsResultService 
{
    /**
     * 查询计算结果
     * 
     * @param id 计算结果主键
     * @return 计算结果
     */
    public PsResult selectPsResultById(Long id);

    /**
     * 查询计算结果列表
     * 
     * @param psResult 计算结果
     * @return 计算结果集合
     */
    public List<PsResult> selectPsResultList(PsResult psResult);

    /**
     * 新增计算结果
     * 
     * @param psResult 计算结果
     * @return 结果
     */
    public int insertPsResult(PsResult psResult);

    /**
     * 修改计算结果
     * 
     * @param psResult 计算结果
     * @return 结果
     */
    public int updatePsResult(PsResult psResult);

    /**
     * 批量删除计算结果
     * 
     * @param ids 需要删除的计算结果主键集合
     * @return 结果
     */
    public int deletePsResultByIds(Long[] ids);

    /**
     * 删除计算结果信息
     * 
     * @param id 计算结果主键
     * @return 结果
     */
    public int deletePsResultById(Long id);
}
