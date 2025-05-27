package com.netease.lib.redistemplatetool.util;

import java.util.Arrays;
import java.util.List;

public enum RedisModeEnum {
    // url
    URL_MODE("1", Arrays.asList("extensions.redis_template_tool.custom.redisUrl")),
    //单机
    SINGLE_MODE("2", Arrays.asList("extensions.redis_template_tool.custom.redisHost", "extensions.redis_template_tool.custom.redisPort")),
    //哨兵
    SENTINEL_MODE("3", Arrays.asList("extensions.redis_template_tool.custom.redisSentinelMaster", "extensions.redis_template_tool.custom.redisSentinelNodes", "extensions.redis_template_tool.custom.redisSentinelPassword")),
    //集群
    CLUSTER_MODE("4", Arrays.asList("extensions.redis_template_tool.custom.redisClusterNodes", "extensions.redis_template_tool.custom.redisClusterMaxRedirects")),
    ;

    private final String key;
    private final List<String> dataMap;

    // 构造函数
    RedisModeEnum(String key, List<String> dataMap) {
        this.dataMap = dataMap;
        this.key = key;
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
}