package com.wgx.aop;

import com.alibaba.fastjson.JSON;
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

    public static final String CONTROLLER = "Controller";
    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    //true开启打印日志、false关闭打印日志
    @Value("${loggingEnabled}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV,value = "true"),
            @Environment(type = EnvironmentType.ONLINE,value = "false")
    })
    private boolean loggingEnabled;

    //simple简易格式、detailed详细格式、error异常格式 注意：null或空字符以及其他字符则不会打印日志
    @Value("${loggingFormat}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV,value = "detailed"),
            @Environment(type = EnvironmentType.ONLINE,value = "simple")
    })
    private String loggingFormat;

    // 需要拦截打印日志的控制类：all表示拦截所有控制类,如果为null或空字符串则不会拦截任何控制类，
    // 若需要拦截特定的控制类格式为 例如：logic1,logic2。注意：需要使用英文逗号为分隔符
    @Value("${loggingClassNames}")
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV,value = "all"),
            @Environment(type = EnvironmentType.ONLINE,value = "all")
    })
    private String loggingClassNames;

    //切点匹配类上有@Controller、@RestController注解的类下的所有方法
    @Pointcut("within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {}

    @Around("controller()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!Boolean.TRUE.equals(loggingEnabled) || shouldLog(joinPoint)) {
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
            if ("error".equals(loggingFormat)) {
                logger.error("Error in {}#{} - IP: {} - URL: {} - Args: {} - Exception: {}",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        requestIP,
                        requestURL,
                        serializeObject(args),
                        ex.toString());
                logger.error("Exception stack trace:", ex);
            }
            throw ex; // rethrow the exception
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            if (!exceptionThrown) {
                if ("simple".equals(loggingFormat)) {
                    logger.info("Completed {}#{} - IP: {} - URL: {} - Duration: {} ms",
                            joinPoint.getSignature().getDeclaringTypeName(),
                            joinPoint.getSignature().getName(),
                            requestIP,
                            requestURL,
                            executionTime);
                }else if ("detailed".equals(loggingFormat)) {
                    logger.info("Completed {}#{} - IP: {} - URL: {} - Args: {} - Result: {} - Duration: {} ms",
                            joinPoint.getSignature().getDeclaringTypeName(),
                            joinPoint.getSignature().getName(),
                            requestIP,
                            requestURL,
                            serializeObject(args),
                            serializeObject(result),
                            executionTime);
                }
            }
        }
        return result;
    }

    private boolean shouldLog(ProceedingJoinPoint joinPoint) {
        if (loggingClassNames == null || loggingClassNames.isEmpty()) {
            return true;
        }

        String[] split = joinPoint.getSignature().getDeclaringTypeName().split("\\.");
        //String lastElement = split[split.length - 1];
        String lastElement = split[split.length - 1] + CONTROLLER;

        if ("all".equals(loggingClassNames)) {
            // 直接判断目标类名是否为SystemTaskController，如果是，则返回true表示不应记录日志
            if ("SystemTaskController".equals(lastElement)) {
                return true;
            }
            return false;
        }

        String[] classNameList = loggingClassNames.split(",");
        for (String className : classNameList) {
            if (className.trim().equals(lastElement)) {
                return false;
            }
        }
        return true;
    }

    private String serializeObject(Object obj) {
        try {
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            logger.error("Error serializing object to JSON", e);
            return "Error";
        }
    }
}

