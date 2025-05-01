package com.nnroad.system.service.impl;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nnroad.common.constant.CacheConstants;
import com.nnroad.common.core.redis.RedisCache;
import com.nnroad.common.exception.Asserts;
import com.nnroad.common.exception.ServiceException;
import com.nnroad.common.utils.DateUtils;
import com.nnroad.common.utils.ThirdAppTokenUtils;
import com.nnroad.system.domain.SysApp;
import com.nnroad.system.form.VerifyForm;
import com.nnroad.system.mapper.SysAppMapper;
import com.nnroad.system.service.ISysAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * 应用管理Service业务层处理
 *
 * @author ruoyi
 * @date 2023-05-25
 */
@Service
public class SysAppServiceImpl implements ISysAppService {
    private static final Logger log = LoggerFactory.getLogger(SysAppServiceImpl.class);

    @Autowired
    private SysAppMapper sysAppMapper;

    @Autowired
    private RedisCache redisCache;

    private static final Object APP_LOCK = new Object();

    /**
     * 查询应用管理
     *
     * @param appId 应用管理主键
     * @return 应用管理
     */
    @Override
    public SysApp selectSysAppByAppId(String appId) {
        String cacheKey = String.format(CacheConstants.APP_AUTH_KEY, appId);
        String appInfo = redisCache.getCacheObject(cacheKey);
        if (StrUtil.isNotBlank(appInfo)) {
            return JSONObject.parseObject(appInfo, SysApp.class);
        }
        SysApp sysApp;
        synchronized (APP_LOCK) {
            appInfo = redisCache.getCacheObject(cacheKey);
            if (StrUtil.isBlank(appInfo)) {
                sysApp = sysAppMapper.selectSysAppByAppId(appId);
                if (sysApp != null) {
                    redisCache.setCacheObject(cacheKey, JSONObject.toJSONString(sysApp));
                }
            } else {
                sysApp = JSONObject.parseObject(appInfo, SysApp.class);
            }
        }

        return sysApp;
    }

    /**
     * 查询应用管理列表
     *
     * @param sysApp 应用管理
     * @return 应用管理
     */
    @Override
    public List<SysApp> selectSysAppList(SysApp sysApp) {
        return sysAppMapper.selectSysAppList(sysApp);
    }

    /**
     * 新增应用管理
     *
     * @param sysApp 应用管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSysApp(SysApp sysApp) {
        sysApp.setAppId(this.getUUID());
        sysApp.setAppKey(this.getUUID());
        sysApp.setSecretKey(getUUID());
        sysApp.setCreateTime(DateUtils.getNowDate());

        int row = sysAppMapper.insertSysApp(sysApp);
        Asserts.isTrue(row == 1, "新增失败！");

        redisCache.setCacheObject(String.format(CacheConstants.APP_AUTH_KEY, sysApp.getAppId()), JSONObject.toJSONString(sysApp));

        return row;
    }

    @Override
    public Boolean verifyToken(VerifyForm form) {
        String appId = form.getAppId();
        String appToken = form.getAppToken();

        SysApp sysApp = selectSysAppByAppId(appId);
        if (sysApp == null) {
            throw new ServiceException("app不存在");
        }
        try {
            ThirdAppTokenUtils.TokenInfo info = ThirdAppTokenUtils.decodeToken(appToken, sysApp.getSecretKey());

            return info.getAppId().equals(appId) && info.getAppKey().equals(sysApp.getAppKey());
        } catch (Exception e) {
            log.error("内部认证异常： appId-{}, e-{}", appId, e.getMessage());
            throw new ServiceException("e.getMessage()");
        }
    }

    @Override
    public String genToken(String appId) {
        SysApp sysApp = selectSysAppByAppId(appId);
        if (sysApp == null) {
            throw new ServiceException("app不存在");
        }
        return ThirdAppTokenUtils.generateToken(appId, sysApp.getAppKey(), sysApp.getSecretKey());
    }

    private String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 修改应用管理
     *
     * @param sysApp 应用管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSysApp(SysApp sysApp) {
        sysApp.setUpdateTime(DateUtils.getNowDate());
        int row = sysAppMapper.updateSysApp(sysApp);
        Asserts.isTrue(row == 1, "更新失败");

        redisCache.setCacheObject(String.format(CacheConstants.APP_AUTH_KEY, sysApp.getAppId()), JSONObject.toJSONString(sysApp));

        return row;
    }

    /**
     * 批量删除应用管理
     *
     * @param appIds 需要删除的应用管理主键
     * @return 结果
     */
    @Override
    public int deleteSysAppByAppIds(String[] appIds) {
        if (ObjectUtil.isEmpty(appIds)) {
            return 0;
        }
        for (String appId : appIds) {
            redisCache.deleteObject(String.format(CacheConstants.APP_AUTH_KEY, appId));
        }
        return sysAppMapper.deleteSysAppByAppIds(appIds);
    }

    /**
     * 删除应用管理信息
     *
     * @param appId 应用管理主键
     * @return 结果
     */
    @Override
    public int deleteSysAppByAppId(String appId) {
        redisCache.deleteObject(String.format(CacheConstants.APP_AUTH_KEY, appId));

        return sysAppMapper.deleteSysAppByAppId(appId);
    }
}
