package com.netease.lowcode.xxljob.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * spring上下文工具类
 *
 * @author xujianping
 */
@Component("xxlJobLibrarySpringContextUtil")
public class SpringContextUtil implements ApplicationContextAware {

    private static final String SUFFIX_BEAN_NAME = "CustomizeService";
    private static final String SUFFIX_BEAN_NAME_4X = "LogicService";
    private static final String SUFFIX_BEAN_NAME_4X_OVERRIDDEN = "OverriddenLogicLibService";
    private static final List<String> SUFFIX_BEAN_NAME_4X_LIST = Arrays.asList("LogicService"
            , "LogicLibService"
            , "OverriddenLogicLibService"
            , "AuthroizationService"
            , "AuthorizationConnectorService"
            , "AuthenticationService"
            , "AuthenticationConnectorService"
            , "EntityService"
            , "ConnectorService"
            , "ProcessService"
            , "DownloadFileService");
    private static ApplicationContext applicationContext;

    /**
     * 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 通过namePrex获取custom service Bean.
     */
    public static Object getCustomServiceBean(String namePrex) {
        // 获取所有@Service注解的Bean
        Map<String, Object> serviceBeans = getApplicationContext().getBeansWithAnnotation(Service.class);
        if (serviceBeans.isEmpty()) {
            return null;
        }
        Map<String, Object> targetServiceBeans = serviceBeans.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(namePrex))
                .filter(entry -> {
                    String endStr = entry.getKey().replace(namePrex, "");
                    return SUFFIX_BEAN_NAME.equals(endStr) || SUFFIX_BEAN_NAME_4X.equals(endStr);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return !targetServiceBeans.isEmpty() ? targetServiceBeans.values().iterator().next() : null;
    }

    /**
     * 通过namePrex获取custom service Bean.
     */
    public static Object getCommonServiceBean(String namePrex) {
        // 获取所有@Service注解的Bean
        Map<String, Object> serviceBeans = getApplicationContext().getBeansWithAnnotation(Service.class);
        if (serviceBeans.isEmpty()) {
            return null;
        }
        Map<String, Object> targetServiceBeans = serviceBeans.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(namePrex))
                .filter(entry -> {
                    String endStr = entry.getKey().replace(namePrex, "");
                    if (SUFFIX_BEAN_NAME.equals(endStr)) {
                        return true;
                    }
                    return SUFFIX_BEAN_NAME_4X_LIST.stream()
                            .anyMatch(suffix -> suffix.equals(endStr));
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return !targetServiceBeans.isEmpty() ? targetServiceBeans.values().iterator().next() : null;
    }

    /**
     * 通过namePrex获取custom service Bean.
     */
    public static Object getCustomOverriddenServiceBean(String namePrex) {
        // 获取所有@Service注解的Bean
        Map<String, Object> serviceBeans = getApplicationContext().getBeansWithAnnotation(Service.class);
        if (serviceBeans.isEmpty()) {
            return null;
        }
        Map<String, Object> targetServiceBeans = serviceBeans.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(namePrex))
                .filter(entry -> {
                    String endStr = entry.getKey().replace(namePrex, "");
                    return SUFFIX_BEAN_NAME_4X_OVERRIDDEN.equals(endStr);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return !targetServiceBeans.isEmpty() ? targetServiceBeans.values().iterator().next() : null;
    }

    /**
     * 通过name获取Bean.
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

}