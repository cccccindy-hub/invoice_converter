package com.nnroad.vendor.service.impl;

import java.util.List;

import com.nnroad.utils.ExtraAttributeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnroad.vendor.mapper.SysServiceFeeMapper;
import com.nnroad.vendor.domain.SysServiceFee;
import com.nnroad.vendor.service.ISysServiceFeeService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author nnroad
 * @date 2024-10-24
 */
@Service
public class SysServiceFeeServiceImpl implements ISysServiceFeeService 
{
    @Autowired
    private SysServiceFeeMapper sysServiceFeeMapper;
    
    @Autowired
    private ExtraAttributeUtils extraAttributeUtils;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public SysServiceFee selectSysServiceFeeById(Long id)
    {
        return sysServiceFeeMapper.selectSysServiceFeeById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sysServiceFee 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SysServiceFee> selectSysServiceFeeList(SysServiceFee sysServiceFee)
    {
        return sysServiceFeeMapper.selectSysServiceFeeList(sysServiceFee);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param sysServiceFee 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSysServiceFee(SysServiceFee sysServiceFee)
    {
    	if (extraAttributeUtils.validateData(sysServiceFee.getExtraData(), "sys_service_fee")) {
    		return sysServiceFeeMapper.insertSysServiceFee(sysServiceFee);
    	}
    	else {
    		throw new IllegalArgumentException("Invalid input data for extra attributes");
    	}
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysServiceFee 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSysServiceFee(SysServiceFee sysServiceFee)
    {
    	if (extraAttributeUtils.validateData(sysServiceFee.getExtraData(), "sys_service_fee")) {
    		return sysServiceFeeMapper.updateSysServiceFee(sysServiceFee);
    	}
    	else {
    		throw new IllegalArgumentException("Invalid input data for extra attributes");
    	}
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteSysServiceFeeByIds(Long[] ids)
    {
        return sysServiceFeeMapper.deleteSysServiceFeeByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteSysServiceFeeById(Long id)
    {
        return sysServiceFeeMapper.deleteSysServiceFeeById(id);
    }
}
