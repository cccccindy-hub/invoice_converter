package com.nnroad.employee.service;

import java.util.List;
import com.nnroad.employee.domain.EmployeeInfo;

/**
 * 【请填写功能名称】Service接口
 *
 * @author nnroad
 * @date 2024-11-13
 */
public interface EmployeeInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public EmployeeInfo selectEmployeeInfoById(Long id, String tableName);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param employeeInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<EmployeeInfo> selectEmployeeInfoList(EmployeeInfo employeeInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param employeeInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertEmployeeInfo(EmployeeInfo employeeInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param employeeInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateEmployeeInfo(EmployeeInfo employeeInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteEmployeeInfoByIds(Long[] ids, String tableName);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteEmployeeInfoById(Long id, String tableName);

}
