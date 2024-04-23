package com.wgx.lowcode.util;

import com.netease.lowcode.core.annotation.NaslLogic;
import com.wgx.lowcode.validation.RegexValidatorResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
    public static RegexValidatorResult customValidate(String value, String regex) {
        if (value == null || value.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }

        if (regex == null || regex.isEmpty()) {
            // 提前检查正则表达式是否为空或无效
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "正则表达式不能为空");
        }

        Pattern pattern;
        try {
            // 编译正则表达式
            pattern = Pattern.compile(regex);

        } catch (PatternSyntaxException e) {
            // 处理正则表达式无法编译的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "无效的正则表达式: " + e.getMessage());
        }

        // 创建匹配器
        Matcher matcher = pattern.matcher(value);
        // 进行匹配
        if (matcher.matches()) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则");
        }
    }

    /**
     * 验证手机号码 支持国际和内地格式，+86135xxxx...（中国内地）、13503906547）
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *               <p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *               <p>电信的号段：133、153、180（未启用）、189</p>
     * @return 如果手机号码符合正则表达式，则返回 RegexValidatorResult.TRUE 和相应的消息；否则返回 RegexValidatorResult.FALSE 和相应的消息
     */
    @NaslLogic
    public static RegexValidatorResult checkMobile(String mobile) {
        if (mobile == null || mobile.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "(\\+\\d+)?1[3456789]\\d{9}$";
        // 进行匹配
        if (Pattern.matches(regex, mobile)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则,应输入手机号（包含国内、国外手机号）例如：18303968745、+8618303968745");
        }
    }

    /**
     * 验证身份证号码 居民身份证号码18位，第一位不能为0，最后一位可能是数字或字母，中间16位为数字 \d同[0-9]
     *
     * @param idCard
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static RegexValidatorResult checkIdCard(String idCard) {
        if (idCard == null || idCard.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "[1-9]\\d{16}[a-zA-Z0-9]{1}";
        // 进行匹配
        if (Pattern.matches(regex, idCard)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则,应输入18位居民身份证号码 例如：410802199608130069");
        }
    }

    /**
     * 验证Email格式. 格式： wanggexin@XXX.com XXX为邮件服务商。
     *
     * @param email 传入的字符串
     * @return 符合Email格式返回true，否则返回false
     */
    @NaslLogic
    public static RegexValidatorResult checkEmail(String email) {
        if (email.length() < 1 || email.length() > 256) {
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值长度不合法超过256");
        }

        if (email == null || email.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        // 进行匹配
        if (Pattern.matches(regex, email)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入邮箱 例如：17303945758@163.com");
        }
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static RegexValidatorResult checkPostcode(String postcode) {
        if (postcode == null || postcode.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "[1-9]\\d{5}";
        // 进行匹配
        if (Pattern.matches(regex, postcode)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入邮政编码 例如：454000");
        }
    }

    /**
     * 验证日期（年月日） 格式：1992-09-03，或1992.09.03
     *
     * @param birthday 日期，
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static RegexValidatorResult checkDate(String birthday) {
        if (birthday == null || birthday.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "(\\d{4}[.\\-](0?[13578]|1[02])[.\\-](0?[1-9]|[12]\\d|3[01]))|(\\d{4}[.\\-](0?[469]|11)[.\\-](0?[1-9]|[12]\\d|30))|(\\d{4}[.\\-]0?2[.\\-](0?[1-9]|1\\d|2[0-8]))|(\\d{2}([02468][048]|[13579][26])[.\\-]0?2[.\\-]29)";
        // 进行匹配
        if (Pattern.matches(regex, birthday)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入日期（年月日） 例如：1992-09-03，或1992.09.03");
        }
    }

    /**
     * 验证日期（年月日时分秒） 格式：1992-09-03 12:22:21
     *
     * @param dateTime 日期，
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static RegexValidatorResult checkDateTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "^\\d{4}-(?:0[1-9]|1[0-2])-([0-2]\\d|3[0-1]) (?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
        // 进行匹配
        if (Pattern.matches(regex, dateTime)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入日期时间（年月日时分秒） 例如：1992-09-03 12:22:21");
        }
    }

    /**
     * 验证整数 格式：正整数和负整包括0
     *
     * @param digit
     * @return
     */
    @NaslLogic
    public static RegexValidatorResult checkInterAndZero(String digit) {
        if (digit == null || digit.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "^([1-9]\\d*|[0]{1,1})$";
        // 进行匹配
        if (Pattern.matches(regex, digit)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则,应输入整数（正整数和负整包括0） 例如：-11、18、0");
        }
    }

    /**
     * 验证整数 格式：正整数和负整不包括0
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static RegexValidatorResult checkIntegerNotZero(String digit) {
        if (digit == null || digit.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "\\-?[1-9]\\d+";
        // 进行匹配
        if (Pattern.matches(regex, digit)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入整数（正整数和负整不包括0） 例如：11、-17");
        }
    }

    /**
     * 验证整数和小数（正负整数和正负小数以及0） 格式：一位或多位0-9之间的浮点数，如：1.23，233.30
     *
     * @param digit
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static RegexValidatorResult checkDigit(String digit) {
        if (digit == null || digit.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "-?\\d+(\\.\\d+)?";
        // 进行匹配
        if (Pattern.matches(regex, digit)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入整数和小数（正负整数和正负小数以及0） 例如：89、1.23、233.30");
        }
    }

    /**
     * 验证是否为小数 格式：-1.1，1.1
     *
     * @param decimal 传入的字符串
     * @return 如果是小数，则返回 true；否则返回 false
     */
    @NaslLogic
    public static RegexValidatorResult checkDecimal(String decimal) {
        if (decimal == null || decimal.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(Boolean.FALSE, "输入值不能为空");
        }
        // 正则表达式
        String regex = "^-?\\d+(\\.\\d+)?$";
        // 进行匹配
        if (Pattern.matches(regex, decimal)) {
            // 匹配成功
            return new RegexValidatorResult(Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(Boolean.FALSE, "输入值不符合校验规则");
        }
    }

    /**
     * 验证是否为负的小数 格式：-1.22、-2.3
     *
     * @param decimal 传入的字符串
     * @return 如果是负的小数，则返回 true；否则返回 false
     */
    @NaslLogic
    public static RegexValidatorResult checkNegativeDecimal(String decimal) {
        if (decimal == null || decimal.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(Boolean.FALSE, "输入值不能为空");
        }
        // 正则表达式
        String regex = "^\\-\\d+\\.\\d+$";
        // 进行匹配
        if (Pattern.matches(regex, decimal)) {
            // 匹配成功
            return new RegexValidatorResult(Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(Boolean.FALSE, "输入值不符合校验规则。应输入为负的小数 例如：-1.22、-2.3");
        }
    }

    /**
     * 验证中文 格式：中文字符 例如：王、李
     *
     * @param chinese
     * @return 验证成功返回true，验证失败返回false
     */
    @NaslLogic
    public static RegexValidatorResult checkChinese(String chinese) {
        if (chinese == null || chinese.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值符合校验规则");
        }
        //正则表达式
        String regex = "^[\u4E00-\u9FA5]+$";
        // 进行匹配
        if (Pattern.matches(regex, chinese)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入中文 例如：王、李");
        }
    }

    /**
     * 判断是否是字母 格式：包括大写及小写字母  例如：A、b、C
     *
     * @param letter
     * @return
     */
    @NaslLogic
    public static RegexValidatorResult checkLetter(String letter) {
        if (letter == null || letter.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "[a-z|A-Z]+";
        // 进行匹配
        if (Pattern.matches(regex, letter)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入字母（包括大小写字母） 例如：A、b、C");
        }
    }

    /**
     * 验证MAC地址 格式：01:23:45:67:89:ab或01-23-45-67-89-ab
     *
     * @param macAddress
     * @return
     */
    @NaslLogic
    public static RegexValidatorResult checkMacAddress(String macAddress) {
        if (macAddress == null || macAddress.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "^([0-9A-Fa-f]{1,2}[:-]){5}[0-9a-f]{1,2}$";
        // 进行匹配
        if (Pattern.matches(regex, macAddress)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入MAC地址 例如：01:23:45:67:89:ab或01-23-45-67-89-ab");
        }
    }

    /**
     * 验证IPv4地址 格式：192.168.0.1 每个部分由 0 到 255 之间的数字组成用点隔开。
     *
     * @param ipAddress
     * @return
     */
    @NaslLogic
    public static RegexValidatorResult checkIPv4Address(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        // 进行匹配
        if (Pattern.matches(regex, ipAddress)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入IPv4地址 例如：192.168.0.1");
        }
    }

    /**
     * 验证IPv6地址 格式：2001:0db8:85a3:0000:0000:8a2e:0370:7334 每个部分由 1 到 4 个十六进制数字组成，以冒号分隔，且总共有 8 个部分
     *
     * @param ipv6Address
     * @return
     */
    @NaslLogic
    public static RegexValidatorResult checkIPv6Address(String ipv6Address) {
        if (ipv6Address == null || ipv6Address.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";
        // 进行匹配
        if (Pattern.matches(regex, ipv6Address)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入IPv6地址 例如：2001:0db8:85a3:0000:0000:8a2e:0370:7334");
        }
    }

    /**
     * 验证密码 格式：长度至少为8位，必须包含字母和数字。
     *
     * @param password 要验证的密码
     * @return 如果密码有效，则返回 true；否则返回 false
     */
    @NaslLogic
    public static RegexValidatorResult checkPasswordLength8Easy(String password) {
        if (password == null || password.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,}";
        // 进行匹配
        if (Pattern.matches(regex, password)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入长度至少为8位，必须包含字母和数字的密码 例如：password123");
        }
    }

    /**
     * 验证密码 格式：长度至少为6位，必须包含字母和数字。
     *
     * @param password 要验证的密码
     *                 * @return 如果密码有效，则返回 true；否则返回 false
     */
    @NaslLogic
    public static RegexValidatorResult checkPasswordLength6Easy(String password) {
        if (password == null || password.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}";
        // 进行匹配
        if (Pattern.matches(regex, password)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入长度至少为6位，必须包含字母和数字的密码 例如：passwor");
        }
    }

    /**
     * 验证密码 格式：长度至少为8位，必须包含特殊字符、字母和数字。
     *
     * @param password 要验证的密码
     *  @return 如果密码有效，则返回 true；否则返回 false
     */
    @NaslLogic
    public static RegexValidatorResult checkPasswordLength8Difficult(String password) {
        if (password == null || password.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
        // 进行匹配
        if (Pattern.matches(regex, password)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入长度至少为8位，必须包含特殊字符、字母和数字。 例如：password123!#");
        }
    }

    /**
     * 验证密码 格式：长度至少为6位，必须包含特殊字符、字母和数字。
     *
     * @param password 要验证的密码
     *                 * @return 如果密码有效，则返回 true；否则返回 false
     */

    @NaslLogic
    public static RegexValidatorResult checkPasswordLength6Difficult(String password) {
        if (password == null || password.isEmpty()) {
            // 处理空值或空字符串的情况
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不能为空");
        }
        //正则表达式
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{6,}$";
        // 进行匹配
        if (Pattern.matches(regex, password)) {
            // 匹配成功
            return new RegexValidatorResult(java.lang.Boolean.TRUE, "输入值符合校验规则");
        } else {
            // 匹配失败
            return new RegexValidatorResult(java.lang.Boolean.FALSE, "输入值不符合校验规则，应输入长度至少为6位，必须包含特殊字符、字母和数字。 例如：pa123!#");
        }
    }
}
