package com.netease.lib.redistemplatetool.util;

import com.netease.lib.redistemplatetool.config.RedisConfig;
import org.apache.commons.lang3.StringUtils;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public enum RedisModeEnum {

    // url
    URL_MODE("1", Arrays.asList("extensions.redis_template_tool.custom.redisUrl")) {
        @Override
        public Config buildRedissonConfig(RedisConfig redisConfig) {
            String redisUrl = redisConfig.getRedisUrl();
            log.info("配置Redis URL模式开始: {}", redisUrl);
            if (StringUtils.isBlank(redisUrl)) {
                throw new IllegalArgumentException("URL模式下redisUrl不能为空");
            }

            Config config = buildSingleConfig(redisConfig, redisUrl);
            log.info("配置Redis URL模式结束");
            return config;
        }
    },
    //单机
    SINGLE_MODE("2", Arrays.asList("extensions.redis_template_tool.custom.redisHost", "extensions.redis_template_tool.custom.redisPort")) {
        @Override
        public Config buildRedissonConfig(RedisConfig redisConfig) {
            log.info("配置Redis单机模式开始: {}", redisConfig.getRedisHost());
            String host = redisConfig.getRedisHost();
            String port = redisConfig.getRedisPort();
            if (StringUtils.isBlank(host) || StringUtils.isBlank(port)) {
                throw new IllegalArgumentException("单机模式下host和port不能为空!");
            }

            String address = "redis://" + host + ":" + port;
            Config config = buildSingleConfig(redisConfig, address);
            log.info("配置Redis 单机模式结束");
            return config;
        }
    },
    //哨兵
    SENTINEL_MODE("3", Arrays.asList("extensions.redis_template_tool.custom.redisSentinelMaster", "extensions.redis_template_tool.custom.redisSentinelNodes", "extensions.redis_template_tool.custom.redisSentinelPassword")) {
        @Override
        public Config buildRedissonConfig(RedisConfig redisConfig) {
            String masterName = redisConfig.getRedisSentinelMaster();
            String sentinelNodes = redisConfig.getRedisSentinelNodes();
            log.info("配置Redis哨兵模式开始 - Master: {}, Sentinels: {}", masterName, sentinelNodes);
            if (StringUtils.isBlank(sentinelNodes) || StringUtils.isBlank(masterName)) {
                throw new IllegalArgumentException("哨兵模式下sentinelNodes和master不能为空");
            }

            Config config = configureCommon();
            SentinelServersConfig sentinelConfig = config.useSentinelServers();
            sentinelConfig.setMasterName(masterName);
            String[] nodeArray = sentinelNodes.split(",");
            for (String node : nodeArray) {
                sentinelConfig.addSentinelAddress("redis://" + node.trim());
            }
            String redisDatabase = redisConfig.getRedisDatabase();
            if (StringUtils.isNotBlank(redisDatabase)) {
                sentinelConfig.setDatabase(Integer.parseInt(redisConfig.getRedisDatabase()));
            }
            String springRedisTimeout = redisConfig.getSpringRedisTimeout();
            if (StringUtils.isNotBlank(springRedisTimeout)) {
                sentinelConfig.setConnectTimeout(Integer.parseInt(springRedisTimeout));
            }
            // 连接池最大连接数
            String springRedisLettucePoolMaxActive = redisConfig.getSpringRedisLettucePoolMaxActive();
            if (StringUtils.isNotBlank(springRedisLettucePoolMaxActive)) {
                sentinelConfig.setMasterConnectionPoolSize(Integer.parseInt(springRedisLettucePoolMaxActive));
                sentinelConfig.setSlaveConnectionPoolSize(Integer.parseInt(springRedisLettucePoolMaxActive));
            }
            // 最小空闲连接池
            String springRedisLettucePoolMinIdle = redisConfig.getSpringRedisLettucePoolMinIdle();
            if (StringUtils.isNotBlank(springRedisLettucePoolMinIdle)) {
                sentinelConfig.setSlaveConnectionMinimumIdleSize(Integer.parseInt(springRedisLettucePoolMinIdle));
                sentinelConfig.setMasterConnectionMinimumIdleSize(Integer.parseInt(springRedisLettucePoolMinIdle));
            }
            // 设置Redis密码
            if (StringUtils.isNotBlank(redisConfig.getRedisPassword())) {
                sentinelConfig.setPassword(redisConfig.getRedisPassword());
            }

            log.info("配置Redis哨兵模式结束");
            return config;
        }
    },
    //集群
    CLUSTER_MODE("4", Arrays.asList("extensions.redis_template_tool.custom.redisClusterNodes", "extensions.redis_template_tool.custom.redisClusterMaxRedirects")) {
        @Override
        public Config buildRedissonConfig(RedisConfig redisConfig) {
            String redisClusterNodes = redisConfig.getRedisClusterNodes();
            if (StringUtils.isBlank(redisClusterNodes)) {
                throw new IllegalArgumentException("集群模式下cluster.nodes不能为空");
            }

            log.info("配置Redis集群模式开始: {}", redisClusterNodes);
            Config config = configureCommon();
            ClusterServersConfig clusterConfig = config.useClusterServers();
            String[] nodeArray = redisClusterNodes.split(",");
            for (String node : nodeArray) {
                clusterConfig.addNodeAddress("redis://" + node.trim());
            }
            String password = redisConfig.getRedisPassword();
            if (StringUtils.isNotBlank(password)) {
                clusterConfig.setPassword(password);
            }
            // 连接池最大连接数
            String springRedisLettucePoolMaxActive = redisConfig.getSpringRedisLettucePoolMaxActive();
            if (StringUtils.isNotBlank(springRedisLettucePoolMaxActive)) {
                clusterConfig.setMasterConnectionPoolSize(Integer.parseInt(springRedisLettucePoolMaxActive));
                clusterConfig.setSlaveConnectionPoolSize(Integer.parseInt(springRedisLettucePoolMaxActive));
            }
            // 最小空闲连接池
            String springRedisLettucePoolMinIdle = redisConfig.getSpringRedisLettucePoolMinIdle();
            if (StringUtils.isNotBlank(springRedisLettucePoolMinIdle)) {
                clusterConfig.setSlaveConnectionMinimumIdleSize(Integer.parseInt(springRedisLettucePoolMinIdle));
                clusterConfig.setMasterConnectionMinimumIdleSize(Integer.parseInt(springRedisLettucePoolMinIdle));
            }

            log.info("配置Redis集群模式结束");
            return config;
        }
    },
    ;

    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    private final String key;
    private final List<String> dataMap;

    // 构造函数
    RedisModeEnum(String key, List<String> dataMap) {
        this.dataMap = dataMap;
        this.key = key;
    }

    public static RedisModeEnum getRedisModeByKey(String key) {
        return Arrays.stream(RedisModeEnum.values())
                .filter(e -> e.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未找到对应的Redis模式"));
    }

    public static List<String> getRedisModeValueByKey(String key) {
        RedisModeEnum[] redisModeEnums = RedisModeEnum.values();
        for (int i = 0; i < redisModeEnums.length; i++) {
            if (redisModeEnums[i].getKey().equals(key)) {
                return redisModeEnums[i].getDataMap();
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public List<String> getDataMap() {
        return dataMap;
    }


    public abstract Config buildRedissonConfig(RedisConfig config);


    /**
     * redis单机模式 & url模式
     */
    private static Config buildSingleConfig(RedisConfig redisConfig, String address) {
        Config config = configureCommon();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(address);
        // 数据库
        String redisDatabase = redisConfig.getRedisDatabase();
        if (StringUtils.isNotBlank(redisDatabase)) {
            singleServerConfig.setDatabase(Integer.parseInt(redisConfig.getRedisDatabase()));
        }
        // 密码
        if (StringUtils.isNotBlank(redisConfig.getRedisPassword())) {
            singleServerConfig.setPassword(redisConfig.getRedisPassword());
        }
        // 连接时间
        String springRedisTimeout = redisConfig.getSpringRedisTimeout();
        if (StringUtils.isNotBlank(springRedisTimeout)) {
            singleServerConfig.setConnectTimeout(Integer.parseInt(springRedisTimeout));
        }
        // 最大空闲连接池
        String springRedisLettucePoolMaxActive = redisConfig.getSpringRedisLettucePoolMaxActive();
        if (StringUtils.isNotBlank(springRedisLettucePoolMaxActive)) {
            singleServerConfig.setConnectionPoolSize(Integer.parseInt(springRedisLettucePoolMaxActive));
        }
        // 最小空闲连接池
        String springRedisLettucePoolMinIdle = redisConfig.getSpringRedisLettucePoolMinIdle();
        if (StringUtils.isNotBlank(springRedisLettucePoolMinIdle)) {
            singleServerConfig.setConnectionMinimumIdleSize(Integer.parseInt(springRedisLettucePoolMinIdle));
        }

        return config;
    }

    /**
     * 配置通用设置
     */
    private static Config configureCommon() {
        Config config = new Config();
        // 设置编解码器
        config.setCodec(new JsonJacksonCodec());
        // 设置线程数
        config.setThreads(16);
        config.setNettyThreads(32);
        // 设置锁看门狗超时时间
        config.setLockWatchdogTimeout(30000L);
        // 设置keep alive
        config.setKeepPubSubOrder(true);
        log.debug("Redisson通用配置完成");
        return config;
    }

}