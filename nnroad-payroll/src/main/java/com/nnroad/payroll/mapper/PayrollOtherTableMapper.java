package com.nnroad.payroll.mapper;

import java.util.List;
import java.util.Map;

import com.nnroad.payroll.domain.PayrollOtherTable;
import org.apache.ibatis.annotations.Param;

/**
 * payrollMapper接口
 * 
 * @author Sheng
 * @date 2025-02-06
 */
public interface PayrollOtherTableMapper 
{
    /**
     * 查询payroll
     * 
     * @param id payroll主键
     * @return payroll
     */
    public PayrollOtherTable selectPayrollOtherTableById(Long id);

    /**
     * 查询payroll列表
     * 
     * @param payrollOtherTable payroll
     * @return payroll集合
     */
    public List<PayrollOtherTable> selectPayrollOtherTableList(PayrollOtherTable payrollOtherTable);

    /**
     * 新增payroll
     * 
     * @param payrollOtherTable payroll
     * @return 结果
     */
    public int insertPayrollOtherTable(PayrollOtherTable payrollOtherTable);

    /**
     * 修改payroll
     * 
     * @param payrollOtherTable payroll
     * @return 结果
     */
    public int updatePayrollOtherTable(PayrollOtherTable payrollOtherTable);

    /**
     * 删除payroll
     * 
     * @param id payroll主键
     * @return 结果
     */
    public int deletePayrollOtherTableById(Long id);

    /**
     * 批量删除payroll
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePayrollOtherTableByIds(Long[] ids);

    List<Map<String, Object>> selectBySql(@Param("sql") StringBuilder sql);

    Long getDataCount(@Param("sql") String countSql);
}
