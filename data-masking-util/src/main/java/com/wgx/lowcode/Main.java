package com.wgx.lowcode;

import com.wgx.lowcode.data.masking.BulkDataMasking;
import com.wgx.lowcode.utils.CustomDataMasking;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //测试SingleDataMasking
        String mobilePhone = CustomDataMasking.mobilePhone("18312345678");
        String chineseName = CustomDataMasking.chineseName("张三");
        String idCardNum = CustomDataMasking.idCardNum("410123199001011234", 6, 4);
        String email = CustomDataMasking.email("example@example.com");
        String bankCard = CustomDataMasking.bankCard("62220211111111111111");
        String address = CustomDataMasking.address("山东省泰安市泰山区文化路 123 号", 6);
        String password = CustomDataMasking.password("1234567890");
        String ipv4 = CustomDataMasking.ipv4("192.168.0.1");
        String ipv6 = CustomDataMasking.ipv6("FC00:AB:CD:EF:12:34:56:78");
        String customMasking = CustomDataMasking.customDesensitization("这是一个测试文本", 5, 3, "*");

        // 输出测试结果
        System.out.println("手机号脱敏: " + mobilePhone);
        System.out.println("中国姓名脱敏: " + chineseName);
        System.out.println("身份证号码脱敏: " + idCardNum);
        System.out.println("邮箱脱敏: " + email);
        System.out.println("银行卡脱敏: " + bankCard);
        System.out.println("地址脱敏: " + address);
        System.out.println("密码脱敏: " + password);
        System.out.println("IPv4 脱敏: " + ipv4);
        System.out.println("IPv6 脱敏: " + ipv6);
        System.out.println("自定义脱敏: " + customMasking);

        // 测试BulkDataMasking
        List<String> phoneList = Arrays.asList("12345678901", "09876543210");
        List<String> chineseNameList = Arrays.asList("张三", "李四");
        List<String> idCardNumList = Arrays.asList("410101199001011234", "410101199102022345");
        List<String> emailList = Arrays.asList("test@example.com", "another@example.org");
        List<String> bankCardList = Arrays.asList("6222111122334455", "6222222233445566");
        List<String> addressList = Arrays.asList("北京市海淀区中关村南大街 123 号", "上海市浦东新区世纪大道 456 号");
        List<String> passwordList = Arrays.asList("password123", "supersecret");
        List<String> ipv4List = Arrays.asList("192.168.0.1", "10.0.0.1");
        List<String> ipv6List = Arrays.asList("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "2001:0db8:85a3:0000:0000:912e:0370:8324");


        List<String> mobilePhoneBulkDataMasking = BulkDataMasking.mobilePhoneBulkDataMasking(phoneList);
        List<String> chineseNameBulkDataMasking = BulkDataMasking.chineseNameBulkDataMasking(chineseNameList);
        List<String> idCardNumBulkDataMasking = BulkDataMasking.idCardNumBulkDataMasking(idCardNumList, 7, 4);
        List<String> emailBulkDataMasking = BulkDataMasking.emailBulkDataMasking(emailList);
        List<String> bankCardBulkDataMasking = BulkDataMasking.bankCardBulkDataMasking(bankCardList);
        List<String> addressBulkDataMasking = BulkDataMasking.addressBulkDataMasking(addressList, 4);
        List<String> passwordBulkDataMasking = BulkDataMasking.passwordBulkDataMasking(passwordList);
        List<String> ipv4BulkDataMasking = BulkDataMasking.ipv4BulkDataMasking(ipv4List);
        List<String> ipv6BulkDataMasking = BulkDataMasking.ipv6BulkDataMasking(ipv6List);
        List<String> generalBulkDataMasking = BulkDataMasking.generalBulkDataMasking(Arrays.asList("This is a test"), 3, 4, "*");

        // 验证结果
        System.out.println("手机号批量脱敏: " + mobilePhoneBulkDataMasking);
        System.out.println("中国姓名批量脱敏: " + chineseNameBulkDataMasking);
        System.out.println("身份证号码批量脱敏: " + idCardNumBulkDataMasking);
        System.out.println("邮箱批量脱敏: " + emailBulkDataMasking);
        System.out.println("银行卡批量脱敏: " + bankCardBulkDataMasking);
        System.out.println("地址批量脱敏: " + addressBulkDataMasking);
        System.out.println("密码批量脱敏: " + passwordBulkDataMasking);
        System.out.println("IPv4 批量脱敏: " + ipv4BulkDataMasking);
        System.out.println("IPv6 批量脱敏: " + ipv6BulkDataMasking);
        System.out.println("自定义批量脱敏: " + generalBulkDataMasking);
    }
}