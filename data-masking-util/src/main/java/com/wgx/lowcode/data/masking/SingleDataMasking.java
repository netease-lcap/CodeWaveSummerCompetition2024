package com.wgx.lowcode.data.masking;


import com.netease.lowcode.core.annotation.NaslLogic;
import com.wgx.lowcode.utils.CustomDataMasking;

/**
 *所有数据脱敏逻辑如果入参为非法参数或者为空都会返回空白字符串不会返回原字符主要考虑数据安全问题避免泄露
 *当前类主要是用户单条数据进行脱敏
 * @author 19153
 */

public class SingleDataMasking {

    /**
     * 手机号脱敏 支持国内、国际、座机号码 例:183****5376、(+86)13645678906、+8613645678906、0755-1234567、010-1234567
     * @param phone 手机号
     * @return
     */
    @NaslLogic
    public static String mobilePhoneSingleDataMasking(String phone){
        return CustomDataMasking.phoneDesensitization(phone);
    }

    /**
     * 中国姓名脱敏 例:王**
     * @param chineseName  中国姓名
     * @return
     */
    @NaslLogic
    public static String chineseNameSingleDataMasking(String chineseName){
        return CustomDataMasking.chineseName(chineseName);
    }

    /**
     * 身份证号码脱敏 例:410***********0093
     * @param idCardNum 身份证号码
     * @param startSaveLength 需要保留的身份证号码开头的长度
     * @param endSaveLength 需要保留的身份证号码末尾的长度
     * @return
     */
    @NaslLogic
    public static String idCardNumSingleDataMasking(String idCardNum, Integer startSaveLength, Integer endSaveLength){
        return CustomDataMasking.idCardNum(idCardNum,startSaveLength,endSaveLength);
    }

    /**
     * 邮箱脱敏 例:w***********@163.com
     * @param email 邮箱
     * @return
     */
    @NaslLogic
    public static String emailSingleDataMasking(String email){
        return CustomDataMasking.email(email);
    }

    /**
     * 银行卡脱敏 例:6200 **** **** 1785
     * @param bankCard 银行卡
     * @return
     */
    @NaslLogic
    public static String bankCardSingleDataMasking(String bankCard){
        return CustomDataMasking.bankCard(bankCard);
    }

    /**
     * 地址脱敏 例:山东省泰安市************
     * @param address 地址
     * @return
     */
    @NaslLogic
    public static String addressSingleDataMasking(String address){
        return CustomDataMasking.addressDesensitization(address);
    }

    /**
     * 密码脱敏 例:**************
     * @param password 密码
     * @return
     */
    @NaslLogic
    public static String passwordSingleDataMasking(String password){
        return CustomDataMasking.password(password);
    }

    /**
     * ipv4脱敏 例:192.*.*.*
     * @param ipv4 ipv4
     * @return
     */
    @NaslLogic
    public static String ipv4SingleDataMasking(String ipv4){
        return CustomDataMasking.ipv4(ipv4);
    }

    /**
     * ipv6脱敏 例:FC00:*:*:*:*:*:*:*
     * @param ipv6 ipv6
     * @return
     */
    @NaslLogic
    public static String ipv6SingleDataMasking(String ipv6){
        return CustomDataMasking.ipv6(ipv6);
    }


    /**
     * 自定义脱敏 可自定义脱敏内容、开始位置、脱敏长度、脱敏字符
     * @param text 脱敏文本
     * @param start  开始位置
     * @param desensitizationLength  长度
     * @param maskChar 脱敏字符
     * @return
     */
    @NaslLogic
    public static String generalSingleDataMasking(String text, Integer start, Integer desensitizationLength, String maskChar){
        return CustomDataMasking.customDesensitization(text, start-1, desensitizationLength, maskChar);
    }
}
