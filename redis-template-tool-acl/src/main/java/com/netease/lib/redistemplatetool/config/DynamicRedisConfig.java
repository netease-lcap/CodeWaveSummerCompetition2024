package com.netease.lib.redistemplatetool.config;

import com.netease.lib.redistemplatetool.spring.LettuceVersionCheckUtil;
import com.netease.lib.redistemplatetool.util.RedisModeEnum;
import io.lettuce.core.RedisURI;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
public class DynamicRedisConfig {
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Resource
    private RedisConfig redisConfig;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // 使用String序列化器
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    private void convertRedisUriToRedisConfig() {
        String scheme = redisConfig.getRedisUrl().split(":")[0];
        String redisMode = RedisModeEnum.getKeyByScheme(scheme);
        RedisURI redisURI = RedisURI.create(redisConfig.getRedisUrl());
        if (redisMode == null) {
            log.error("redisUri scheme is null,{}", redisConfig.getRedisUrl());
            throw new RuntimeException("redis url模式仅支持单机和哨兵");
        }
        redisConfig.setRedisMode(redisMode);
        if (RedisModeEnum.SINGLE_MODE.getKey().equals(redisMode)) {
            redisConfig.setRedisHost(redisURI.getHost());
            redisConfig.setRedisPort(redisURI.getPort());
        } else if (RedisModeEnum.SENTINEL_MODE.getKey().equals(redisMode)) {
            redisConfig.setRedisSentinelMaster(redisURI.getSentinelMasterId());
            redisConfig.setRedisSentinelNodes(redisURI.getSentinels().stream().map(s -> s.getHost() + ":" + s.getPort()).collect(Collectors.joining(",")));
        } else {
            log.error("不支持的redis url scheme,{}", redisConfig.getRedisUrl());
            throw new RuntimeException("不支持的redis url scheme");
        }
        Optional.ofNullable(redisURI.getPassword()).filter(p -> !StringUtils.isEmpty(p)).ifPresent(p -> redisConfig.setRedisPassword(String.valueOf(p)));
        Optional.ofNullable(redisURI.getUsername()).filter(p -> !StringUtils.isEmpty(p)).ifPresent(redisConfig::setRedisUsername);
        Optional.of(redisURI.getDatabase()).filter(p -> p != 0).ifPresent(redisConfig::setRedisDatabase);
        Optional.ofNullable(redisURI.getClientName()).filter(p -> !StringUtils.isEmpty(p)).ifPresent(redisConfig::setRedisClientName);
        Optional.of(redisURI.isSsl()).filter(p -> p).ifPresent(p -> redisConfig.setSpringRedisSsl(p + ""));
        Optional.of(redisURI.getTimeout().getSeconds()).filter(p -> p != 0).ifPresent(redisConfig::setSpringRedisTimeout);
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        LettuceVersionCheckUtil.lettuceVersionCheckTask();
        if (RedisModeEnum.URL_MODE.getKey().equals(redisConfig.getRedisMode())) {
            convertRedisUriToRedisConfig();
        }
        if (RedisModeEnum.SINGLE_MODE.getKey().equals(redisConfig.getRedisMode())) {
            return createStandaloneConnectionFactory();
        } else if (RedisModeEnum.CLUSTER_MODE.getKey().equals(redisConfig.getRedisMode())) {
            return createClusterConnectionFactory();
        } else if (RedisModeEnum.SENTINEL_MODE.getKey().equals(redisConfig.getRedisMode())) {
            return createSentinelConnectionFactory();
        } else {
            log.error("redis mode异常,{}", redisConfig.getRedisMode());
            throw new RuntimeException("不支持的redis mode");
        }
    }

    private LettuceConnectionFactory createStandaloneConnectionFactory() {
        // 单机模式配置
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        if (redisConfig.getRedisPort() == null || StringUtils.isEmpty(redisConfig.getRedisPort())) {
            throw new RuntimeException("redis port/host is null");
        }
        config.setPort(redisConfig.getRedisPort());
        config.setHostName(redisConfig.getRedisHost());
        Optional.ofNullable(redisConfig.getRedisPassword()).filter(p -> !StringUtils.isEmpty(p)).ifPresent(config::setPassword);
        Optional.ofNullable(redisConfig.getRedisUsername()).filter(u -> !StringUtils.isEmpty(u)).ifPresent(config::setUsername);
        Optional.ofNullable(redisConfig.getRedisDatabase()).ifPresent(config::setDatabase);
        return new LettuceConnectionFactory(config, buildLettuceClientConfiguration());
    }

