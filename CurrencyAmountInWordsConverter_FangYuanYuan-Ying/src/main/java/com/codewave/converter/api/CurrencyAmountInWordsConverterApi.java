package com.codewave.converter.api;

import com.netease.lowcode.core.annotation.NaslLogic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

import static com.codewave.converter.api.CurrencyAmountInWordsConverterConstant.*;


public class CurrencyAmountInWordsConverterApi {


    /**
     * 将人民币金额转换为中文大写形式，最多支持到”仟兆“，最多满足最后2位小数点转换，超过2位小数点自动四舍五入保留最后2位小数点
     * @param rmbDecimalStringValue 符合人民币格式的字符串
     * @return 中文大写，如传入参数不符合符合型则返回空字符串
     */
    @NaslLogic
    public static String convertRMBToChinese(String rmbDecimalStringValue) {
        Long number = getLongNumber(rmbDecimalStringValue);
        if(number == null) {
            return "";
        }
        System.out.println(number);
        // 超过18位返回“”
        if(String.valueOf(number).length() >= 18){
            return "";
        }
        StringBuilder numberBuilder = new StringBuilder();
        boolean valueZeroFlag = false;
        if(number == 0L){
            numberBuilder.append(CN_UPPER_NUMBER[0]).append(CN_UPPER_MONETARY_UNIT[2]);
            valueZeroFlag = true;
        }
        // 3. 对number进行10求余、当余数不为0 100.01，则记录对应的大写和单位，其中单位是按位数来、依次是CN_UPPER_MONETARY_UNIT，当不存在角和分则记录为整
        int unitIndex = 1;
        boolean yuanZeroFlag = false;
        boolean zeroFlag = false;
        // 第一个约束，当第三位为0时，则需要拼接一个元； 第二个约束：在前面字符没有零时，可以追加零但是在3，7.11.15上不添加零；
        while(number != 0L){
            long n = (long) number % 10;
            // 第一个约束
            if(n == 0 && unitIndex == 3){
                yuanZeroFlag = true;
            }
            if(n == 0){
                // 第3位、第7位、11位、15位不追加0，其他位置判断前面一位是否为0，如果不为0则输入0
                if (!Arrays.asList(3,7,11,15).contains(unitIndex) &&
                        numberBuilder.length() != 0
                        && !zeroFlag) {
                    numberBuilder.insert(0, CN_UPPER_NUMBER[0]);
                    zeroFlag = true;
                }

            } else {
                numberBuilder.insert(0, CN_UPPER_MONETARY_UNIT[unitIndex-1]);
                numberBuilder.insert(0, CN_UPPER_NUMBER[(int)n]);
                zeroFlag = false;
            }
            number = number / 10;
            unitIndex++;
        }
        // 添加元
        if(yuanZeroFlag){
            // (有角有分)
            if(numberBuilder.toString().contains(CN_UPPER_MONETARY_UNIT[0]) && numberBuilder.toString().contains(CN_UPPER_MONETARY_UNIT[1])){
                numberBuilder.insert(numberBuilder.length() - 4, CN_UPPER_MONETARY_UNIT[2]);
            }
            // 有角无分
            else if(!numberBuilder.toString().contains(CN_UPPER_MONETARY_UNIT[0]) && numberBuilder.toString().contains(CN_UPPER_MONETARY_UNIT[1])){
                numberBuilder.insert(numberBuilder.length() - 2, CN_UPPER_MONETARY_UNIT[2]);
            }
            // 有分无角
            else if(numberBuilder.toString().contains(CN_UPPER_MONETARY_UNIT[0]) && !numberBuilder.toString().contains(CN_UPPER_MONETARY_UNIT[1])){
                numberBuilder.insert(numberBuilder.length() - 3, CN_UPPER_MONETARY_UNIT[2]);
            }
            // 无角无分
            else if(!numberBuilder.toString().contains(CN_UPPER_MONETARY_UNIT[0]) && !numberBuilder.toString().contains(CN_UPPER_MONETARY_UNIT[1])){
                numberBuilder.insert(numberBuilder.length() , CN_UPPER_MONETARY_UNIT[2]);
            }
        }
        // 添加负数
        if(rmbDecimalStringValue.startsWith("-") && !valueZeroFlag){
            numberBuilder.insert(0, CN_NEGATIVE);
        }
        return numberBuilder.toString();
    }

