package com.fdddf.regexvalidateutil;

import com.fdddf.regexvalidateutil.api.RegexValidateUtilApi;

public class Main {
    public static void main(String[] args) {
        System.out.println(RegexValidateUtilApi.isIdCard("410203198906283309"));
        System.out.println(RegexValidateUtilApi.isEmail("abc@163.com"));
        System.out.println(RegexValidateUtilApi.isMobile("13521456789"));
        System.out.println(RegexValidateUtilApi.isPhone("0917-3385212", true));
        System.out.println(RegexValidateUtilApi.isPhone("3385212", false));
        System.out.println(RegexValidateUtilApi.isMobilePhone("+86-16691850000", "CN"));
        System.out.println(RegexValidateUtilApi.IsUrl("https://www.baidu.com"));
        System.out.println(RegexValidateUtilApi.IsChinese("你好"));
        System.out.println(RegexValidateUtilApi.isIPv4("192.168.1.1"));
    }
}
