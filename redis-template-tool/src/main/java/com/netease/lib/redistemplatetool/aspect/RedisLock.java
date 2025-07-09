package com.netease.lib.redistemplatetool.aspect;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    /**
     * key
     */
    String lockKey();

    /**
     * 过期时间
     */
    long expire() default 0L;

    /**
     * 超时时间
     */
    long timeout() default 0L;

    /**
     * 是否自动释放
     */
    boolean fastRelease() default true;

}
