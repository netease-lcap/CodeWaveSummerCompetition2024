package com.fdddf.regexvalidateutil.api;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidateUtilApi {

    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

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
     * 判断传入的字符串是否为有效的国内手机号码。
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
     * 部分国家手机号码校验正则
     */
    public enum MobileRegularExp {
        CN("中国", "^(\\+?0?86\\-?)?1[3456789]\\d{9}$"),
        TW("台湾", "^(\\+?886\\-?|0)?9\\d{8}$"),
        HK("香港", "^(\\+?852\\-?)?[569]\\d{3}\\-?\\d{4}$"),
        MS("马来西亚", "^(\\+?6?01){1}(([145]{1}(\\-|\\s)?\\d{7,8})|([236789]{1}(\\s|\\-)?\\d{7}))$"),
        PH("菲律宾", "^(\\+?0?63\\-?)?\\d{10}$"),
        TH("泰国", "^(\\+?0?66\\-?)?\\d{10}$"),
        SG("新加坡", "^(\\+?0?65\\-?)?\\d{10}$"),
        /*以下是其他国家的手机号校验正则*/
        DZ("阿尔及利亚", "^(\\+?213|0)(5|6|7)\\d{8}$"),
        SY("叙利亚", "^(!?(\\+?963)|0)?9\\d{8}$"),
        SA("沙特阿拉伯", "^(!?(\\+?966)|0)?5\\d{8}$"),
        US("美国", "^(\\+?1)?[2-9]\\d{2}[2-9](?!11)\\d{6}$"),
        CZ("捷克共和国", "^(\\+?420)? ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$"),
        DE("德国", "^(\\+?49[ \\.\\-])?([\\(]{1}[0-9]{1,6}[\\)])?([0-9 \\.\\-\\/]{3,20})((x|ext|extension)[ ]?[0-9]{1,4})?$"),
        DK("丹麦", "^(\\+?45)?(\\d{8})$"),
        GR("希腊", "^(\\+?30)?(69\\d{8})$"),
        AU("澳大利亚", "^(\\+?61|0)4\\d{8}$"),
        GB("英国", "^(\\+?44|0)7\\d{9}$"),
        CA("加拿大", "^(\\+?1)?[2-9]\\d{2}[2-9](?!11)\\d{6}$"),
        IN("印度", "^(\\+?91|0)?[789]\\d{9}$"),
        NZ("新西兰", "^(\\+?64|0)2\\d{7,9}$"),
        ZA("南非", "^(\\+?27|0)\\d{9}$"),
        ZM("赞比亚", "^(\\+?26)?09[567]\\d{7}$"),
        ES("西班牙", "^(\\+?34)?(6\\d{1}|7[1234])\\d{7}$"),
        FI("芬兰", "^(\\+?358|0)\\s?(4(0|1|2|4|5)?|50)\\s?(\\d\\s?){4,8}\\d$"),
        FR("法国", "^(\\+?33|0)[67]\\d{8}$"),
        IL("以色列", "^(\\+972|0)([23489]|5[0248]|77)[1-9]\\d{6}"),
        HU("匈牙利", "^(\\+?36)(20|30|70)\\d{7}$"),
        IT("意大利", "^(\\+?39)?\\s?3\\d{2} ?\\d{6,7}$"),
        JP("日本", "^(\\+?81|0)\\d{1,4}[ \\-]?\\d{1,4}[ \\-]?\\d{4}$"),
        NO("挪威", "^(\\+?47)?[49]\\d{7}$"),
        BE("比利时", "^(\\+?32|0)4?\\d{8}$"),
        PL("波兰", "^(\\+?48)? ?[5-8]\\d ?\\d{3} ?\\d{2} ?\\d{2}$"),
        BR("巴西", "^(\\+?55|0)\\-?[1-9]{2}\\-?[2-9]{1}\\d{3,4}\\-?\\d{4}$"),
        PT("葡萄牙", "^(\\+?351)?9[1236]\\d{7}$"),
        RU("俄罗斯", "^(\\+?7|8)?9\\d{9}$"),
        RS("塞尔维亚", "^(\\+3816|06)[- \\d]{5,9}$"),
        TR("土耳其", "^(\\+?90|0)?5\\d{9}$"),
        VN("越南", "^(\\+?84|0)?((1(2([0-9])|6([2-9])|88|99))|(9((?!5)[0-9])))([0-9]{7})$"),
        /* 以下是匹配所有手机号校验正则*/
        OT("所有", "^(\\+?0)?([0-9]*[1-9][0-9]*)$");

        /**
         * 国际名称
         */
        private final String national;

        /**
         * 正则表达式
         */
        private final String regularExp;

        MobileRegularExp(String national, String regularExp) {
            this.national = national;
            this.regularExp = regularExp;
        }

        public String getNational() {
            return national;
        }

        public String getRegularExp() {
            return regularExp;
        }
    }

    /**
     * 检查一个字符串是否为有效的手机号码
     *
     * @param mobile 待检查的手机号码字符串
     * @param countryCode 国家代码，目前支持的国家代码有：CN,TW,HK,MS,PH,TH,SG,DZ,SY,
     *                    SA,US,CZ,DE,DK,GR,AU,GB,CA,IN,NZ,ZA,ZM,ES,FI,FR,IL,HU,
     *                    IT,JP,NO,BE,PL,BR,PT,RU,RS,TR,VN
     *                    全部国家为OT
     * @return Boolean
     */
    public static Boolean isMobilePhone(String mobile, String countryCode) {
        boolean isMobileNumber = false;
        try {
            MobileRegularExp regularExp = MobileRegularExp.valueOf(countryCode);
            isMobileNumber = mobile.matches(regularExp.getRegularExp());
        } catch (IllegalArgumentException e) {
            if (countryCode.equals("OT")) {
                MobileRegularExp regularExp = MobileRegularExp.OT;
                isMobileNumber = mobile.matches(regularExp.getRegularExp());
            } else {
                logger.error("Unsupported country code：" + countryCode);
            }
        }
        return isMobileNumber;
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
     * 检查提供的字符串是否为电话号码
     * 该函数支持两种格式的电话号码：带区号和不带区号
     *
     * @param str 需要检查的字符串。
     * @return Boolean
     */
    @NaslLogic
    public static Boolean isPhone(final String str, Boolean withAreaCode) {
        Pattern p1, p2;  // 定义两个正则表达式模式，分别用于匹配带区号和不带区号的电话号码
        Matcher m;       // 匹配器，用于匹配字符串和正则表达式
        boolean b;       // 用于存储匹配结果的布尔值

        // 匹配带区号的电话号码的正则表达式
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");
        // 匹配不带区号的电话号码的正则表达式
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");

        if (str.length() > 9 && withAreaCode) {
            // 如果字符串长度大于9，认为它可能是带区号的电话号码
            m = p1.matcher(str);
        } else {
            // 否则，认为它可能是不带区号的电话号码
            m = p2.matcher(str);
        }
        b = m.matches();

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
        return str.matches("^[\\u4e00-\\u9fa5]+$");
    }
}
