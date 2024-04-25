package com.fdddf.StringUtils.api;


import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static final String AlphaNumeric = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Logger log = LoggerFactory.getLogger(StringUtils.class);

    /**
     * 生成指定长度的随机字符串,字符串仅包含大小写字母和数字
     *
     * @param len 指定生成字符串的长度。必须为非负整数
     * @return 生成的随机字符串
     */
    @NaslLogic
    public static String generateRandomString(Integer len) throws IllegalArgumentException {
        if (len == null || len <= 0) {
            log.error("length should greater than zero");
            throw new IllegalArgumentException("length should greater than zero");
        }
        StringBuilder sb = new StringBuilder();
        // 为字符串生成指定数量的随机字符
        for (int i = 0; i < len; i++) {
            int randomNum = (int) (Math.random() * AlphaNumeric.length());
            char randomChar = AlphaNumeric.charAt(randomNum);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    /**
     * 使用自定义字符集，生成长度为length的随机字符串
     *
     * @param len             随机字符串的长度
     * @param CustomCharacterSet 定义随机字符集，生成的随机字符串将从这个字符集中抽取
     * @return 生成的随机字符串
     * @throws IllegalArgumentException 当length小于等于0时抛出此异常
     */
    @NaslLogic
    public static String generateRandomStringCustom(Integer len, String CustomCharacterSet) throws IllegalArgumentException {
        if (len == null || len <= 0) {
            log.error("length should greater than zero");
            throw new IllegalArgumentException("length should greater than zero");
        }
        if (CustomCharacterSet == null || CustomCharacterSet.isEmpty()) {
            log.error("CustomCharacterSet should not be empty");
            throw new IllegalArgumentException("CustomCharacterSet should not be empty");
        }
        StringBuilder sb = new StringBuilder();
        // 循环生成指定长度的随机字符
        for (int i = 0; i < len; i++) {
            // 从自定义字符集中随机选择一个字符并添加到StringBuilder中
            sb.append(CustomCharacterSet.charAt((int) (Math.random() * CustomCharacterSet.length())));
        }
        return sb.toString();
    }

    /**
     * 生成指定长度的随机字符串
     * 字符串仅包含大小写字母和数字，且相邻字符不能相同
     *
     * @param len 指定生成字符串的长度。必须为非负整数
     * @return 生成的随机字符串
     * @throws IllegalArgumentException 如果length小于等于0，则抛出IllegalArgumentException异常
     */
    @NaslLogic
    public static String generateNonConsecutiveRandomString(Integer len) throws IllegalArgumentException {
        if (len == null || len <= 0) {
            log.error("length should greater than zero");
            throw new IllegalArgumentException("length should greater than zero");
        }
        StringBuilder sb = new StringBuilder();
        char[] allowedChars = AlphaNumeric.toCharArray();

        // 为字符串生成指定数量的随机字符，确保相邻字符不相同
        for (int i = 0; i < len; i++) {
            char randomChar;
            do {
                randomChar = allowedChars[(int) (Math.random() * allowedChars.length)];
            } while (i > 0 && randomChar == sb.charAt(i - 1)); // 若当前字符与前一字符相同，重新生成

            sb.append(randomChar);
        }

        return sb.toString();
    }


    /**
     * 返回原始字符串中匹配给定子字符串后的剩余部分
     * 如果找不到匹配项，则返回原始字符串
     *
     * @param origin 原始字符串
     * @param needle 要匹配的子字符串
     * @return 匹配给定子字符串后的剩余部分，或原始字符串（若未找到匹配项）
     * @throws IllegalArgumentException 如果原始字符串或要匹配的子字符串为空
     */
    @NaslLogic
    public static String subStringAfter(String origin, String needle) throws IllegalArgumentException {
        // 增强输入验证
        if (origin == null || needle == null) {
            log.error("origin and needle should not be null");
            throw new IllegalArgumentException("origin and needle should not be null");
        }

        int index = origin.indexOf(needle);
        if (index != -1) {
            return origin.substring(index + needle.length());
        } else {
            return origin;
        }
    }

    /**
     * 返回原始字符串中匹配给定子字符串之前的剩余部分
     * 如果找不到匹配项，则返回原始字符串
     *
     * @param origin 原始字符串
     * @param needle 要匹配的子字符串
     * @return 匹配给定子字符串之前的剩余部分，或原始字符串（若未找到匹配项）
     * @throws IllegalArgumentException 如果原始字符串或要匹配的子字符串为空
     */
    @NaslLogic
    public static String subStringBefore(String origin, String needle) throws IllegalArgumentException {
        // 增强输入验证
        if (origin == null || needle == null) {
            log.error("origin and needle should not be null");
            throw new IllegalArgumentException("origin and needle should not be null");
        }

        int index = origin.indexOf(needle);
        if (index != -1) {
            return origin.substring(0, index);
        } else {
            return origin;
        }
    }


    /**
     * 移除给定HTML字符串中的所有HTML标签
     *
     * @param html 带有HTML标签的字符串
     * @return 移除HTML标签后的纯文本字符串
     * @throws IllegalArgumentException 如果HTML字符串为空
     */
    @NaslLogic
    public static String stripHtml(String html) throws IllegalArgumentException {
        if (html == null || html.isEmpty()) {
            log.error("html should not be empty");
            throw new IllegalArgumentException("html should not be empty");
        }
        Document doc = Jsoup.parse(html, "", Parser.xmlParser());
        return doc.text();
    }


    /**
     * 判断给定字符串是否是一个有效的数字
     * 支持整数和浮点数，包括正负号、小数点和科学计数法
     *
     * @param str 待判断的字符串
     * @return 若字符串表示一个有效数字，返回true；否则返回false
     * @throws IllegalArgumentException 如果输入字符串为空
     */
    @NaslLogic
    public static Boolean isNumber(String str) throws IllegalArgumentException {
        // 使用正则表达式匹配数字模式
        // 允许可选的正负号，小数点和指数部分
        if (str == null || str.isEmpty()) {
            log.error("str should not be empty");
            throw new IllegalArgumentException("str should not be empty");
        }
        String pattern = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        // 返回是否匹配
        return m.matches();
    }


    /**
     * 反转给定字符串中的字符顺序
     *
     * @param str 待反转的字符串
     * @return 反转后的字符串
     */
    @NaslLogic
    public static String reverseString(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        return sb.toString();
    }

}
