package com.nnroad.extraAttribute.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnroad.extraAttribute.mapper.SysExtraAttributeMapper;
import com.nnroad.extraAttribute.domain.SysExtraAttribute;
import com.nnroad.extraAttribute.service.ISysExtraAttributeService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-16
 */
@Service
public class SysExtraAttributeServiceImpl implements ISysExtraAttributeService 
{
    @Autowired
    private SysExtraAttributeMapper sysExtraAttributeMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public SysExtraAttribute selectSysExtraAttributeById(Long id, String tableName)
    {
        return sysExtraAttributeMapper.selectSysExtraAttributeById(id, tableName);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sysExtraAttribute 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SysExtraAttribute> selectSysExtraAttributeList(SysExtraAttribute sysExtraAttribute)
    {
        return sysExtraAttributeMapper.selectSysExtraAttributeList(sysExtraAttribute);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param sysExtraAttribute 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSysExtraAttribute(SysExtraAttribute sysExtraAttribute)
    {
        return sysExtraAttributeMapper.insertSysExtraAttribute(sysExtraAttribute);
    }
    
    /**
     * 新增【请填写功能名称】
     * 
     * @param sysExtraAttribute 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSysExtraAttributeCustom(String tableName, SysExtraAttribute sysExtraAttribute)
    {
        return sysExtraAttributeMapper.insertSysExtraAttributeCustom(tableName, sysExtraAttribute);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysExtraAttribute 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSysExtraAttribute(SysExtraAttribute sysExtraAttribute)
    {
        return sysExtraAttributeMapper.updateSysExtraAttribute(sysExtraAttribute);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteSysExtraAttributeByIds(Long[] ids, String tableName)
    {
        return sysExtraAttributeMapper.deleteSysExtraAttributeByIds(ids, tableName);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteSysExtraAttributeById(Long id, String tableName)
    {
    	String keyPath = findKeyPath(id, tableName);
    	int count = countNullValuesForKey(keyPath, tableName);
    	//if it has no associated non-null values
    	if (count == 0) {
    		 return sysExtraAttributeMapper.deleteSysExtraAttributeById(id, tableName);
    	}
    	else {
    		throw new IllegalArgumentException("Cannot delete attribute because it has associated values.");
    	}
    	
    }

	@Override
	public List<SysExtraAttribute> selectSysExtraAttributeInHierarchy(String tableName) {
		return sysExtraAttributeMapper.selectSysExtraAttributeInHierarchy(tableName);
	}

	@Override
	public List<SysExtraAttribute> selectParentAttribute(String tableName) {
		return sysExtraAttributeMapper.selectParentAttribute(tableName);
	}
	
    private int countNullValuesForKey(String key, String tableName) {
        return sysExtraAttributeMapper.countNullValuesByKey(key, tableName);
    }
    
    private String findKeyPath(Long key, String tableName) {
    	SysExtraAttribute attribute = sysExtraAttributeMapper.selectSysExtraAttributeById(key, tableName);
    	String path;
    	if (attribute.getParentId() != null) {
    	    path = "\"" + attribute.getParentId() + "\"." + "\"" + attribute.getId() + "\"";
    	} else {
    	    path = "\"" + attribute.getId() + "\""; 
    	}
        return path;
    }

    
}
