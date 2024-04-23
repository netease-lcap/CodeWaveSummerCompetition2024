package com.wgx.lowcode;

import com.wgx.lowcode.data.masking.BulkDataMasking;
import com.wgx.lowcode.data.masking.SingleDataMasking;
import com.wgx.lowcode.utils.CustomDataMasking;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wgx.lowcode.data.masking.SingleDataMasking.addressSingleDataMasking;
import static com.wgx.lowcode.data.masking.SingleDataMasking.mobilePhoneSingleDataMasking;


public class Main {
    public static void main(String[] args) {

        String address1 = "北京市";
        String address2 = "上海市浦东新区";
        String address3 = "盟";
        String address4 = "内蒙古自治区";
        String address5 = "云南省昆明市呈贡区";
        String address6 = "云南省昆明市官渡区";
        String address7 = "黑龙江省哈尔滨市";
        String address8 = "吉林省四平市梨树县";
        String address9 = "新疆维吾尔自治区";
        String address10 = "河北省";
        String address11 = "广西壮族自治区桂林市阳朔县";
        String address12 = "广西壮族自治区桂林市";
        String address13 = "广西桂林市";
        String address14 = "桂林市";
        String address15 = "广东省广州市天河区";
        String address16 = "广州市天河区";
        String address17 = "广州市";
        String address18 = "天河区";
        String address19 = "广东广州";
        String address20 = "广东";
        String address21 = "广州";
        String address22 = "广西";
        String address23 = "桂林";
        String address24 = "北京朝阳区";
        String address25 = "朝阳区";
        String address26 = "北京海淀区";
        String address27 = "海淀区";
        String address28 = "朝阳区";
        String address29 = "海淀区";
        String address30 = "北京";
        String address31 = "海淀";
        String address32 = "朝阳";
        String address33 = "区";
        String address34 = "海";
        String address35 = "广西桂林";
        String address36 = "中国";

        // 测试
        System.out.println(addressSingleDataMasking(address1));
        System.out.println(addressSingleDataMasking(address2));
        System.out.println(addressSingleDataMasking(address3));
        System.out.println(addressSingleDataMasking(address4));
        System.out.println(addressSingleDataMasking(address5));
        System.out.println(addressSingleDataMasking(address6));
        System.out.println(addressSingleDataMasking(address7));
        System.out.println(addressSingleDataMasking(address8));
        System.out.println(addressSingleDataMasking(address9));
        System.out.println(addressSingleDataMasking(address10));
        System.out.println(addressSingleDataMasking(address11));
        System.out.println(addressSingleDataMasking(address12));
        System.out.println(addressSingleDataMasking(address13));
        System.out.println(addressSingleDataMasking(address14));
        System.out.println(addressSingleDataMasking(address15));
        System.out.println(addressSingleDataMasking(address16));
        System.out.println(addressSingleDataMasking(address17));
        System.out.println(addressSingleDataMasking(address18));
        System.out.println(addressSingleDataMasking(address19));
        System.out.println(addressSingleDataMasking(address20));
        System.out.println(addressSingleDataMasking(address21));
        System.out.println(addressSingleDataMasking(address22));
        System.out.println(addressSingleDataMasking(address23));
        System.out.println(addressSingleDataMasking(address24));
        System.out.println(addressSingleDataMasking(address25));
        System.out.println(addressSingleDataMasking(address26));
        System.out.println(addressSingleDataMasking(address27));
        System.out.println(addressSingleDataMasking(address28));
        System.out.println(addressSingleDataMasking(address29));
        System.out.println(addressSingleDataMasking(address30));
        System.out.println(addressSingleDataMasking(address31));
        System.out.println(addressSingleDataMasking(address32));
        System.out.println(addressSingleDataMasking(address33));
        System.out.println(addressSingleDataMasking(address34));
        System.out.println(addressSingleDataMasking(address35));
        System.out.println(addressSingleDataMasking(address36));

        String phoneNumber1 = "13185217412";
        String phoneNumber2 = "131 8521 7412";
        String phoneNumber3 = "(+86)13645678906";
        String phoneNumber4 = "0755-1234567";
        String phoneNumber5 = "010-12345678";
        String phoneNumber6 = "010-12345678";

        System.out.println("原号码: " + phoneNumber1 + ", 脱敏后: " + mobilePhoneSingleDataMasking(phoneNumber1));
        System.out.println("原号码: " + phoneNumber2 + ", 脱敏后: " + mobilePhoneSingleDataMasking(phoneNumber2));
        System.out.println("原号码: " + phoneNumber3 + ", 脱敏后: " + mobilePhoneSingleDataMasking(phoneNumber3));
        System.out.println("原号码: " + phoneNumber4 + ", 脱敏后: " + mobilePhoneSingleDataMasking(phoneNumber4));
        System.out.println("原号码: " + phoneNumber5 + ", 脱敏后: " + mobilePhoneSingleDataMasking(phoneNumber5));
        System.out.println("原号码: " + phoneNumber6 + ", 脱敏后: " + mobilePhoneSingleDataMasking(phoneNumber6));

        //测试SingleDataMasking
        String chineseName = CustomDataMasking.chineseName("张三");
        String idCardNum = CustomDataMasking.idCardNum("410123199001011234", 6, 4);
        String email = CustomDataMasking.email("example@example.com");
        String bankCard = CustomDataMasking.bankCard("62220211111111111111");
        String address = addressSingleDataMasking("山东省泰安市泰山区文化路 123 号");
        String password = CustomDataMasking.password("1234567890");
        String ipv4 = CustomDataMasking.ipv4("192.168.0.1");
        String ipv6 = CustomDataMasking.ipv6("FC00:AB:CD:EF:12:34:56:78");
        String customMasking = CustomDataMasking.customDesensitization("这是一个测试文本", 5, 3, "*");

        // 输出测试结果
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
        List<String> phoneList = Arrays.asList("12345678901", "19876543210");
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
        List<String> addressBulkDataMasking = BulkDataMasking.addressBulkDataMasking(addressList);
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