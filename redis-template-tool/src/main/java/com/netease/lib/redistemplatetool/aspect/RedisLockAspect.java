package com.netease.lib.redistemplatetool.aspect;

import com.netease.lib.redistemplatetool.redission.DistributedLock;
import com.netease.lib.redistemplatetool.redission.RedisDistributedLock;
import com.netease.lib.redistemplatetool.redission.RedissonService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Component
@Aspect
public class RedisLockAspect{

    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Value("${spring.application.id}")
    private String appId;
    @Value("spring.profiles.active:dev")
    private String active;

    @Resource
    private RedissonService redissonService;
    @Resource
    private DistributedLock redisDistributedLock;

    @Around("@annotation(com.netease.lib.redistemplatetool.aspect.RedisLock)")
    public Object redisLockOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = getMethod(joinPoint);
        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        long expire = 0L;
        if (redisLock.expire() > 0) {
            expire = redisLock.expire();
        }
        long timeout = 0L;
        if (redisLock.timeout() > 0) {
            timeout = redisLock.timeout();
        }

        String name = joinPoint.getSignature().getName();
        // 这里加appid，防止多应用共用一个依赖库，且逻辑名相同
        String key = redisLock.lockKey() + "_" + appId + "_" + active;
        try {
            redisDistributedLock.tryLock(key, timeout, expire);
            log.info("redis分布式锁获取成功！methodName={}, lockKey={}", name, key);
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("执行业务逻辑异常！methodName={}, lockId={}", name, key, e);
            throw e;
        } finally {
            redisDistributedLock.unLock(key);
        }

    }

    public Method getMethod(JoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod();
    }

}
