package com.nnroad.common.utils;


import cn.hutool.core.util.StrUtil;
import com.nnroad.common.core.redis.RedisCache;
import com.nnroad.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 全局配置类
 * 配置文件的开头的相关配置
 *
 * @author Aaron
 */
@Component
public class NnroadSequence {

    private static final Logger log = LoggerFactory.getLogger(NnroadSequence.class);

//    @Autowired
//    private JedisPool jedisPool;

    @Autowired
    private RedisCache redisCache;

//    /**
//     * 获取有过期时间的自增长ID
//     *
//     * @param redisKey
//     * @param increment
//     * @return
//     */
//    public long generate(String redisKey, Long increment) {
//
//        long valueNum = -1;
//
//        try(Jedis jedis = jedisPool.getResource()){
//            String value = StringUtils.isBlank(jedis.get(redisKey)) ? "0" : jedis.get(redisKey).toString();
//            valueNum = Long.parseLong(value);
//            if("0".equals(value)) {
//                valueNum = increment;
//                jedis.set(redisKey,increment.toString());
//            } else {
//                jedis.incrBy(redisKey, increment);
//                valueNum = Long.parseLong(jedis.get(redisKey));
//            }
//        } catch (Exception e){
//            e.printStackTrace(); // 打印堆栈信息
//            //return (int)(Math.random()* (10000000 - 100000) + 100000);
//            throw new BusinessException("Generate ID failed!");
//        }
//
//        return valueNum;
//    }
//
//
//    /**
//     * 获取有过期时间的自增长ID
//     *
//     * @param redisKey
//     * @return
//     */
//    public long generate(String redisKey) {
//        return generate(redisKey, 1l);
//    }
//
//
//
//    /**
//     * 不够位数的在前面补0，保留num的长度位数字
//     * @param redisKey
//     * @return
//     */
//    public String getCode(String redisKey, String joiner , int len) {
//
//        String code = "";
//
//        long num = generate(redisKey);
//        // 保留num的位数
//        // 0 代表前面补充0
//        // num 代表长度为4
//        // d 代表参数为正数型
//        code = redisKey + joiner + String.format("%0" + len + "d", num);
//
//        return code;
//    }
//
//    /**
//     * 根据数据库既存code重置Redis的数值并且获取code
//     *
//     * @param code
//     * @param joiner
//     * @return
//     */
//    public String resetAndGetCode(String code, String joiner) {
//        String[] codes = code.split(joiner);
//        String redisKey = null;
//        String num = null;
//        Long valueNum = 0l;
//        int len = 0;
//
//        if(codes.length > 1) {
//            redisKey = codes[0];
//            num = codes[1];
//            len = num.length();
//        }
//
//        try(Jedis jedis = jedisPool.getResource()){
//            // 重置Redis
//            jedis.set(redisKey,new Long(num).toString());
//            // 获取最新值
//            jedis.incrBy(redisKey, 1l);
//            valueNum = Long.parseLong(jedis.get(redisKey));
//        } catch (Exception e){
//            throw new BusinessException("resetAndGetCode failed!");
//        }
//
//        return redisKey + joiner + String.format("%0" + len + "d", valueNum);
//    }

    /**
     * 获取有过期时间的自增长ID
     *
     * @param redisKey
     * @param increment
     * @return
     */
    public Long generate(String redisKey, Long increment) {

        String value = redisCache.hasKey(redisKey) ? redisCache.getCacheObject(redisKey).toString() : "0";
        //set to default value if the key does not exist or not an integer
        if ("0".equals(value) || !value.matches("-?\\d+")) {
            redisCache.setCacheObject(redisKey, increment.intValue());
        }
        else if (value.matches("-?\\d+")) {
            redisCache.setCacheObject(redisKey, Integer.parseInt(value));
        }
        return redisCache.incr(redisKey, increment);
    }


    /**
     * 获取有过期时间的自增长ID
     *
     * @param redisKey
     * @return
     */
    public long generate(String redisKey) {
        return generate(redisKey, 1L);
    }


    /**
     * 不够位数的在前面补0，保留num的长度位数字
     *
     * @param redisKey
     * @return
     */
    public String getCode(String redisKey, String joiner, int len) {

        String code = "";

        long num = generate(redisKey);
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        code = redisKey + joiner + String.format("%0" + len + "d", num);

        return code;
    }

    /**
     * 根据数据库既存code重置Redis的数值并且获取code
     *
     * @param code
     * @param joiner
     * @return
     */
    public String resetAndGetCode(String code, String joiner) {
        String[] codes = code.split(joiner);
        String redisKey = null;
        String num = null;
        Long valueNum = 0l;
        int len = 0;

        if (codes.length > 1) {
            redisKey = codes[0];
            num = codes[1];
            len = num.length();
        }

        redisCache.setCacheObject(redisKey, Long.valueOf(num));
        // 获取最新值
        valueNum = redisCache.incr(redisKey, 1);

        return redisKey + joiner + String.format("%0" + len + "d", valueNum);
    }
}
