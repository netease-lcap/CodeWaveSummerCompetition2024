package com.fdddf.regexvalidateutil.api;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegexValidateUtilApi {

    /**
     * 检查提供的字符串是否是有效的电子邮件地址。
     *
     * @param email 需要检查的电子邮件地址。
     * @return 如果提供的字符串是有效的电子邮件地址，则返回true；否则返回false。
     */
    @NaslLogic
    public static Boolean isEmail(String email) {
        // 使用正则表达式匹配电子邮件地址的格式
        return email.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    }

    /**
     * 判断传入的字符串是否为有效的手机号码。
     *
     * @param mobile 需要验证的手机号码字符串。
     * @return 返回布尔值，如果手机号码有效则返回true，否则返回false。
     */
    @NaslLogic
    public static Boolean isMobile(String mobile) {
        // 使用正则表达式匹配手机号码格式
        return mobile.matches("^1[3456789]\\d{9}$");
    }

    /**
     * 检查一个字符串是否为有效的中国身份证号码。
     *
     * @param idCard 待检查的身份证号码字符串。
     * @return 返回一个布尔值，如果该字符串是有效的身份证号码则返回true，否则返回false。
     */
    @NaslLogic
    public static Boolean isIdCard(String idCard) {
        // 使用正则表达式匹配身份证号码的格式
        return idCard.matches("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
    }

    /**
     * 检查提供的字符串是否为电话号码。
     * 该函数支持两种格式的电话号码：带区号和不带区号。
     * 带区号的电话号码格式为：0xx-xxxxxxxx（x为数字，区号为2-3位，电话号码为5-10位）。
     * 不带区号的电话号码格式为：xxxxxxxxxx（x为数字，电话号码为6-9位）。
     *
     * @param str 需要检查的字符串。
     * @return 如果提供的字符串符合上述任一电话号码格式，则返回true；否则返回false。
     */
    @NaslLogic
    public static Boolean isPhone(final String str) {
        Pattern p1, p2;  // 定义两个正则表达式模式，分别用于匹配带区号和不带区号的电话号码
        Matcher m;       // 匹配器，用于匹配字符串和正则表达式
        boolean b;       // 用于存储匹配结果的布尔值

        // 匹配带区号的电话号码的正则表达式
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");
        // 匹配不带区号的电话号码的正则表达式
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");

        if (str.length() > 9) {
            // 如果字符串长度大于9，认为它可能是带区号的电话号码
            m = p1.matcher(str);
            b = m.matches();
        } else {
            // 否则，认为它可能是不带区号的电话号码
            m = p2.matcher(str);
            b = m.matches();
        }

        return b; // 返回匹配结果
    }

    /**
     * 判断传入的字符串是否为有效的IPv4地址。
     *
     * @param ip 待验证的IP地址字符串。
     * @return 返回{@code Boolean}类型，如果传入的字符串是有效的IPv4地址，则返回{@code true}，否则返回{@code false}。
     */
    @NaslLogic
    public static Boolean isIPv4(String ip) {
        // 使用正则表达式匹配IP地址格式
        return ip.matches("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
    }

    /**
     * 判断传入的字符串是否为URL地址。
     *
     * @param url 待判断的字符串。
     * @return 返回{@code true}如果字符串是URL地址，否则返回{@code false}。
     */
    @NaslLogic
    public static Boolean IsUrl(String url) {
        // 使用正则表达式匹配URL格式
        return url.matches("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");
    }

    /**
     * 判断给定的字符串是否全为中文字符。
     *
     * @param str 需要进行判断的字符串。
     * @return 如果字符串中所有的字符都是中文字符，则返回true；否则返回false。
     */
    @NaslLogic
    public static Boolean IsChinese(String str) {
        // 使用正则表达式匹配字符串，判断是否只包含中文字符
        return str.matches("^[\u4e00-\u9fa5]+$");
    }
}
