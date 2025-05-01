package com.nnroad.system.service;


import com.nnroad.system.domain.SysApp;
import com.nnroad.system.form.VerifyForm;

import java.util.List;

/**
 * 应用管理Service接口
 *
 * @author ruoyi
 * @date 2023-05-25
 */
public interface ISysAppService {
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
     * 验证token
     * @return
     */
    public Boolean verifyToken(VerifyForm form);

    /**
     * 生成token
     *
     * @param appId
     * @return
     */
    public String genToken(String appId);

    /**
     * 修改应用管理
     *
     * @param sysApp 应用管理
     * @return 结果
     */
    public int updateSysApp(SysApp sysApp);

    /**
     * 批量删除应用管理
     *
     * @param appIds 需要删除的应用管理主键集合
     * @return 结果
     */
    public int deleteSysAppByAppIds(String[] appIds);

    /**
     * 删除应用管理信息
     *
     * @param appId 应用管理主键
     * @return 结果
     */
    public int deleteSysAppByAppId(String appId);
}
