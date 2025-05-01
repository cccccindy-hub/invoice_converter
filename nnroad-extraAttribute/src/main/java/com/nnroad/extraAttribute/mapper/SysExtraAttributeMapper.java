package com.nnroad.extraAttribute.mapper;

import java.util.List;

import com.nnroad.extraAttribute.domain.SysExtraAttribute;
import org.apache.ibatis.annotations.Param;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2024-10-16
 */
public interface SysExtraAttributeMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public SysExtraAttribute selectSysExtraAttributeById(@Param("id") Long id, @Param("tableName") String tableName);

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
    public int insertSysExtraAttributeCustom(@Param("tableName") String tableName, @Param("sysExtraAttribute") SysExtraAttribute sysExtraAttribute);

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysExtraAttribute 【请填写功能名称】
     * @return 结果
     */
    public int updateSysExtraAttribute(@Param("sysExtraAttribute") SysExtraAttribute sysExtraAttribute);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteSysExtraAttributeById(@Param("id") Long id, @Param("tableName") String tableName);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysExtraAttributeByIds(@Param("ids") Long[] ids, @Param("tableName") String tableName);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
	public List<SysExtraAttribute> selectSysExtraAttributeInHierarchy(String tableName);
	
	/**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
	public List<SysExtraAttribute> selectExtraAttributeListByTableName(String tableName);

	/**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
	public List<SysExtraAttribute> selectParentAttribute(String tableName);

	/**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
	public int countNullValuesByKey(@Param("keyPath") String keyPath, @Param("tableName") String tableName);
}
