package com.nnroad.payroll.mapper;

import java.util.List;
import com.nnroad.payroll.domain.PayrollAttribute;

/**
 * Formula ConfiigMapper接口
 * 
 * @author Sheng
 * @date 2025-02-08
 */
public interface PayrollAttributeMapper 
{
    /**
     * 查询Formula Confiig
     * 
     * @param id Formula Confiig主键
     * @return Formula Confiig
     */
    public PayrollAttribute selectPayrollAttributeById(Long id);

    /**
     * 查询Formula Confiig列表
     * 
     * @param payrollAttribute Formula Confiig
     * @return Formula Confiig集合
     */
    public List<PayrollAttribute> selectPayrollAttributeList(PayrollAttribute payrollAttribute);

    /**
     * 新增Formula Confiig
     * 
     * @param payrollAttribute Formula Confiig
     * @return 结果
     */
    public int insertPayrollAttribute(PayrollAttribute payrollAttribute);

    /**
     * 修改Formula Confiig
     * 
     * @param payrollAttribute Formula Confiig
     * @return 结果
     */
    public int updatePayrollAttribute(PayrollAttribute payrollAttribute);

    /**
     * 删除Formula Confiig
     * 
     * @param id Formula Confiig主键
     * @return 结果
     */
    public int deletePayrollAttributeById(Long id);

    /**
     * 批量删除Formula Confiig
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePayrollAttributeByIds(Long[] ids);
}
