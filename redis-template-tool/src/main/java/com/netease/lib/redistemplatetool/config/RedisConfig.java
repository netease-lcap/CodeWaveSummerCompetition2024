package com.netease.lib.redistemplatetool.config;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component("libraryRedisConfig")
public class RedisConfig {
    /**
     * redis配置类型1.url 2.单机 3.sentinel 4.cluster
     */

    @Value("${redisMode:1}")
    @NaslConfiguration(defaultValue = {@Environment(type = EnvironmentType.DEV, value = "1"),
            @Environment(type = EnvironmentType.ONLINE, value = "1")})
    public String redisMode;

    /**
     * redis 地址。redis模式多选一，按需配置。无用的配置可空
     */
    @Value("${redisHost:}")
    @NaslConfiguration
    public String redisHost;

    /**
     * redis 端口
     */
    @Value("${redisPort:}")
    @NaslConfiguration
    public String redisPort;

    /**
     * redis密码
     */
    @Value("${redisPassword:}")
    @NaslConfiguration
    public String redisPassword;

    /**
     * redis sentinel 主节点
     */
    @Value("${redisSentinelMaster:}")
    @NaslConfiguration
    public String redisSentinelMaster;

    /**
     * redis sentinel 节点
     */
    @Value("${redisSentinelNodes:}")
    @NaslConfiguration
    public String redisSentinelNodes;

    /**
     * redis cluster 节点
     */
    @Value("${redisClusterNodes:}")
    @NaslConfiguration
    public String redisClusterNodes;

    /**
     * redis cluster 节点最大重定向次数
     */
    @Value("${redisClusterMaxRedirects:}")
    @NaslConfiguration
    public String redisClusterMaxRedirects;

    /**
     * redis哨兵密码
     */
    @Value("${redisSentinelPassword:}")
    @NaslConfiguration
    public String redisSentinelPassword;

    /**
     * Redis 数据库索引
     */
    @Value("${redisDatabase:}")
    @NaslConfiguration
    public String redisDatabase;

    /**
     * 连接超时时间，默认为 0，单位为毫秒。
     */
    @Value("${springRedisTimeout:}")
    @NaslConfiguration
    public String springRedisTimeout;

    /**
     * 是否启用 SSL 连接，默认为 false。
     */
    @Value("${springRedisSsl:}")
    @NaslConfiguration
    public String springRedisSsl;

    /**
     * redis url
     */
    @Value("${redisUrl:}")
    @NaslConfiguration
    public String redisUrl;

    /**
     * 客户端名称
     */
    @Value("${redisClientName:}")
    @NaslConfiguration
    public String redisClientName;

    /**
     * 连接池最大连接数。
     */
    @Value("${springRedisLettucePoolMaxActive:}")
    @NaslConfiguration
    public String springRedisLettucePoolMaxActive;

    /**
     * 连接池中的最大空闲连接。
     */
    @Value("${springRedisLettucePoolMaxIdle:}")
    @NaslConfiguration
    public String springRedisLettucePoolMaxIdle;

    /**
     * 连接池中的最小空闲连接。
     */
    @Value("${springRedisLettucePoolMinIdle:}")
    @NaslConfiguration
    public String springRedisLettucePoolMinIdle;

    /**
     * 获取连接时的最大等待时间，默认为 100 毫秒。
     */
    @Value("${springRedisLettucePoolMaxWait:}")
    @NaslConfiguration
    public String springRedisLettucePoolMaxWait;

    /**
     * 关闭连接时的超时时间，默认为 100 毫秒。
     */
    @Value("${springRedisLettuceShutdownTimeout:}")
    @NaslConfiguration
    public String springRedisLettuceShutdownTimeout;

    /**
     * redisson等待锁的超时时间,默认300000毫秒
     */
    @Value("${redissonWaitTimeout:300000}")
    @NaslConfiguration
    public String redissonWaitTimeout;

    @Value("${spring.application.id}")
    private String applicationId;

    public String getRedissonWaitTimeout() {
        return redissonWaitTimeout;
    }

