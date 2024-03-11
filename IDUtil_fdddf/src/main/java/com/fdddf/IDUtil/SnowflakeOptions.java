package com.fdddf.IDUtil;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * 雪花算法配置
 */
@NaslStructure
public class SnowflakeOptions {

    /**
     * 数据中心标识
     */
    public String datacenterId;

    /**
     * 机器标识
     */
    public String workerId;
}
