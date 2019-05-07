package com.example.demo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class RedisPoolFactory {
    @Autowired
    private RedisConfig redisConfig;

    @Bean
    public JedisPool poolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        poolConfig.setMinIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);

        JedisPool pool = new JedisPool(poolConfig, redisConfig.getHost(),
                redisConfig.getPort(), redisConfig.getTimeout() * 1000);
        return pool;
    }
}