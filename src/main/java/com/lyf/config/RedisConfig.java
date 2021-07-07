package com.lyf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 */

//@Configuration
public class RedisConfig {

//    @Autowired
//    public static JedisPool pool;
//
//    @Value("${redis.maxTotal}")
//    private Integer maxTotal;
//    @Value("${redis.redisIP}")
//    private String host;
//    @Value("${redis.redisPort}")
//    private Integer port;
//
//    @Value("${redis.testOnBorrow}")
//    private boolean testOnBorrow;
//    @Value("${redis.maxIdle}")
//    private Integer maxIdle;
//    @Value("${redis.maxWait}")
//    private Integer maxWait;
//
//
//    public JedisPoolConfig jedisPoolConfig(){    //这个是修改redis性能的时候需要的对象
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxTotal(maxTotal);
//        jedisPoolConfig.setMaxWaitMillis(maxWait);
//        jedisPoolConfig.setMaxIdle(maxIdle);
//        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
//        return jedisPoolConfig;
//    }
//
//    @Bean  //这个注解注入工厂的名称是方法名
//    public JedisPool jedisPool(){
//        JedisPoolConfig jedisPoolConfig = jedisPoolConfig();
//        return new JedisPool(jedisPoolConfig,host,port,2000);
//    }
//
//    //获取连接
//    public static Jedis getJedis() {
//        return pool.getResource();
//    }
//
//    //释放连接
//    public static void jedisPoolClose(Jedis jedis) {
//        if (jedis != null) {
//            jedis.close();
//        }
//    }

}