    public void setRedissonWaitTimeout(String redissonWaitTimeout) {
        this.redissonWaitTimeout = redissonWaitTimeout;
    }

    public String getRedisMode() {
        return redisMode;
    }

    public void setRedisMode(String redisMode) {
        this.redisMode = redisMode;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public String getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(String redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public String getRedisSentinelMaster() {
        return redisSentinelMaster;
    }

    public void setRedisSentinelMaster(String redisSentinelMaster) {
        this.redisSentinelMaster = redisSentinelMaster;
    }

    public String getRedisSentinelNodes() {
        return redisSentinelNodes;
    }

    public void setRedisSentinelNodes(String redisSentinelNodes) {
        this.redisSentinelNodes = redisSentinelNodes;
    }

    public String getRedisClusterNodes() {
        return redisClusterNodes;
    }

    public void setRedisClusterNodes(String redisClusterNodes) {
        this.redisClusterNodes = redisClusterNodes;
    }

    public String getRedisClusterMaxRedirects() {
        return redisClusterMaxRedirects;
    }

    public void setRedisClusterMaxRedirects(String redisClusterMaxRedirects) {
        this.redisClusterMaxRedirects = redisClusterMaxRedirects;
    }

    public String getRedisSentinelPassword() {
        return redisSentinelPassword;
    }

    public void setRedisSentinelPassword(String redisSentinelPassword) {
        this.redisSentinelPassword = redisSentinelPassword;
    }

    public String getRedisDatabase() {
        return redisDatabase;
    }

    public void setRedisDatabase(String redisDatabase) {
        this.redisDatabase = redisDatabase;
    }

    public String getSpringRedisTimeout() {
        return springRedisTimeout;
    }

    public void setSpringRedisTimeout(String springRedisTimeout) {
        this.springRedisTimeout = springRedisTimeout;
    }

    public String getSpringRedisSsl() {
        return springRedisSsl;
    }

    public void setSpringRedisSsl(String springRedisSsl) {
        this.springRedisSsl = springRedisSsl;
    }

    public String getRedisUrl() {
        return redisUrl;
    }

    public void setRedisUrl(String redisUrl) {
        this.redisUrl = redisUrl;
    }

    public String getRedisClientName() {
        return redisClientName;
    }

    public void setRedisClientName(String redisClientName) {
        this.redisClientName = redisClientName;
    }

    public String getSpringRedisLettucePoolMaxActive() {
        return springRedisLettucePoolMaxActive;
    }

    public void setSpringRedisLettucePoolMaxActive(String springRedisLettucePoolMaxActive) {
        this.springRedisLettucePoolMaxActive = springRedisLettucePoolMaxActive;
    }

    public String getSpringRedisLettucePoolMaxIdle() {
        return springRedisLettucePoolMaxIdle;
    }

    public void setSpringRedisLettucePoolMaxIdle(String springRedisLettucePoolMaxIdle) {
        this.springRedisLettucePoolMaxIdle = springRedisLettucePoolMaxIdle;
    }

    public String getSpringRedisLettucePoolMinIdle() {
        return springRedisLettucePoolMinIdle;
    }

    public void setSpringRedisLettucePoolMinIdle(String springRedisLettucePoolMinIdle) {
        this.springRedisLettucePoolMinIdle = springRedisLettucePoolMinIdle;
    }

    public String getSpringRedisLettucePoolMaxWait() {
        return springRedisLettucePoolMaxWait;
    }

    public void setSpringRedisLettucePoolMaxWait(String springRedisLettucePoolMaxWait) {
        this.springRedisLettucePoolMaxWait = springRedisLettucePoolMaxWait;
    }

    public String getSpringRedisLettuceShutdownTimeout() {
        return springRedisLettuceShutdownTimeout;
    }

    public void setSpringRedisLettuceShutdownTimeout(String springRedisLettuceShutdownTimeout) {
        this.springRedisLettuceShutdownTimeout = springRedisLettuceShutdownTimeout;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}
