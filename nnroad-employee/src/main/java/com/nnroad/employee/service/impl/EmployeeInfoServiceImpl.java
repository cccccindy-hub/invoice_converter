package com.nnroad.employee.service.impl;

import java.util.List;

import com.nnroad.employee.mapper.EmployeeInfoMapper;
import com.nnroad.employee.service.EmployeeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnroad.employee.domain.EmployeeInfo;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author nnroad
employeete 2024-11-13
 */
@Service
public class EmployeeInfoServiceImpl implements EmployeeInfoService
{
    @Autowired
    private EmployeeInfoMapper employeeInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public EmployeeInfo selectEmployeeInfoById(Long id, String tableName)
    {
        return employeeInfoMapper.selectEmployeeInfoById(id, tableName);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param employeeInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<EmployeeInfo> selectEmployeeInfoList(EmployeeInfo employeeInfo)
    {
        return employeeInfoMapper.selectEmployeeInfoList(employeeInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param employeeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertEmployeeInfo(EmployeeInfo employeeInfo)
    {
        return employeeInfoMapper.insertEmployeeInfo(employeeInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param employeeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateEmployeeInfo(EmployeeInfo employeeInfo)
    {
        return employeeInfoMapper.updateEmployeeInfo(employeeInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteEmployeeInfoByIds(Long[] ids, String tableName)
    {
        return employeeInfoMapper.deleteEmployeeInfoByIds(ids, tableName);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteEmployeeInfoById(Long id, String tableName)
    {
        return employeeInfoMapper.deleteEmployeeInfoById(id, tableName);
    }

}
