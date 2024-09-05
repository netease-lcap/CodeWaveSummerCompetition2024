package com.wgx.lowcode.utils;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;

import java.util.regex.Pattern;

/**
 * 自定义脱敏工具类
 */
public class CustomDataMasking extends DesensitizedUtil {

    /**
     * 自定义脱敏
     *
     * @param text       内容
     * @param startIndex 从第几位开始
     * @param length     脱敏几位
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


    /**
     * 地址脱敏
     * @param address
     * @return
     */
    public static String addressDesensitization(String address) {
        if (StrUtil.isBlank(address)) {
            return "";
        } else {
            String addressDesensitized;
            if (StrUtils.strStr(address, "盟") >= 0) { // 1.盟
                addressDesensitized = StrUtil.hide(address, StrUtils.strStr(address, "盟") + 1, address.length());
            } else if (StrUtils.strStr(address, "自治州") >= 0) { // 2.自治州
                addressDesensitized = StrUtil.hide(address, StrUtils.strStr(address, "自治州") + 1, address.length());
            } else if (StrUtils.strStr(address, "地区") >= 0) { // 3.地区
                addressDesensitized = StrUtil.hide(address, StrUtils.strStr(address, "地区") + 1, address.length());
            } else if (StrUtils.strStr(address, "市") >= 0) { // 4.市
                boolean isBeijing = address.contains("北京"); // 4-1.排除直辖市
                boolean isShanghai = address.contains("上海");
                boolean isTianjin = address.contains("天津");
                boolean isChongqing = address.contains("重庆");
                if (!isBeijing && !isShanghai && !isTianjin && !isChongqing) { // 4-2.地级市
                    addressDesensitized = StrUtil.hide(address, StrUtils.strStr(address, "市") + 1, address.length());
                } else { // 4-3.直辖市查区县
                    int indexDistrictOrCounty; // 直辖市的区或者县
                    if (StrUtils.strStr(address, "区") >= 0) {
                        indexDistrictOrCounty = StrUtils.strStr(address, "区");
                    } else if (StrUtils.strStr(address, "县") >= 0) {
                        indexDistrictOrCounty = StrUtils.strStr(address, "县");
                    } else {
                        indexDistrictOrCounty = 3; // 从下标3的字符开始脱敏，直辖市都是三个字的
                    }
                    addressDesensitized = StrUtil.hide(address, indexDistrictOrCounty + 1, address.length());
                }
            } else { // 5.判断长度是否超过三位如没有则脱敏2/3
                if(address.length()<=3){
                    addressDesensitized = address;
                    return addressDesensitized;
                }
                addressDesensitized = StrUtil.hide(address, address.length() / 3, address.length());
            }
            return addressDesensitized;
        }
    }

    /**
     * 手机号脱敏
     * @param phoneNumber
     * @return
     */
    public static String phoneDesensitization(String phoneNumber) {
        // 判断入参是否为空，若为空则返回空字符串
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return "";
        }
        // 去掉前后的空格
        phoneNumber = phoneNumber.trim();
        // 定义正则表达式模式
        // 匹配以1开头的11位数字手机号 格式如：13185217412 脱敏后为：131****7412
        Pattern pattern1 = Pattern.compile("^1[2-9]\\d{9}$");
        // 匹配以1开头的带区号的手机号，格式如：131 8521 7412 或 131-8521-7412 脱敏后为：131 **** 7412 或 131-****-7412
        Pattern pattern2 = Pattern.compile("^1[2-9]\\d{1}[-\\s]\\d{4}[-\\s]\\d{4}$");
        // 匹配国际格式的手机号，如：(+86)13645678906 或 +8613645678906 脱敏后为： (+86)136****8906 或 +86136****8906
        Pattern pattern3 = Pattern.compile("^(?:\\(\\+\\d{2}\\)|\\+\\d{2})(\\d{11})$");
        // 匹配以0开头的带四位区号的座机号，格式如：0755-1234567 脱敏后为：0755-****567
        Pattern pattern4 = Pattern.compile("^0\\d{3}-\\d{7}$");
        // 匹配以0开头的带三位区号的座机号，格式如：010-12345678 脱敏后为：010-****5678
        Pattern pattern5 = Pattern.compile("^0\\d{2}-\\d{8}$");

        // 进行正则匹配
        if (pattern1.matcher(phoneNumber).matches()) {
            return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7);
        } else if (pattern2.matcher(phoneNumber).matches()) {
            return phoneNumber.substring(0, 4) + "****" + phoneNumber.substring(8);
        }  else if (pattern3.matcher(phoneNumber).matches()) {
            boolean hasLeftParenthesis = false;
            for (char c : phoneNumber.toCharArray()) {
                if (c == '(' || c == '（') {
                    hasLeftParenthesis = true;
                    break;
                }
            }
            if (hasLeftParenthesis) {
                return phoneNumber.substring(0, 8) + "****" + phoneNumber.substring(12);
            }
            return phoneNumber.substring(0, 6) + "****" + phoneNumber.substring(7);
        } else if (pattern4.matcher(phoneNumber).matches()) {
            return phoneNumber.substring(0, 5) + "****" + phoneNumber.substring(9);
        } else if (pattern5.matcher(phoneNumber).matches()) {
            return phoneNumber.substring(0, 4) + "****" + phoneNumber.substring(8);
        } else {
            return "";
        }
    }

}
