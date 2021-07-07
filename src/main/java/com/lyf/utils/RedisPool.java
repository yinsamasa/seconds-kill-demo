package com.lyf.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *   Redis连接池配置
 */

public class RedisPool {

    private static JedisPool pool;

    //连接池最大连接数量
    private static Integer maxTotal = 300;

    //连接池中空闲连接的最大数量
    private static Integer maxIdle = 100;

    //最大建立连接等待时间。如果超过此时间将接到异常
    private static Integer maxWait = 10000;

    //testOnBorrow属性设置为true，从连接池中获取对象时，会进行检查，检查不通过，会从连接池中移走并销毁。
    private static Boolean testOnBorrow = true;

    private static String redisIP= "localhost";

    private static Integer redisPort = 6379;

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setBlockWhenExhausted(true);
        config.setMaxWaitMillis(maxWait);

        pool = new JedisPool(config, redisIP, redisPort, 1000 * 2);
    }

     //类加载到 jvm 时直接初始化连接池
    static {
        initPool();
    }

    //获取连接
    public static Jedis getJedis() {
        return pool.getResource();
    }

    //释放连接
    public static void jedisPoolClose(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
