package com.nnroad.system.mapper;


import com.nnroad.system.domain.SysApp;

import java.util.List;

/**
 * 应用管理Mapper接口
 * 
 * @author ruoyi
 * @date 2023-05-25
 */
public interface SysAppMapper 
{
    /**
     * 查询应用管理
     * 
     * @param appId 应用管理主键
     * @return 应用管理
     */
    public SysApp selectSysAppByAppId(String appId);

    /**
     * 查询应用管理列表
     * 
     * @param sysApp 应用管理
     * @return 应用管理集合
     */
    public List<SysApp> selectSysAppList(SysApp sysApp);

    /**
     * 新增应用管理
     * 
     * @param sysApp 应用管理
     * @return 结果
     */
    public int insertSysApp(SysApp sysApp);

    /**
     * 修改应用管理
     * 
     * @param sysApp 应用管理
     * @return 结果
     */
    public int updateSysApp(SysApp sysApp);

    /**
     * 删除应用管理
     * 
     * @param appId 应用管理主键
     * @return 结果
     */
    public int deleteSysAppByAppId(String appId);

    /**
     * 批量删除应用管理
     * 
     * @param appIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAppByAppIds(String[] appIds);
}