    private static Long getLongNumber(String rmbDecimalStringValue) {
        BigDecimal amount = getBigDecimalNumber(rmbDecimalStringValue);
        if(amount == null){
            return null;
        }
        return amount.movePointRight(2).setScale(0, RoundingMode.HALF_UP).abs().longValue();
    }


    private static BigDecimal getBigDecimalNumber(String rmbDecimalStringValue) {
        // 1. 将string转decimal
        if(rmbDecimalStringValue == null || rmbDecimalStringValue.length() == 0){
            return null;
        }
        BigDecimal decimalNumber;
        try{
            decimalNumber = new BigDecimal(rmbDecimalStringValue);
        } catch (Exception e){
            return null;
        }
        // 2. 转成保留2位小数点，采用四舍五入计算法,转为long进行处理 2.0->200; 2->200, 0.00/0 -> 0
        return decimalNumber.setScale(2, RoundingMode.HALF_UP);
    }


    /**
     * 将美元金额转换为英文大写形式，最多满足100亿以下的数字转换，最多满足最后2位小数点转换，超过2位小数点自动四舍五入保留最后2位小数点
     */
    @NaslLogic
    public static String convertUSDToWords(String usdDecimalStringValue){
        BigDecimal bd = getBigDecimalNumber(usdDecimalStringValue);
        if(bd == null){
            return "";
        }
        // 将金额转换为英文大写形式
        DecimalFormat df = new DecimalFormat("#.00");
        String[] numberInWords = df.format(bd.doubleValue()).split("\\.");
        long dollars = 0L;
        if(numberInWords[0].length() != 0){
            try {
                dollars =  Long.parseLong(numberInWords[0]);
            } catch (Exception e){
                return "";
            }
        }
        String dollarsInWords = "DOLLARS " +  convertToWords(dollars);
        String centsInWords = "";
        System.out.println(Arrays.asList(numberInWords).toString());
        if(numberInWords[1].length() != 0){
            int cents = 0;
            try {
                cents = Integer.parseInt(numberInWords[1]);
            } catch (Exception e){
                return "";
            }
            // 当美分为0时不进行操作
            if(cents != 0) {
                centsInWords = "CENTS " + convertToWords(cents);
            }
        }
        // 格式化英文大写形式
        String result = dollarsInWords;
        if (centsInWords.length() > 0) {
            result += " AND " + centsInWords;
        }
        return result;
    }

    public static String convertToWords(long number) {
        if (number == 0) {
            return "ZERO";
        }
        if (number < 10) {
            return EN_UPPER_UNITS[(int) number];
        }
        if (number < 20) {
            return EN_UPPER_TEENS[(int) (number - 11)];
        }
        if (number < 100) {
            return EN_UPPER_TENS[(int) (number / 10)] + (number % 10 > 0 ? "-" + EN_UPPER_UNITS[(int) (number % 10)] : "");
        }
        if (number < 1000) {
            return EN_UPPER_UNITS[(int) (number / 100)] + " HUNDRED" + (number % 100 > 0 ? "," + convertToWords(number % 100) : "");
        }
        if (number < 1000000) {
            return convertToWords(number / 1000) + " THOUSAND" + (number % 1000 > 0 ? "," + convertToWords(number % 1000) : "");
        }
        if (number < 1000000000) {
            return convertToWords(number / 1000000) + " MILLION" + (number % 1000000 > 0 ? "," + convertToWords(number % 1000000) : "");
        }
        if (number < 1000000000000L) {
            return convertToWords(number / 1000000000) + " BILLION" + (number % 1000000000 > 0 ? "," + convertToWords(number % 1000000000) : "");
        }
        return "";
    }

}
