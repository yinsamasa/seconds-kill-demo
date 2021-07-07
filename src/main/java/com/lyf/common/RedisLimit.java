package com.lyf.common;

import com.lyf.utils.RedisPool;
import com.lyf.utils.RedisScriptUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 *  Redis 限流
 */


@Slf4j
public class RedisLimit {

    private static final int FAIL_CODE = 0;

    private static Integer limit = 5;

    //限流
    public static boolean limit() {
        Jedis jedis = null;
        Object result = null;
        try {
            // 获取jedis实例
            jedis = RedisPool.getJedis();
            // 解析Lua文件
            String script = RedisScriptUtil.getScript("redisLimit.lua");
            // 请求限流(取当前时间到秒)
            String key = String.valueOf(System.currentTimeMillis() / 1000);
            // 计数限流
            result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(String.valueOf(limit)));
            if (FAIL_CODE != (Long) result) {
                log.info("成功获取令牌");
                return true;
            }
        } catch (Exception e) {
            log.error("limit 获取 Jedis 实例失败：", e);
        } finally {
            RedisPool.jedisPoolClose(jedis);
        }
        return false;
    }
}
