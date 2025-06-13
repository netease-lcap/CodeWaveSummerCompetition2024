package com.netease.lowcode.lib.annotation;

import com.netease.lowcode.annotation.helper.provider.OverriddenFrameworkHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class ReconfirmAuthorizationAspect {

    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Resource
    public ApplicationContext applicationContext;

    @Around("@annotation(reconfirmAuthorization)") // 拦截所有带有@ReconfirmAuthorization注解的方法
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, ReconfirmAuthorization reconfirmAuthorization) throws Throwable {
        // 执行检查逻辑
        Boolean flag = true;
        if (this.applicationContext.containsBean("reconfirmAuthorizationOverriddenLcap_process_extCustomizeService")) {
            Object[] args = {joinPoint.getSignature().getName()};
            try {
                flag = (Boolean) OverriddenFrameworkHelper.invokeOverriddenMethod0("reconfirmAuthorization", "lcap-process-ext", args);
            } catch (Exception e) {
                log.error("reconfirmAuthorizationOverriddenLcap_process_extCustomizeService error", e);
                flag = false;
            }
        }
        if (!flag) {
            throw new RuntimeException(joinPoint.getSignature().getName() + "资源点reconfirmAuthorization二次鉴权失败");
        }
        // 如果检查通过，继续执行原方法
        return joinPoint.proceed();
    }
}