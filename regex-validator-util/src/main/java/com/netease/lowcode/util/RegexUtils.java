package com.netease.lowcode.util;

import com.netease.lowcode.core.annotation.NaslLogic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 19153
 * @Date: 2024/3/7 - 03 - 07 - 20:22
 * @Description: com.netease.lowcode.util
 * @version: 1.0
 * 正则工具类
 */
public class RegexUtils {

    /**
     * 自定义校验规则 验证输入的值是否符合给定的正则表达式
     *
     * @param value 待验证的输入字符串
     * @param regex 用于验证的正则表达式
     * @return 如果输入符合正则表达式，则返回 true；否则返回 false
     */
    @NaslLogic
    public static Boolean customValidate(String value, String regex) {
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        // 创建匹配器
        Matcher matcher = pattern.matcher(value);
        // 进行匹配
        return matcher.matches();
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *               <p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *               <p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static Boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[3456789]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证身份证号码 居民身份证号码18位，第一位不能为0，最后一位可能是数字或字母，中间16位为数字 \d同[0-9]
     *
     * @param idCard
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static Boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    /**
     * 验证Email格式. 格式： wanggexin@XXX.com XXX为邮件服务商。
     *
     * @param email 传入的字符串
     * @return 符合Email格式返回true，否则返回false
     */
    @NaslLogic
    public static Boolean isEmail(String email) {
        if (email == null || email.length() < 1 || email.length() > 256) {
            return false;
        }
        String regex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        return Pattern.matches(regex,email);
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static Boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * 验证日期（年月日） 格式：1992-09-03，或1992.09.03
     *
     * @param birthday 日期，
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static Boolean checkDate(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }

    /**
     * 验证日期（年月日时分秒） 格式：1992-09-03 12:22:21
     *
     * @param birthday 日期，
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static Boolean checkDateTime(String birthday) {
        String regex = "^\\d{4}-(?:0[1-9]|1[0-2])-([0-2]\\d|3[0-1]) (?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
        return Pattern.matches(regex, birthday);
    }

    /**
     * 验证整数 格式：正整数和负整数
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static Boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex, digit);
    }

    /**
     * 验证整数 格式：0以及正整数
     * @param num
     * @return
     */
    @NaslLogic
    public static Boolean validateInterAndZero(String num) {
        String reg = "^([1-9]\\d*|[0]{1,1})$";
        return num.matches(reg);
    }

    /**
     * 验证整数 格式：包括整数和负数以及0
     *
     * @param value 传入的字符串
     * @autor:chenssy
     */
    @NaslLogic
    public static Boolean isInteger(String value) {
        String regex = "^[-\\+]?[\\d]+$";
        return Pattern.matches(regex,value);
    }

    /**
     * 验证整数和小数（正负整数和正负小数） 格式：一位或多位0-9之间的浮点数，如：1.23，233.30
     *
     * @param decimals
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static Boolean checkDecimals(String decimals) {
        String regex = "-?\\d+(\\.\\d+)?";
        return Pattern.matches(regex, decimals);
    }

    /**
     * 判断是否为正的小数
     *
     * @param value 传入的字符串
     * @return 如果是正的小数，则返回 true；否则返回 false
     */
    @NaslLogic
    public static Boolean isPositiveDouble(String value) {
        String regex = "^\\+\\d+\\.\\d+$";
        return Pattern.matches(regex, value);
    }

    /**
     * 验证中文 格式：中文字符 例如：王、李
     *
     * @param chinese
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static Boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, chinese);
    }

    /**
     * 判断是否是字母 格式：包括大写及小写字母
     *
     * @param value
     * @return
     */
    @NaslLogic
    public static Boolean checkChar(String value) {
        String regex = "[a-z|A-Z]+";
        return Pattern.matches(regex,value);

    }

    /**
     * 验证空白字符 格式：空白字符，包括：空格、\t、\n、\r、\f、\x0B
     *
     * @param blankSpace
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static Boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex, blankSpace);
    }

    /**
     * 验证MAC地址 格式：01:23:45:67:89:ab或01-23-45-67-89-ab
     * @param macAddress
     * @return
     */
    @NaslLogic
    public static Boolean isValidMacAddress(String macAddress) {
        String regex = "^([0-9A-Fa-f]{1,2}[:-]){5}[0-9a-f]{1,2}$";
        return Pattern.matches(regex, macAddress);
    }

    /**
     * 验证IPv4地址 格式：192.168.0.1 每个部分由 0 到 255 之间的数字组成用点隔开。
     * @param ipAddress
     * @return
     */
    @NaslLogic
    public static Boolean isValidIPv4(String ipAddress) {
        String regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return Pattern.matches(regex, ipAddress);
    }

    /**
     * 验证IPv6地址 格式：2001:0db8:85a3:0000:0000:8a2e:0370:7334 每个部分由 1 到 4 个十六进制数字组成，以冒号分隔，且总共有 8 个部分
     * @param ipv6Address
     * @return
     */
    @NaslLogic
    public static Boolean isValidIPv6(String ipv6Address) {
        String regex = "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";
        return Pattern.matches(regex, ipv6Address);
    }

    /**
     * 判断字符串是否符合正则表达式
     *
     * @param str
     * @param regex
     * @return
     */
    @NaslLogic
    public static Boolean find(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        boolean b = m.find();
        return b;
    }

    /**
     * 验证密码 格式：长度至少为8位，必须包含字母和数字。
     *  @param password 要验证的密码
     *  * @return 如果密码有效，则返回 true；否则返回 false
     */
    @NaslLogic
    public static Boolean isValidPasswordLength8Easy(String password) {
        String regex = "(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,}";
        return Pattern.matches(regex, password);
    }

    /**
     * 验证密码 格式：长度至少为6位，必须包含字母和数字。
     *  @param password 要验证的密码
     *  * @return 如果密码有效，则返回 true；否则返回 false
     */
    @NaslLogic
    public static Boolean isValidPasswordLength6Easy(String password) {
        String regex = "(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}";
        return Pattern.matches(regex, password);
    }

    /**
     * 验证密码 格式：长度至少为8位，必须包含特殊字符、字母和数字。
     *  @param password 要验证的密码
     *  * @return 如果密码有效，则返回 true；否则返回 false
     */
    @NaslLogic
    public static Boolean isValidPasswordLength8Difficult(String password) {
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%])(?=.*[A-Z]).{8,}$";
        return Pattern.matches(regex, password);
    }

    /**
     * 验证密码 格式：长度至少为6位，必须包含特殊字符、字母和数字。
     *  @param password 要验证的密码
     *  * @return 如果密码有效，则返回 true；否则返回 false
     */

    @NaslLogic
    public static Boolean isValidPasswordLength6Difficult(String password) {
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%])(?=.*[A-Z]).{6,}$";
        return Pattern.matches(regex, password);
    }
}
