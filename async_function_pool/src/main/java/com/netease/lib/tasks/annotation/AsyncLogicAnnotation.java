package com.netease.lib.tasks.annotation;

import com.netease.lowcode.annotation.LCAPLogicAnnotation;
import com.netease.lowcode.core.annotation.NaslAnnotation;

@NaslAnnotation(
        applyTo = {NaslAnnotation.Component.LOGIC} // 用于声明这是一个逻辑注解
)
public class AsyncLogicAnnotation extends LCAPLogicAnnotation {
    @NaslAnnotation.Property(
            title = "是否开加入多线程执行",
            defaultValue = "false"
    )
    public Boolean useAnno;
}
