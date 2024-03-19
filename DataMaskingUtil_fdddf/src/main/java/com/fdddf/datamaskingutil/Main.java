package com.fdddf.datamaskingutil;

import com.fdddf.datamaskingutil.api.DataMaskingUtil;

public class Main {
    public static void main(String[] args) {
        System.out.println(DataMaskingUtil.email("godisgod@126.com"));
        System.out.println(DataMaskingUtil.mobilePhone("13512341234"));
        System.out.println(DataMaskingUtil.address("上海市普陀区金沙江路 1518 弄", 5));
        System.out.println(DataMaskingUtil.carLicense("沪A891234"));
        System.out.println(DataMaskingUtil.idCardNum("310101199001010001", 4, 4));
        System.out.println(DataMaskingUtil.bankCard("1234 2222 3333 4444 6789 91"));
        System.out.println(DataMaskingUtil.ipv4("192.168.1.1"));
        System.out.println(DataMaskingUtil.ipv6("2001:0db8:85a3:0000:0000:8a2e:0370:7334"));
        System.out.println(DataMaskingUtil.fixedPhone("021-88888888"));
        System.out.println(DataMaskingUtil.firstMask("1234567890"));
        System.out.println(DataMaskingUtil.chineseName("张三强"));
    }
}
