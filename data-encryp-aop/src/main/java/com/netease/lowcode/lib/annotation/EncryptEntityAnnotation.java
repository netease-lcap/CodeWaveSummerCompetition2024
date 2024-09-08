package com.netease.lowcode.lib.annotation;

import com.netease.lowcode.annotation.LCAPSQLAnnotation;
import com.netease.lowcode.core.annotation.NaslAnnotation;

@NaslAnnotation(
        applyTo = {NaslAnnotation.Component.ENTITY}
)
public class EncryptEntityAnnotation extends LCAPSQLAnnotation {
    @NaslAnnotation.Property(
            title = "是否开启数据加密",
            defaultValue = "false"
    )
    public Boolean useAnno;
    @NaslAnnotation.Property(
            title = "加密字段列表, 逗号分隔"
    )
    public String encryptFieldList;
}
