package com.netease.lib.ipfilteraop.annotation;

import com.netease.lowcode.annotation.LCAPLogicAnnotation;
import com.netease.lowcode.core.annotation.NaslAnnotation;

/**
 * IP白名单注解
 */
@NaslAnnotation(
        applyTo = {NaslAnnotation.Component.LOGIC}
)
public class IpLogicAnnotation extends LCAPLogicAnnotation {
    @NaslAnnotation.Property(
            title = "是否开启IP白名单",             // IDE将使用这个值作为默认开关的标题
            defaultValue = "false"               // IDE将使用这个值作为开关的默认开启|关闭情况，useAnno的deaultValue必须为true或false
    )
    public Boolean useAnno; // 这个field代表的是注解的默认开关。因设计原因，public、Boolean、useAnno这三部分必须是固定的
    @NaslAnnotation.Property(title = "提示信息")
    public String inputText;
}
