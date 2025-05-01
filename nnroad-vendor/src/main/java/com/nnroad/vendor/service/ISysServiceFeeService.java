package com.nnroad.vendor.service;
import com.nnroad.vendor.domain.SysServiceFee;
import java.util.List;


/**
 * 【请填写功能名称】Service接口
 * 
 * @author nnroad
 * @date 2024-10-24
 */
public interface ISysServiceFeeService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public SysServiceFee selectSysServiceFeeById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sysServiceFee 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SysServiceFee> selectSysServiceFeeList(SysServiceFee sysServiceFee);

    /**
     * 新增【请填写功能名称】
     * 
     * @param sysServiceFee 【请填写功能名称】
     * @return 结果
     */
    public int insertSysServiceFee(SysServiceFee sysServiceFee);

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysServiceFee 【请填写功能名称】
     * @return 结果
     */
    public int updateSysServiceFee(SysServiceFee sysServiceFee);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteSysServiceFeeByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteSysServiceFeeById(Long id);
}
