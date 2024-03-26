package com.codewave.masking.api;

import com.netease.lowcode.core.annotation.NaslLogic;

public class DataMaskingApi {

    /**
     * 将dataMaskingContent字符串进行脱敏，，从前往后进行脱敏，保留holdLength对应位数明文，按*格式进行脱敏，固定脱敏长度为1
     * @param dataMaskingContent 待脱敏数据
     * @param holdLength 保留位数
     * @return 脱敏字符串, 异常情况返回null
     */
    @NaslLogic
    public static String simplePrefixMasking(String dataMaskingContent, Integer holdLength) {
        return prefixMasking(dataMaskingContent, holdLength, "*", 1);
    }


    /**
     * 将dataMaskingContent字符串进行脱敏，从后往前进行脱敏，保留holdLength对应位数明文，按*格式进行脱敏，固定脱敏长度为1
     * @param dataMaskingContent 待脱敏数据
     * @param holdLength 保留位数
     * @return 脱敏字符串, 异常情况返回null
     */
    @NaslLogic
    public static String simpleSuffixMasking(String dataMaskingContent, Integer holdLength) {
        return suffixMasking(dataMaskingContent, holdLength, "*", 1);
    }

    /**
     * 将dataMaskingContent字符串进行中间脱敏，按*格式进行脱敏，固定脱敏长度为1
     * @param dataMaskingContent 待脱敏数据
     * @param prefixHoldLength 前面保留位数
     * @param suffixHoldLength 后面保留位数
     * @return 脱敏字符串， 异常情况返回null
     */
    @NaslLogic
    public static String simpleMiddleMasking(String dataMaskingContent,  Integer prefixHoldLength, Integer suffixHoldLength) {
        return middleSuffixMasking(dataMaskingContent, prefixHoldLength, suffixHoldLength, "*", 1);
    }
    /**
     * 将字符串的前面数字进行脱敏，从前往后进行脱敏，保留holdLength对应位数明文，按maskingChar格式进行脱敏，
     * fixedMaskingLength固定脱敏字符长度； -1表示按照实际脱敏长度（最短长度为1），正数代表：固定脱敏字符
     * @param dataMaskingContent 待脱敏数据
     * @param holdLength 保留位数
     * @param maskingChar 脱敏字符
     * @param fixedMaskingLength 固定脱敏字符长度； -1表示按照实际脱敏长度，正数代表：固定脱敏字符
     * @return 脱敏字符串, 异常情况返回null
     */
    @NaslLogic
    public static String prefixMasking(String dataMaskingContent, Integer holdLength, String maskingChar, Integer fixedMaskingLength) {
        if(dataMaskingContent == null || dataMaskingContent.length() == 0){
            return null;
        }
        if(holdLength < 0 || holdLength > dataMaskingContent.length()){
            return null;
        }
        if(maskingChar == null || maskingChar.length() == 0){
            maskingChar = "*";
        }
        StringBuilder maskingContent = getMaskingContent(dataMaskingContent, holdLength, maskingChar, fixedMaskingLength);
        return maskingContent.append(dataMaskingContent.substring(dataMaskingContent.length() - holdLength)).toString();
    }


    /**
     * 对字符串后面进行脱敏，从后往前对holdLength位数进行脱敏，按maskingChar格式进行脱敏
     * fixedMaskingLength固定脱敏字符长度； -1表示按照实际脱敏长度（最小长度为1），正数代表：固定脱敏字符
     * @param dataMaskingContent 待脱敏数据
     * @param holdLength 保留位数
     * @param maskingChar 脱敏字符
     * @param fixedMaskingLength 固定脱敏字符长度； -1表示按照实际脱敏长度，正数代表：固定脱敏字符
     * @return 脱敏字符串, 异常情况返回null
     */
    @NaslLogic
    public static String suffixMasking(String dataMaskingContent, Integer holdLength, String maskingChar, Integer fixedMaskingLength) {
        if(dataMaskingContent == null || dataMaskingContent.length() == 0){
            return null;
        }
        if(holdLength < 0 || holdLength > dataMaskingContent.length()){
            return null;
        }
        if(maskingChar == null || maskingChar.length() == 0){
            maskingChar = "*";
        }
        StringBuilder maskingContent = getMaskingContent(dataMaskingContent, holdLength, maskingChar, fixedMaskingLength);

        return  dataMaskingContent.substring(0, holdLength) + maskingContent.toString();
    }

    private static StringBuilder getMaskingContent(String dataMaskingContent, Integer holdLength, String maskingChar, Integer fixedMaskingLength) {
        final int maskingLength = dataMaskingContent.length() - holdLength == 0 ? 1 : dataMaskingContent.length() - holdLength ;
        StringBuilder maskingContent = new StringBuilder();
        // 不固定
        for (int index = 0; index < (fixedMaskingLength == -1 ? maskingLength : fixedMaskingLength) ; index++) {
            maskingContent.append(maskingChar);
        }
        return maskingContent;
    }

    /**
     * 将字符串按中间进行脱敏，当(prefixHoldLength + suffixHoldLength > dataMaskingContent.length) 时，当前方法优先保证后面明文保留位数
     * fixedMaskingLength固定脱敏字符长度； -1表示按照实际脱敏长度（最小长度为1），正数代表：固定脱敏字符
     * @param dataMaskingContent 待脱敏数据
     * @param prefixHoldLength 前面保留位数
     * @param suffixHoldLength 后面保留位数
     * @param maskingChar 脱敏字符
     * @param fixedMaskingLength 固定脱敏字符长度； -1表示按照实际脱敏长度，正数代表：固定脱敏字符
     * @return 脱敏字符串, 异常情况返回null
     */
    @NaslLogic
    public static String middleSuffixMasking(String dataMaskingContent, Integer prefixHoldLength, Integer suffixHoldLength, String maskingChar, Integer fixedMaskingLength) {
        if(dataMaskingContent == null || dataMaskingContent.length() == 0){
            return null;
        }
        if(prefixHoldLength < 0 || prefixHoldLength > dataMaskingContent.length()){
            return null;
        }
        if(suffixHoldLength < 0 || suffixHoldLength > dataMaskingContent.length()){
            return null;
        }
        StringBuilder maskingContent = getMaskingContent(dataMaskingContent, prefixHoldLength + suffixHoldLength, maskingChar, fixedMaskingLength);
        if(prefixHoldLength + suffixHoldLength > dataMaskingContent.length()){
            prefixHoldLength = dataMaskingContent.length() - suffixHoldLength;
        }

        return dataMaskingContent.substring(0, prefixHoldLength) + maskingContent + dataMaskingContent.substring(dataMaskingContent.length() - suffixHoldLength);
    }


}
