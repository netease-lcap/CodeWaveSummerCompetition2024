package com.moocsk.lowcode.baidu.translate.util;

public class StringUtil {

    /**
     * @param value 需要判断的字符串
     * @return 返回字符串的实际长度
     */
    public static int getStringLengthByByte(String value) {
        if (value == null || "".equals(value)) {
            return 0;
        }
        int valueLength = 0;
        for (int i = 0; i < value.length(); i++) {
            char temp = value.charAt(i);
            if ((temp + "").getBytes().length == 1) {
                valueLength += 1;
            } else {
                valueLength += 2;
            }
        }
        return valueLength;
    }

}
