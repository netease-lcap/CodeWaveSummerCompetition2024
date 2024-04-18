package com.wgx.aop;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Aspect
@Component
@Order(1)
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Value("${isLoggingEnabled}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV,value = "true"),
            @Environment(type = EnvironmentType.ONLINE,value = "false")
    })
    private boolean isLoggingEnabled;

    //切点匹配类上有@Controller、@RestController注解的类下的所有方法
    @Pointcut("within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {}

    @Around("controller()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!isLoggingEnabled) {
            return joinPoint.proceed();
        }

        long startTime = System.currentTimeMillis();

        // Ensure the current request attributes are available.
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String requestURL = (request != null) ? request.getRequestURL().toString() : "N/A";
        String requestIP = (request != null) ? request.getRemoteAddr() : "N/A";

        Object[] args = joinPoint.getArgs();

        Object result = null;
        boolean exceptionThrown = false;

        try {
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            exceptionThrown = true;
            logger.error("Error in {}#{} - IP: {} - URL: {} - Args: {} - Exception: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    requestIP,
                    requestURL,
                    args,
                    ex.toString());
            logger.error("Exception stack trace:", ex);
            throw ex; // rethrow the exception
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            if (!exceptionThrown) {
                logger.info("Completed {}#{} - IP: {} - URL: {} - Args: {} - Result: {} - Duration: {} ms",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        requestIP,
                        requestURL,
                        args,
                        result,
                        executionTime);
            }
        }
        return result;
    }
}

