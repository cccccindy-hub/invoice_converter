package com.nnroad.employee.mapper;

import java.util.List;
import com.nnroad.employee.domain.EmployeeInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author nnroad
 * @date 2024-11-13
 */
public interface EmployeeInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public EmployeeInfo selectEmployeeInfoById(@Param("id")Long id, @Param("tableName") String tableName);

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
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteEmployeeInfoById(@Param("id")Long id, @Param("tableName") String tableName);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmployeeInfoByIds(@Param("ids") Long[] ids, @Param("tableName") String tableName);

}
