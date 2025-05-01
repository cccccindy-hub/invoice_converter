package com.nnroad.datacenter.utils;

import com.nnroad.common.core.redis.RedisCache;
import com.nnroad.common.utils.spring.SpringUtils;
import com.nnroad.datacenter.common.Constants;
import com.nnroad.datacenter.domain.DCDictData;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 字典工具类
 *
 * @author Hrone
 */
@Component
public class DCDictUtils {

    /**
     * 设置字典缓存
     *
     * @param key       参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<DCDictData> dictDatas) {

        SpringUtils.getBean(RedisCache.class).setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<DCDictData> getDictCache(String key) {
        return SpringUtils.getBean(RedisCache.class).getCacheObject(getCacheKey(key));
    }


    /**
     * 清空字典缓存
     */
    public static void clearDictCache() {
        Collection<String> keys = SpringUtils.getBean(RedisCache.class).keys(Constants.DC_DICT_KEY + "*");
        SpringUtils.getBean(RedisCache.class).deleteObject(keys);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return Constants.DC_DICT_KEY + configKey;
    }
}