    private LettuceConnectionFactory createClusterConnectionFactory() {
        // 集群模式配置
        RedisClusterConfiguration config = new RedisClusterConfiguration();
        try {
            Arrays.stream(redisConfig.getRedisClusterNodes().split(",")).forEach(n -> {
                String[] hostAndPort = n.split(":");
                config.addClusterNode(new RedisNode(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
            });
        } catch (Exception e) {
            log.error("redis cluster nodes格式异常,{}", redisConfig.getRedisClusterNodes());
            throw new RuntimeException("redis cluster nodes格式异常");
        }
        Optional.ofNullable(redisConfig.getRedisClusterMaxRedirects()).filter(p -> p > 0).ifPresent(config::setMaxRedirects);
        Optional.ofNullable(redisConfig.getRedisPassword()).filter(p -> !StringUtils.isEmpty(p)).ifPresent(config::setPassword);
        Optional.ofNullable(redisConfig.getRedisUsername()).filter(u -> !StringUtils.isEmpty(u)).ifPresent(config::setUsername);
        return new LettuceConnectionFactory(config, buildLettuceClientConfiguration());
    }

    private LettuceConnectionFactory createSentinelConnectionFactory() {
        // 哨兵模式配置
        RedisSentinelConfiguration config = new RedisSentinelConfiguration();
        try {
            Arrays.stream(redisConfig.getRedisSentinelNodes().split(",")).forEach(n -> {
                String[] hostAndPort = n.split(":");
                config.addSentinel(new RedisNode(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
            });
        } catch (Exception e) {
            log.error("redis sentinel nodes格式异常,{}", redisConfig.getRedisClusterNodes());
            throw new RuntimeException("redis sentinel nodes格式异常");
        }
        config.setMaster(redisConfig.getRedisSentinelMaster());
        Optional.ofNullable(redisConfig.getRedisDatabase()).ifPresent(config::setDatabase);
        Optional.ofNullable(redisConfig.getRedisPassword()).filter(p -> !StringUtils.isEmpty(p)).ifPresent(config::setPassword);
        Optional.ofNullable(redisConfig.getRedisUsername()).filter(u -> !StringUtils.isEmpty(u)).ifPresent(config::setUsername);
        return new LettuceConnectionFactory(config, buildLettuceClientConfiguration());
    }


    private LettuceClientConfiguration buildLettuceClientConfiguration() {
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
        Optional.ofNullable(redisConfig.getSpringRedisTimeout()).ifPresent(r -> builder.commandTimeout(Duration.ofSeconds(r)));
        Optional.ofNullable(redisConfig.getSpringRedisLettuceShutdownTimeout()).ifPresent(r -> builder.shutdownTimeout(Duration.ofSeconds(r)));
        Optional.ofNullable(redisConfig.getRedisClientName()).filter(h -> !StringUtils.isEmpty(h)).ifPresent(builder::clientName);
        Optional.ofNullable(redisConfig.getSpringRedisSsl()).filter("true"::equals).ifPresent(h -> builder.useSsl());
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        Optional.ofNullable(redisConfig.getSpringRedisLettucePoolMaxActive()).ifPresent(poolConfig::setMaxTotal);
        Optional.ofNullable(redisConfig.getSpringRedisLettucePoolMaxIdle()).ifPresent(poolConfig::setMaxIdle);
        Optional.ofNullable(redisConfig.getSpringRedisLettucePoolMinIdle()).ifPresent(poolConfig::setMinIdle);
        Optional.ofNullable(redisConfig.getSpringRedisLettucePoolMaxWait()).ifPresent(poolConfig::setMaxWaitMillis);
        builder.poolConfig(poolConfig);
        return builder.build();
    }
}