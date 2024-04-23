package com.wgx;

import com.wgx.lowcode.validation.RegexValidatorResult;

import static com.wgx.lowcode.util.RegexUtils.*;

/**
 * @Date: ${DATE} - ${MONTH} - ${DAY} - ${TIME}
 * @Description: com.netease
 * @version: 1.0
 */
public class Main {
    public static void main(String[] args) {
        // 测试自定义校验规则
        String value = "123";
        String regex = "\\d+";
        RegexValidatorResult customResult = customValidate(value, regex);
        System.out.println("自定义校验结果: " + customResult.isIsValid() + ", 消息: " + customResult.getErrorMessage());

        // 测试手机号码校验
        String mobile = "+8613512345678";
        RegexValidatorResult mobileResult = checkMobile(mobile);
        System.out.println("手机号码校验结果: " + mobileResult.isIsValid() + ", 消息: " + mobileResult.getErrorMessage());

        // 测试身份证号码校验
        String idCard = "110101199001011234";
        RegexValidatorResult idCardResult = checkIdCard(idCard);
        System.out.println("身份证号码校验结果: " + idCardResult.isIsValid() + ", 消息: " + idCardResult.getErrorMessage());

        // 测试电子邮件校验
        String email = "example@example.com";
        RegexValidatorResult emailResult = checkEmail(email);
        System.out.println("电子邮件校验结果: " + emailResult.isIsValid() + ", 消息: " + emailResult.getErrorMessage());

        // 测试邮政编码校验
        String postcode = "100000";
        RegexValidatorResult postcodeResult = checkPostcode(postcode);
        System.out.println("邮政编码校验结果: " + postcodeResult.isIsValid() + ", 消息: " + postcodeResult.getErrorMessage());

        // 测试日期校验
        String date = "1992-09-03";
        RegexValidatorResult dateResult = checkDate(date);
        System.out.println("日期校验结果: " + dateResult.isIsValid() + ", 消息: " + dateResult.getErrorMessage());

        // 测试日期时间校验
        String dateTime = "1992-09-03 12:22:21";
        RegexValidatorResult dataTimeResult = checkDateTime(dateTime);
        System.out.println("日期时间校验结果: " + dataTimeResult.isIsValid() + ", 消息: " + dataTimeResult.getErrorMessage());

        // 测试整数校验（包括 0）
        String digit = "0";
        RegexValidatorResult digitResult = checkInterAndZero(digit);
        System.out.println("整数校验（包括 0）结果: " + digitResult.isIsValid() + ", 消息: " + digitResult.getErrorMessage());

        // 测试整数校验（不包括 0）
        digit = "123";
        digitResult = checkIntegerNotZero(digit);
        System.out.println("整数校验（不包括 0）结果: " + digitResult.isIsValid() + ", 消息: " + digitResult.getErrorMessage());

        // 测试数字校验
        String decimal = "1.23";
        digitResult = checkDigit(decimal);
        System.out.println("小数校验结果: " + digitResult.isIsValid() + ", 消息: " + digitResult.getErrorMessage());

        // 测试小数校验
        decimal = "-1.1";
        digitResult = checkDecimal(decimal);
        System.out.println("小数校验结果: " + digitResult.isIsValid() + ", 消息: " + digitResult.getErrorMessage());

        // 测试负小数
        decimal = "-1.22";
        digitResult = checkNegativeDecimal(decimal);
        System.out.println("负小数校验结果: " + digitResult.isIsValid() + ", 消息: " + digitResult.getErrorMessage());

        // 测试中文
        String chinese = "王";
        RegexValidatorResult chineseResult = checkChinese(chinese);
        System.out.println("中文校验结果: " + chineseResult.isIsValid() + ", 消息: " + chineseResult.getErrorMessage());

        // 测试字母校验
        String letter = "abc";
        RegexValidatorResult checkLetterResult = checkLetter(letter);
        System.out.println("字母校验结果: " + checkLetterResult.isIsValid() + ", 消息: " + checkLetterResult.getErrorMessage());

        // 测试 MAC 地址校验
        String macAddress = "01:23:45:67:89:ab";
        RegexValidatorResult checkMacAddressResult =checkMacAddress(macAddress);
        System.out.println("MAC 地址校验结果: " + checkMacAddressResult.isIsValid() + ", 消息: " + checkMacAddressResult.getErrorMessage());

        // 测试 IPv4 地址校验
        String ipAddress = "192.168.0.1";
        RegexValidatorResult checkIPv4AddressResult = checkIPv4Address(ipAddress);
        System.out.println("IPv4 地址校验结果: " + checkIPv4AddressResult.isIsValid() + ", 消息: " + checkIPv4AddressResult.getErrorMessage());

        // 测试密码长度至少为 8 且较简单的校验
        String password1 = "pass1234";
        RegexValidatorResult checkPasswordLength8EasyResult = checkPasswordLength8Easy(password1);
        System.out.println("密码长度为 8 且较简单的校验结果: " + checkPasswordLength8EasyResult.isIsValid() + ", 消息: " + checkPasswordLength8EasyResult.getErrorMessage());

        // 测试密码长度至少为 6 且较简单的校验
        String password2 = "Password123";
        RegexValidatorResult checkPasswordLength6EasyResult = checkPasswordLength6Easy(password2);
        System.out.println("密码长度为 6 且较简单的校验结果: " + checkPasswordLength6EasyResult.isIsValid() + ", 消息: " + checkPasswordLength6EasyResult.getErrorMessage());

        // 测试密码长度至少为 8 且较困难的校验
        String password3 = "password123!#";
        RegexValidatorResult checkPasswordLength8DifficultResult = checkPasswordLength8Difficult(password3);
        System.out.println("密码长度为 8 且较困难的校验结果: " + checkPasswordLength8DifficultResult.isIsValid() + ", 消息: " + checkPasswordLength8DifficultResult.getErrorMessage());

        //测试密码长度至少为 6 且较困难的校验
        String password4 = "Password123@";
        RegexValidatorResult checkPasswordLength6DifficultResult = checkPasswordLength6Difficult(password4);
        System.out.println("密码长度为 6 且较困难的校验结果: " + checkPasswordLength6DifficultResult.isIsValid() + ", 消息: " + checkPasswordLength6DifficultResult.getErrorMessage());

        //测试日期
        System.out.println(checkDate("1992-02-28"));
        System.out.println(checkDate("1992.09.03"));
        System.out.println(checkDate("1992-9-3"));
        System.out.println(checkDate("1992.09.2"));
    }
}