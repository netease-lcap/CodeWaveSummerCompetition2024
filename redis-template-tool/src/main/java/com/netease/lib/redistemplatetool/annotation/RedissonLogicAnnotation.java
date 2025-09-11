package com.netease.lib.redistemplatetool.annotation;

import com.netease.lowcode.annotation.LCAPLogicAnnotation;
import com.netease.lowcode.core.annotation.NaslAnnotation;

/**
 * redisson分布式锁
 *
 * @author xujianping
 */
@NaslAnnotation(
        applyTo = {NaslAnnotation.Component.LOGIC}
)
public class RedissonLogicAnnotation extends LCAPLogicAnnotation {
    @NaslAnnotation.Property(
            title = "是否开启分布式锁",
            defaultValue = "false"
    )
    public Boolean useAnno;
    @NaslAnnotation.Property(title = "过期时间（ms）")
    public String inputText;
}
