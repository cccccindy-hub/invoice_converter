package com.nnroad.extraAttribute.service;

import java.util.List;
import com.nnroad.extraAttribute.domain.SysExtraAttribute;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author nnroad
 * @date 2024-10-16
 */
public interface ISysExtraAttributeService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public SysExtraAttribute selectSysExtraAttributeById(Long id, String tableName);
    
    
    public List<SysExtraAttribute> selectParentAttribute(String tableName);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sysExtraAttribute 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SysExtraAttribute> selectSysExtraAttributeList(SysExtraAttribute sysExtraAttribute);

    /**
     * 新增【请填写功能名称】
     * 
     * @param sysExtraAttribute 【请填写功能名称】
     * @return 结果
     */
    public int insertSysExtraAttribute(SysExtraAttribute sysExtraAttribute);
    
    /**
     * 新增【请填写功能名称】
     * 
     * @param sysExtraAttribute 【请填写功能名称】
     * @return 结果
     */
    public int insertSysExtraAttributeCustom(String tableName, SysExtraAttribute sysExtraAttribute);

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysExtraAttribute 【请填写功能名称】
     * @return 结果
     */
    public int updateSysExtraAttribute(SysExtraAttribute sysExtraAttribute);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteSysExtraAttributeByIds(Long[] ids, String tableName);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteSysExtraAttributeById(Long id, String tableName);

	public List<SysExtraAttribute> selectSysExtraAttributeInHierarchy(String tableName);
}
