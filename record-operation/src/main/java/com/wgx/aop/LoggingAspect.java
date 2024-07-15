package com.wgx.aop;

import com.alibaba.fastjson.JSON;
import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import com.wgx.logics.DataWriter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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

    @Autowired
    private ApplicationContext applicationContext;

    //切点匹配类上有@Controller、@RestController注解的类下的所有方法
    @Pointcut("within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {}

    @Around("controller()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!Boolean.TRUE.equals(loggingEnabled) || shouldLog(joinPoint)) {
            return joinPoint.proceed();
        }

        Object recordService = null;
        Method saveLog = null;
        try {
            recordService = applicationContext.getBean("saveLogOverriddenRecord_operationCustomizeService");
            saveLog = recordService.getClass().getMethod("saveLogOverriddenRecord_operation", String.class);
        } catch (NoSuchBeanDefinitionException e) {
            // do nothing
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
                String msg = MessageFormat.format("Error in {0}#{1} - IP: {2} - URL: {3} - Args: {4} - Exception: {5}",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        requestIP,
                        requestURL,
                        serializeArgs(args),
                        ex.toString());
                logger.error("Error in {}#{} - IP: {} - URL: {} - Args: {} - Exception: {}",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        requestIP,
                        requestURL,
                        serializeArgs(args),
                        ex.toString());
                logger.error("Exception stack trace:", ex);
                DataWriter.invoke(recordService, saveLog, msg);
            }
            throw ex; // rethrow the exception
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            if (!exceptionThrown) {
                if ("simple".equals(loggingFormat)) {
                    String msg = MessageFormat.format("Completed {0}#{} - IP: {1} - URL: {2} - Duration: {3} ms",
                            joinPoint.getSignature().getDeclaringTypeName(),
                            joinPoint.getSignature().getName(),
                            requestIP,
                            requestURL,
                            executionTime);
                    logger.info("Completed {}#{} - IP: {} - URL: {} - Duration: {} ms",
                            joinPoint.getSignature().getDeclaringTypeName(),
                            joinPoint.getSignature().getName(),
                            requestIP,
                            requestURL,
                            executionTime);
                    DataWriter.invoke(recordService, saveLog, msg);
                }else if ("detailed".equals(loggingFormat)) {
                    String msg = MessageFormat.format("Completed {0}#{1} - IP: {2} - URL: {3} - Args: {4} - Result: {5} - Duration: {6} ms",
                            joinPoint.getSignature().getDeclaringTypeName(),
                            joinPoint.getSignature().getName(),
                            requestIP,
                            requestURL,
                            serializeArgs(args),
                            serializeObject(result),
                            executionTime);
                    logger.info("Completed {}#{} - IP: {} - URL: {} - Args: {} - Result: {} - Duration: {} ms",
                            joinPoint.getSignature().getDeclaringTypeName(),
                            joinPoint.getSignature().getName(),
                            requestIP,
                            requestURL,
                            serializeArgs(args),
                            serializeObject(result),
                            executionTime);
                    DataWriter.invoke(recordService, saveLog, msg);
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

    private String serializeArgs(Object[] args) {
        if (Objects.nonNull(args)) {
            List<Object> objList = new ArrayList<>();
            Arrays.asList(args).forEach(obj -> {
                if (obj instanceof HttpServletRequest || obj instanceof HttpServletResponse) {
                    return;
                }
                if (obj instanceof List) {
                    List<?> argsObjList = (List<?>) obj;
                    if (Objects.nonNull(argsObjList) && !argsObjList.isEmpty() && argsObjList.get(0) instanceof MultipartFile) {
                        return;
                    }
                }
                objList.add(obj);
            });

            return JSON.toJSONString(objList.toArray());
        }
        return JSON.toJSONString(args);
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

