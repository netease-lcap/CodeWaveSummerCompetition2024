package newease.lowcode.utils;

import cn.hutool.core.util.DesensitizedUtil;

/**
 * 自定义脱敏工具类
 */
public class CustomDataMaskingUtil extends DesensitizedUtil {

    /**
     * 自定义脱敏
     * @param text 内容
     * @param startIndex 从第几位开始
     * @param length 脱敏几位
     * @param maskString 脱敏字符
     * @return
     */
    public static String customDesensitization(String text, int startIndex, int length, String maskString) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        int textLength = text.length();
        if (startIndex < 0 || startIndex >= textLength || length <= 0 || startIndex + length > textLength) {
            // 起始位置或长度不合法，无法脱敏，直接返回空白字符串
            return "";
        }

        char[] chars = text.toCharArray();
        int maskLength = maskString.length();
        for (int i = startIndex; i < startIndex + length && i < textLength; i++) {
            chars[i] = maskString.charAt((i - startIndex) % maskLength); // Repeats maskString if it's shorter
        }
        return new String(chars);
    }

}
