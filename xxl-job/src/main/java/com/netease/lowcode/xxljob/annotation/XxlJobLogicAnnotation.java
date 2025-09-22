package com.netease.lowcode.xxljob.annotation;

import com.netease.lowcode.annotation.LCAPLogicAnnotation;
import com.netease.lowcode.core.annotation.NaslAnnotation;

/**
 * xxljob注解
 *
 * @author xujianping
 */
@NaslAnnotation(
        applyTo = {NaslAnnotation.Component.LOGIC}
)
public class XxlJobLogicAnnotation extends LCAPLogicAnnotation {

    @NaslAnnotation.Property(
            title = "是否开启定时任务",
            defaultValue = "false"
    )
    public Boolean useAnno;

    @NaslAnnotation.Property(title = "JobHandler")
    public String inputText;
}
