package com.netease.lib.redistemplatetool.config;

import com.netease.lib.redistemplatetool.util.RedisModeEnum;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class RedissonConfiguration {

    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Resource(name = "libraryRedisConfig")
    private RedisConfig redisConfig;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        String redisMode = redisConfig.getRedisMode();
        RedisModeEnum redisModeEnum = RedisModeEnum.getRedisModeByKey(redisMode);
        Config config = redisModeEnum.buildRedissonConfig(redisConfig);
        return Redisson.create(config);
    }

}

