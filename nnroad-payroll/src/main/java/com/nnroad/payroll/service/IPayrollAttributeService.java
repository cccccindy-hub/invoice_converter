package com.nnroad.payroll.service;

import java.util.List;
import com.nnroad.payroll.domain.PayrollAttribute;

/**
 * Formula ConfiigService接口
 * 
 * @author Sheng
 * @date 2025-02-08
 */
public interface IPayrollAttributeService 
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
     * 批量删除Formula Confiig
     * 
     * @param ids 需要删除的Formula Confiig主键集合
     * @return 结果
     */
    public int deletePayrollAttributeByIds(Long[] ids);

    /**
     * 删除Formula Confiig信息
     * 
     * @param id Formula Confiig主键
     * @return 结果
     */
    public int deletePayrollAttributeById(Long id);

    public List<PayrollAttribute> buildTree(List<PayrollAttribute> payrollAttributes);
}
