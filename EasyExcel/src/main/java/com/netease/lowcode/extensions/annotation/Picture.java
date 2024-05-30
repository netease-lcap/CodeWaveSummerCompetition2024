package com.netease.lowcode.extensions.annotation;

import java.lang.annotation.*;

/**
 * 标识属性字段为图片
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Picture {
    /**
     * 指定图片列的索引，下标从0开始
     *
     * @return
     */
    int columnIndex() default 0;
}
