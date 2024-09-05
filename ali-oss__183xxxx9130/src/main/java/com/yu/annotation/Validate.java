package com.yu.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/21 14:06
 **/
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
    boolean required() default false;
}
