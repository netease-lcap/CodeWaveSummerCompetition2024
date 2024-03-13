package com.fdddf.IDUtil;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * IDUtil中TimeId方法使用的结构体
 */
@NaslStructure
public class IDUtilTime {
    /**
     * 起始时间，格式为yyyy-MM-dd HH:mm:ss，如2024-01-01 00:00:00
     */
    public String startTime;

    /**
     * 随机数位数
     */
    public Integer randomLength;

}
