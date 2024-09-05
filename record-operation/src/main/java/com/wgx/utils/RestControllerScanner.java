package com.wgx.utils;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RestControllerScanner {

    private final ApplicationContext applicationContext;

    @Autowired
    public RestControllerScanner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取Spring容器中所有带有@RestController注解的类的名称，并以List<String>形式返回。
     *
     * @return List containing names of all classes annotated with @RestController
     */
    @NaslLogic
    public List<String> getRestControllerNames() {
        ListableBeanFactory beanFactory = (ListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        Map<String, Object> restControllers = beanFactory.getBeansWithAnnotation(RestController.class);

        List<String> controllerNames = new ArrayList<>();
        for (Map.Entry<String, Object> entry : restControllers.entrySet()) {
            Object bean = entry.getValue();
            Class<?> beanClass = bean.getClass();
            controllerNames.add(beanClass.getName());
        }

        return controllerNames;
    }
}