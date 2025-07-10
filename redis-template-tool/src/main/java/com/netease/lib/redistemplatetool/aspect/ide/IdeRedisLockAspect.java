package com.netease.lib.redistemplatetool.aspect.ide;

import com.netease.lib.redistemplatetool.redission.DistributedLock;
import com.netease.lib.redistemplatetool.redission.RedisDistributedLock;
import com.netease.lib.redistemplatetool.config.RedisConfig;
import com.netease.lib.redistemplatetool.redission.RedissonService;
import com.netease.lib.redistemplatetool.util.RedissonAspectHelper;
import com.netease.lowcode.annotation.context.LogicContext;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Component
@Aspect
public class IdeRedisLockAspect {

    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    /**
     * redis分布式锁模版key
     */
    private static final String REDIS_LOCK_KEY_FORMAT = "redisLock:%s:%s";

    @Resource
    private RedissonService redissonService;
    @Resource
    private RedisConfig redisConfig;
    @Resource
    private DistributedLock redisDistributedLock;

    // 只拦截带@Controller/@RestController注解的类
    @Around("execution(* com..*.web.controller.logics..*.*(..)) && " +
            "(@within(org.springframework.stereotype.Controller) || " +
            "@within(org.springframework.web.bind.annotation.RestController))")
    public Object redisLockOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        List<LogicContext> logicContexts = RedissonAspectHelper.listJobContext();
        if (logicContexts == null || logicContexts.isEmpty()) {
            return joinPoint.proceed();
        }

        LogicContext logicContext = logicContexts.stream()
                .filter(e -> {
                    if (!e.getLogicName().equals(methodName)) {
                        return false;
                    }
                    String useAnno = String.valueOf(e.getAnnotationProperties().get("useAnno"));
                    if (!StringUtils.equals("true", useAnno)) {
                        return false;
                    }
                    return true;
                })
                .findFirst()
                .orElse(null);
        if (Objects.isNull(logicContext)) {
            return joinPoint.proceed();
        }

        log.info("redis分布式锁开始！methodName={}", methodName);
        String applicationId = redisConfig.getApplicationId();
        String lockKey = String.format(REDIS_LOCK_KEY_FORMAT, applicationId, methodName);
        Object text = logicContext.getAnnotationProperties().get("inputText");
        long expire = 30000L;
        if (Objects.nonNull(text)) {
            String extStr = String.valueOf(text);
            expire = Long.parseLong(extStr);
        }

        try {
            redisDistributedLock.tryLock(lockKey, Long.parseLong(redisConfig.getRedissonWaitTimeout()), expire);
            log.info("redis分布式锁获取成功！methodName={}, lockId={}", methodName, lockKey);
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("执行业务逻辑异常！methodName={}, lockId={}", methodName, lockKey, e);
            throw e;
        } finally {
            redisDistributedLock.unLock(lockKey);
        }
    }
}
