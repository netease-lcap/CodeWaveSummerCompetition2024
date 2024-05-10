package com.yu.connector;


import com.yu.annotation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/23 10:27
 **/
public class Util {
    private static final Logger log = LoggerFactory.getLogger(Util.class);

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

    public static String getName(String filePath) {
        if (null == filePath) {
            return null;
        }
        int len = filePath.length();
        if (0 == len) {
            return filePath;
        }
        if (isFileSeparator(filePath.charAt(len - 1))) {
            // 以分隔符结尾的去掉结尾分隔符
            len--;
        }

        int begin = 0;
        char c;
        for (int i = len - 1; i > -1; i--) {
            c = filePath.charAt(i);
            if (isFileSeparator(c)) {
                // 查找最后一个路径分隔符（/或者\）
                begin = i + 1;
                break;
            }
        }

        return filePath.substring(begin, len);
    }

    public static boolean isFileSeparator(char c) {
        return '/' == c || '\\' == c;
    }

    /**
     * 对中文字符进行UTF-8编码
     *
     * @param source 要转义的字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String tranformStyle(String source) throws UnsupportedEncodingException {
        char[] arr = source.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            char temp = arr[i];
            if (isChinese(temp)) {
                sb.append(URLEncoder.encode("" + temp, "UTF-8"));
                continue;
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * 判断是不是中文字符
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
