package com.fdddf.datamaskingutil.api;

import com.fdddf.datamaskingutil.StrUtil;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.springframework.stereotype.Component;

/**
 * 脱敏工具类，支持以下类型信息的脱敏自动处理：
 *
 * <ul>
 *     <li>中文名</li>
 *     <li>身份证</li>
 *     <li>座机号</li>
 *     <li>手机号</li>
 *     <li>地址</li>
 *     <li>电子邮件</li>
 *     <li>车牌</li>
 *     <li>银行卡号</li>
 * </ul>
 *
 * @author dazer and neusoft and qiaomu
 * @since 5.6.2
 */
@Component
public class DataMaskingUtil {

    /**
     * 定义了一个first_mask的规则，只显示第一个字符。<br>
     * 脱敏前：123456789；脱敏后：1********。
     *
     * @param str 字符串
     * @return 脱敏后的字符串
     */
    @NaslLogic
    public static String firstMask(String str) {
        if (str.isEmpty()) {
            return StrUtil.EMPTY;
        }
        return StrUtil.firstMask(str);
    }

    /**
     * 【中文姓名】只显示第一个汉字，其他隐藏为2个星号，比如：李**
     *
     * @param fullName 姓名
     * @return 脱敏后的姓名
     */
    @NaslLogic
    public static String chineseName(String fullName) {
        return firstMask(fullName);
    }

    /**
     * 【身份证号】前1位 和后2位
     *
     * @param idCardNum 身份证
     * @param front     保留：前面的front位数；从1开始
     * @param end       保留：后面的end位数；从1开始
     * @return 脱敏后的身份证
     */
    @NaslLogic
    public static String idCardNum(String idCardNum, Integer front, Integer end) {
        //身份证不能为空
        if (StrUtil.isBlank(idCardNum)) {
            return StrUtil.EMPTY;
        }
        //需要截取的长度不能大于身份证号长度
        if ((front + end) > idCardNum.length()) {
            return StrUtil.EMPTY;
        }
        //需要截取的不能小于0
        if (front < 0 || end < 0) {
            return StrUtil.EMPTY;
        }
        return StrUtil.hide(idCardNum, front, idCardNum.length() - end);
    }

    /**
     * 【固定电话 前四位，后两位
     *
     * @param num 固定电话
     * @return 脱敏后的固定电话；
     */
    @NaslLogic
    public static String fixedPhone(String num) {
        if (StrUtil.isBlank(num)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.hide(num, 4, num.length() - 2);
    }

    /**
     * 【手机号码】前三位，后4位，其他隐藏，比如135****2210
     *
     * @param num 移动电话；
     * @return 脱敏后的移动电话；
     */
    @NaslLogic
    public static String mobilePhone(String num) {
        if (StrUtil.isBlank(num)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.hide(num, 3, num.length() - 4);
    }

    /**
     * 【地址】只显示到地区，不显示详细地址，比如：北京市海淀区****
     *
     * @param address       家庭住址
     * @param sensitiveSize 敏感信息长度
     * @return 脱敏后的家庭地址
     */
    @NaslLogic
    public static String address(String address, Integer sensitiveSize) {
        if (StrUtil.isBlank(address)) {
            return StrUtil.EMPTY;
        }
        int length = address.length();
        return StrUtil.hide(address, length - sensitiveSize, length);
    }

    /**
     * 【电子邮箱】邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com
     *
     * @param email 邮箱
     * @return 脱敏后的邮箱
     */
    @NaslLogic
    public static String email(String email) {
        if (StrUtil.isBlank(email)) {
            return StrUtil.EMPTY;
        }
        int index = email.indexOf('@');
        if (index <= 1) {
            return email;
        }
        return StrUtil.hide(email, 1, index);
    }

    /**
     * 【中国车牌】车牌中间用*代替
     * eg1：null       -》 ""
     * eg1：""         -》 ""
     * eg3：苏D40000   -》 苏D4***0
     * eg4：陕A12345D  -》 陕A1****D
     * eg5：京A123     -》 京A123     如果是错误的车牌，不处理
     *
     * @param carLicense 完整的车牌号
     * @return 脱敏后的车牌
     */
    @NaslLogic
    public static String carLicense(String carLicense) {
        if (StrUtil.isBlank(carLicense)) {
            return StrUtil.EMPTY;
        }
        // 普通车牌
        if (carLicense.length() == 7) {
            carLicense = StrUtil.hide(carLicense, 3, 6);
        } else if (carLicense.length() == 8) {
            // 新能源车牌
            carLicense = StrUtil.hide(carLicense, 3, 7);
        }
        return carLicense;
    }

    /**
     * 【银行卡号脱敏】由于银行卡号长度不定，所以只展示前4位，后面的位数根据卡号决定展示1-4位
     * 例如：
     * <pre>{@code
     *      1. "1234 2222 3333 4444 6789 9"    ->   "1234 **** **** **** **** 9"
     *      2. "1234 2222 3333 4444 6789 91"   ->   "1234 **** **** **** **** 91"
     *      3. "1234 2222 3333 4444 678"       ->    "1234 **** **** **** 678"
     *      4. "1234 2222 3333 4444 6789"      ->    "1234 **** **** **** 6789"
     *  }</pre>
     *
     * @param bankCardNo 银行卡号
     * @return 脱敏之后的银行卡号
     */
    @NaslLogic
    public static String bankCard(String bankCardNo) {
        if (StrUtil.isBlank(bankCardNo)) {
            return bankCardNo;
        }
        bankCardNo = StrUtil.cleanBlank(bankCardNo);
        if (bankCardNo.length() < 9) {
            return bankCardNo;
        }

        final int length = bankCardNo.length();
        final int endLength = length % 4 == 0 ? 4 : length % 4;
        final int midLength = length - 4 - endLength;

        final StringBuilder buf = new StringBuilder();
        buf.append(bankCardNo, 0, 4);
        for (int i = 0; i < midLength; ++i) {
            if (i % 4 == 0) {
                buf.append(StrUtil.SPACE);
            }
            buf.append('*');
        }
        buf.append(StrUtil.SPACE).append(bankCardNo, length - endLength, length);
        return buf.toString();
    }

    /**
     * IPv4脱敏，如：脱敏前：192.0.2.1；脱敏后：192.*.*.*。
     *
     * @param ipv4 IPv4地址
     * @return 脱敏后的地址
     */
    @NaslLogic
    public static String ipv4(String ipv4) {
        return StrUtil.subBefore(ipv4, ".") + ".*.*.*";
    }

    /**
     * IPv4脱敏，如：脱敏前：2001:0db8:86a3:08d3:1319:8a2e:0370:7344；脱敏后：2001:*:*:*:*:*:*:*
     *
     * @param ipv6 IPv4地址
     * @return 脱敏后的地址
     */
    @NaslLogic
    public static String ipv6(String ipv6) {
        return StrUtil.subBefore(ipv6, ":") + ":*:*:*:*:*:*:*";
    }
}