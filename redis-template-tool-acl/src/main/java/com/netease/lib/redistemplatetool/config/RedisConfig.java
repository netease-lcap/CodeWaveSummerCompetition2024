package com.netease.lib.redistemplatetool.config;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RedisConfig {
    /**
     * redis配置类型1.url 2.单机 3.sentinel（不支持高版本redis服务端） 4.cluster
     */

    @NaslConfiguration(defaultValue = {@Environment(type = EnvironmentType.DEV, value = "1"),
            @Environment(type = EnvironmentType.ONLINE, value = "1")})
    @Value("${redisMode}")
    public String redisMode;
    /**
     * redis 地址。redis模式多选一，按需配置。无用的配置可空
     */
    @NaslConfiguration
    @Value("${redisHost}")
    public String redisHost;
    /**
     * redis 端口
     */
    @NaslConfiguration
    @Value("${redisPort}")
    public Integer redisPort;

    /**
     * redis密码
     */
    @NaslConfiguration
    @Value("${redisPassword}")
    public String redisPassword;

    /**
     * redis用户名
     */
    @NaslConfiguration
    @Value("${redisUsername}")
    public String redisUsername;
    /**
     * redis sentinel 主节点
     */
    @NaslConfiguration
    @Value("${redisSentinelMaster}")
    public String redisSentinelMaster;
    /**
     * redis sentinel 节点
     */
    @NaslConfiguration
    @Value("${redisSentinelNodes}")
    public String redisSentinelNodes;
    /**
     * redis cluster 节点
     */
    @NaslConfiguration
    @Value("${redisClusterNodes}")
    public String redisClusterNodes;
    /**
     * redis cluster 节点最大重定向次数
     */
    @NaslConfiguration
    @Value("${redisClusterMaxRedirects}")
    public Integer redisClusterMaxRedirects;
    /**
     * Redis 数据库索引
     */
    @NaslConfiguration
    @Value("${redisDatabase}")
    public Integer redisDatabase;
    /**
     * 连接超时时间，默认为 0，单位为毫秒。
     */
    @NaslConfiguration
    @Value("${springRedisTimeout}")
    public Long springRedisTimeout;
    /**
     * 是否启用 SSL 连接，默认为 false。
     */
    @NaslConfiguration
    @Value("${springRedisSsl}")
    public String springRedisSsl;
    /**
     * redis url，仅支持单机和哨兵
     */
    @NaslConfiguration
    @Value("${redisUrl}")
    public String redisUrl;
    /**
     * 客户端名称
     */
    @NaslConfiguration
    @Value("${redisClientName}")
    public String redisClientName;
    /**
     * 连接池最大连接数。
     */
    @NaslConfiguration
    @Value("${springRedisLettucePoolMaxActive}")
    public Integer springRedisLettucePoolMaxActive;
    /**
     * 连接池中的最大空闲连接。
     */
    @NaslConfiguration
    @Value("${springRedisLettucePoolMaxIdle}")
    public Integer springRedisLettucePoolMaxIdle;
    /**
     * 连接池中的最小空闲连接。
     */
    @NaslConfiguration
    @Value("${springRedisLettucePoolMinIdle}")
    public Integer springRedisLettucePoolMinIdle;
    /**
     * 获取连接时的最大等待时间，默认为 100 毫秒。
     */
    @NaslConfiguration
    @Value("${springRedisLettucePoolMaxWait}")
    public Integer springRedisLettucePoolMaxWait;
    /**
     * 关闭连接时的超时时间，默认为 100 毫秒。
     */
    @NaslConfiguration
    @Value("${springRedisLettuceShutdownTimeout}")
    public Long springRedisLettuceShutdownTimeout;

    public String getRedisUsername() {
        return redisUsername;
    }

    public void setRedisUsername(String redisUsername) {
        this.redisUsername = redisUsername;
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

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
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

    public Integer getRedisClusterMaxRedirects() {
        return redisClusterMaxRedirects;
    }

    public void setRedisClusterMaxRedirects(Integer redisClusterMaxRedirects) {
        this.redisClusterMaxRedirects = redisClusterMaxRedirects;
    }

    public Integer getRedisDatabase() {
        return redisDatabase;
    }

    public void setRedisDatabase(Integer redisDatabase) {
        this.redisDatabase = redisDatabase;
    }

    public Long getSpringRedisTimeout() {
        return springRedisTimeout;
    }

    public void setSpringRedisTimeout(Long springRedisTimeout) {
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

    public Integer getSpringRedisLettucePoolMaxActive() {
        return springRedisLettucePoolMaxActive;
    }

    public void setSpringRedisLettucePoolMaxActive(Integer springRedisLettucePoolMaxActive) {
        this.springRedisLettucePoolMaxActive = springRedisLettucePoolMaxActive;
    }

    public Integer getSpringRedisLettucePoolMaxIdle() {
        return springRedisLettucePoolMaxIdle;
    }

    public void setSpringRedisLettucePoolMaxIdle(Integer springRedisLettucePoolMaxIdle) {
        this.springRedisLettucePoolMaxIdle = springRedisLettucePoolMaxIdle;
    }

    public Integer getSpringRedisLettucePoolMinIdle() {
        return springRedisLettucePoolMinIdle;
    }

    public void setSpringRedisLettucePoolMinIdle(Integer springRedisLettucePoolMinIdle) {
        this.springRedisLettucePoolMinIdle = springRedisLettucePoolMinIdle;
    }

    public Integer getSpringRedisLettucePoolMaxWait() {
        return springRedisLettucePoolMaxWait;
    }

    public void setSpringRedisLettucePoolMaxWait(Integer springRedisLettucePoolMaxWait) {
        this.springRedisLettucePoolMaxWait = springRedisLettucePoolMaxWait;
    }

    public Long getSpringRedisLettuceShutdownTimeout() {
        return springRedisLettuceShutdownTimeout;
    }

    public void setSpringRedisLettuceShutdownTimeout(Long springRedisLettuceShutdownTimeout) {
        this.springRedisLettuceShutdownTimeout = springRedisLettuceShutdownTimeout;
    }
}
