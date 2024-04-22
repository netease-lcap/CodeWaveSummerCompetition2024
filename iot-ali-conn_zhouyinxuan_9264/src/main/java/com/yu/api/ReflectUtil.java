package com.yu.api;

import com.yu.annotation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/21 14:09
 **/
public class ReflectUtil {
    private static final Logger log = LoggerFactory.getLogger(IotAliConnector.class);

    public static <T> void validRequire(T instance) {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Validate.class)) {
                Validate validateAnno = field.getAnnotation(Validate.class);
                if (validateAnno.required()) {
                    Object value = null;
                    try {
                        value = field.get(instance);
                    } catch (IllegalAccessException e) {
                        log.error(instance.getClass() + "反射调用出错：", e);
                        throw new RuntimeException(e);
                    }
                    if (value == null) {
                        String msg = "属性" + field.getName() + "不得为空";
                        log.error("属性{}不得为空", field.getName());
                        throw new IllegalArgumentException(msg);
                    }
                }
            }
        }

    }

}
