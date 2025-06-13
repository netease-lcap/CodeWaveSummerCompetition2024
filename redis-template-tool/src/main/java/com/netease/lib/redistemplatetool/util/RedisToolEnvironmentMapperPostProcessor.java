package com.netease.lib.redistemplatetool.util;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Order
public class RedisToolEnvironmentMapperPostProcessor implements EnvironmentPostProcessor {
    private static final Map<String, String> REDIS_CONFIG_MAPPER = new HashMap<>();
    private static final Map<String, String> REDIS_CONFIG_MAPPER_COMMON = new HashMap<>();

    //File文件类型  数据权限
    static {
        //url
        REDIS_CONFIG_MAPPER.put("extensions.redis_template_tool.custom.redisUrl", "spring.redis.url");
        //单机
        REDIS_CONFIG_MAPPER.put("extensions.redis_template_tool.custom.redisHost", "spring.redis.host");
        REDIS_CONFIG_MAPPER.put("extensions.redis_template_tool.custom.redisPort", "spring.redis.port");
        //哨兵
        REDIS_CONFIG_MAPPER.put("extensions.redis_template_tool.custom.redisSentinelMaster", "spring.redis.sentinel.master");
        REDIS_CONFIG_MAPPER.put("extensions.redis_template_tool.custom.redisSentinelNodes", "spring.redis.sentinel.nodes");
        REDIS_CONFIG_MAPPER.put("extensions.redis_template_tool.custom.redisSentinelPassword", "spring.redis.sentinel.password");
        //集群
        REDIS_CONFIG_MAPPER.put("extensions.redis_template_tool.custom.redisClusterNodes", "spring.redis.cluster.nodes");
        REDIS_CONFIG_MAPPER.put("extensions.redis_template_tool.custom.redisClusterMaxRedirects", "spring.redis.cluster.maxRedirects");
        //公共
        REDIS_CONFIG_MAPPER_COMMON.put("extensions.redis_template_tool.custom.redisDatabase", "spring.redis.database");
        REDIS_CONFIG_MAPPER_COMMON.put("extensions.redis_template_tool.custom.redisPassword", "spring.redis.password");
        REDIS_CONFIG_MAPPER_COMMON.put("extensions.redis_template_tool.custom.redisClientName", "spring.redis.clientName");
        REDIS_CONFIG_MAPPER_COMMON.put("extensions.redis_template_tool.custom.springRedisTimeout", "spring.redis.timeout");
        REDIS_CONFIG_MAPPER_COMMON.put("extensions.redis_template_tool.custom.springRedisSsl", "spring.redis.ssl");
        REDIS_CONFIG_MAPPER_COMMON.put("extensions.redis_template_tool.custom.springRedisLettucePoolMaxActive", "spring.redis.lettuce.pool.max-active");
        REDIS_CONFIG_MAPPER_COMMON.put("extensions.redis_template_tool.custom.springRedisLettucePoolMaxIdle", "spring.redis.lettuce.pool.max-idle");
        REDIS_CONFIG_MAPPER_COMMON.put("extensions.redis_template_tool.custom.springRedisLettucePoolMinIdle", "spring.redis.lettuce.pool.min-idle");
        REDIS_CONFIG_MAPPER_COMMON.put("extensions.redis_template_tool.custom.springRedisLettucePoolMaxWait", "spring.redis.lettuce.pool.max-wait");
        REDIS_CONFIG_MAPPER_COMMON.put("extensions.redis_template_tool.custom.springRedisLettuceShutdownTimeout", "spring.redis.lettuce.shutdown-timeout");
    }

    /**
     * Post-process the given {@code environment}.
     *
     * @param environment the environment to post-process
     * @param application the application to which the environment belongs
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> mapperProperties = new HashMap<>();
        String redisMode = environment.getProperty("extensions.redis_template_tool.custom.redisMode");
        if (StringUtils.isEmpty(redisMode)) {
            redisMode = "1";
        }
        List<String> redisCustomConfigList = RedisModeEnum.getRedisModeValueByKey(redisMode);
        //自定义模式配置
        REDIS_CONFIG_MAPPER.forEach((key, value) -> {
            if (environment.containsProperty(value)) {
                return;
            }
            if (environment.containsProperty(key) && redisCustomConfigList.contains(key)) {
                String property = environment.getProperty(key);
                if (!StringUtils.isEmpty(property)) {
                    mapperProperties.put(value, property);
                }
            }
        });
        //公共配置
        REDIS_CONFIG_MAPPER_COMMON.forEach((key, value) -> {
            if (environment.containsProperty(value)) {
                return;
            }
            if (environment.containsProperty(key)) {
                String property = environment.getProperty(key);
                if (!StringUtils.isEmpty(property)) {
                    mapperProperties.put(value, property);
                }
            }
        });

        if (mapperProperties.isEmpty()) {
            return;
        }
        environment.getPropertySources().addLast(new MapPropertySource("REDIS_CONFIG_MAPPER", mapperProperties));
    }

}