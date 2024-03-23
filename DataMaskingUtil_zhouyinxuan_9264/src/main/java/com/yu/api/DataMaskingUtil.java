package com.yu.api;

import com.netease.lowcode.core.annotation.NaslLogic;

import java.util.Random;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/3/23 14:15
 **/
public class DataMaskingUtil {
    /**
     * 全部数据进行脱敏，随机生成1-5个*  防止知道数据长度
     *
     * @param value 要脱敏的字符串
     * @return
     */
    @NaslLogic
    public static String allReplace(String value) {
        int j = new Random().nextInt(5) + 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < j; i++) {
            sb.append("*");
        }
        value = sb.toString();
        return value;
    }

    /**
     * 指定区间进行数据脱敏
     *
     * @param value 要脱敏的字符串
     * @param start 开始位置 从1开始计算
     * @param end   结束位置 从1开始计算
     * @return 脱敏后的数据
     */
    @NaslLogic
    public static String partReplace(String value, Integer start, Integer end) {
        if (isEmpty(value)) throw new IllegalArgumentException("字符串为空，不需要进行脱敏");
        else if (start <= 0 || start >= value.length()) {
            throw new IllegalArgumentException("start值传入不规范,start大于0且小于字符串长度");
        } else if (end >= value.length() || end < start) {
            throw new IllegalArgumentException("end值传入不规范，end大于start且小于字符串长度");
        }
        char[] array = value.toCharArray();
        char[] resArray = new char[value.length()];
        for (int i = 0; i < array.length; i++) {
            if (i >= start - 1 && i <= end - 1)
                resArray[i] = '*';
            else resArray[i] = array[i];
        }
        return new String(resArray);
    }

    public static boolean isEmpty(String str) {
        if (str == null) return true;
        else if ("".equals(str.trim())) return true;
        return false;

    }

    /**
     * 正则脱敏，满足正则表达式的部分进行脱敏
     *
     * @param value 要脱敏的字符串
     * @param regex 正则表达式
     * @return
     */
    @NaslLogic
    public static String regexReplace(String value, String regex) {
        if (isEmpty(value)) throw new IllegalArgumentException("字符串为空，无法进行正则脱敏");
        return value.replaceAll(regex, "*");
    }


    public static void main(String[] args) {
        String str = "18379459130";
        System.out.println(allReplace(str));
        System.out.println(partReplace(str, 0, 7));
        System.out.println(regexReplace("yeexunsdasdsyeexunslsls", "[yeexun]"));

    }

}
