package com.netease.lib.redistemplatetool.config;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RedisConfig {
    /**
     * redis配置类型1.url 2.单机 3.sentinel 4.cluster
     */

    @NaslConfiguration(defaultValue = {@Environment(type = EnvironmentType.DEV, value = "1"),
            @Environment(type = EnvironmentType.ONLINE, value = "1")})
    public String redisMode;
    /**
     * redis 地址。redis模式多选一，按需配置。无用的配置可空
     */
    @NaslConfiguration
    public String redisHost;
    /**
     * redis 端口
     */
    @NaslConfiguration
    public String redisPort;

    /**
     * redis密码
     */
    @NaslConfiguration
    public String redisPassword;
    /**
     * redis sentinel 主节点
     */
    @NaslConfiguration
    public String redisSentinelMaster;
    /**
     * redis sentinel 节点
     */
    @NaslConfiguration
    public String redisSentinelNodes;
    /**
     * redis cluster 节点
     */
    @NaslConfiguration
    public String redisClusterNodes;
    /**
     * redis cluster 节点最大重定向次数
     */
    @NaslConfiguration
    private String redisClusterMaxRedirects;

    /**
     * redis哨兵密码
     */
    @NaslConfiguration
    private String redisSentinelPassword;
    /**
     * Redis 数据库索引
     */
    @NaslConfiguration
    private String redisDatabase;

    /**
     * 连接超时时间，默认为 0，单位为毫秒。
     */
    @NaslConfiguration
    private String springRedisTimeout;
    /**
     * 是否启用 SSL 连接，默认为 false。
     */
    @NaslConfiguration
    private String springRedisSsl;
    /**
     * redis url
     */
    @NaslConfiguration
    private String redisUrl;
    /**
     * 客户端名称
     */
    @NaslConfiguration
    private String redisClientName;
    /**
     * 连接池最大连接数。
     */
    @NaslConfiguration
    private String springRedisLettucePoolMaxActive;
    /**
     * 连接池中的最大空闲连接。
     */
    @NaslConfiguration
    private String springRedisLettucePoolMaxIdle;
    /**
     * 连接池中的最小空闲连接。
     */
    @NaslConfiguration
    private String springRedisLettucePoolMinIdle;
    /**
     * 获取连接时的最大等待时间，默认为 100 毫秒。
     */
    @NaslConfiguration
    private String springRedisLettucePoolMaxWait;
    /**
     * 关闭连接时的超时时间，默认为 100 毫秒。
     */
    @NaslConfiguration
    private String springRedisLettuceShutdownTimeout;
}
