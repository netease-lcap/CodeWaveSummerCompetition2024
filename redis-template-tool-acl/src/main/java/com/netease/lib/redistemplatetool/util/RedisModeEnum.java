package com.netease.lib.redistemplatetool.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RedisModeEnum {
    // url
    URL_MODE("1", new ArrayList<>()),
    //单机
    SINGLE_MODE("2", Arrays.asList("redis", "rediss")),
    //哨兵
    SENTINEL_MODE("3", Arrays.asList("redis-sentinel", "rediss-sentinel")),
    //集群
    CLUSTER_MODE("4", new ArrayList<>()),
    ;

    private final String key;
    private final List<String> schemes;

    // 构造函数
    RedisModeEnum(String key, List<String> schemes) {
        this.schemes = schemes;
        this.key = key;
    }

    /**
     * 根据scheme获取key
     *
     * @return
     */
    public static String getKeyByScheme(String scheme) {
        for (RedisModeEnum value : RedisModeEnum.values()) {
            if (value.schemes.contains(scheme)) {
                return value.key;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }
}