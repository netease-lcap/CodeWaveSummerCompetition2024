package com.wjm.api;

import com.netease.lowcode.core.annotation.NaslLogic;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {

    private static final String[] UNITS = {"", " THOUSAND,", " MILLION,", " BILLION,", " TRILLION,"};
    private static final String[] ONES = {"", " ONE", " TWO", " THREE", " FOUR", " FIVE", " SIX", " SEVEN", " EIGHT", " NINE", "TEN",
            " ELEVEN", " TWELVE", " THIRTEEN", " FOURTEEN", " FIFTEEN", " SIXTEEN", " SEVENTEEN", " EIGHTEEN", " NINETEEN"};
    private static final String[] TENS = {"", "", " TWENTY", " THIRTY", " FORTY", " FIFTY", " SIXTY", " SEVENTY", " EIGHTY", " NINETY"};

    private static final String CN_NEGATIVE = "负";
    private static final String CN_ZEOR_FULL = "零圆整";

    private static final Map<String, Double> NUMBER_MAP = new HashMap<String, Double>() {{
        put("zero", 0d);
        put("one", 1d);
        put("two", 2d);
        put("three", 3d);
        put("four", 4d);
        put("five", 5d);
        put("six", 6d);
        put("seven", 7d);
        put("eight", 8d);
        put("nine", 9d);
        put("ten", 10d);
        put("eleven", 11d);
        put("twelve", 12d);
        put("thirteen", 13d);
        put("fourteen", 14d);
        put("fifteen", 15d);
        put("sixteen", 16d);
        put("seventeen", 17d);
        put("eighteen", 18d);
        put("nineteen", 19d);
        put("twenty", 20d);
        put("thirty", 30d);
        put("forty", 40d);
        put("fifty", 50d);
        put("sixty", 60d);
        put("seventy", 70d);
        put("eighty", 80d);
        put("ninety", 90d);
        put("hundred", 100d);
        put("thousand", 1000d);
        put("million", 1000000d);
        put("billion", 1000000000d);
        put("trillion", 1000000000000d);
    }};

    private static final Map<String, Double> NUMBER_MAP2 = new HashMap<String, Double>() {
        {
            put("零", 0d);
            put("壹", 1d);
            put("贰", 2d);
            put("叁", 3d);
            put("肆", 4d);
            put("伍", 5d);
            put("陆", 6d);
            put("柒", 7d);
            put("捌", 8d);
            put("玖", 9d);
            put("拾", 10d);
            put("佰", 100d);
            put("仟", 1000d);
            put("万", 10000d);
            put("亿", 100000000d);
            put("角", 0.1d);
            put("分", 0.01d);
        }
    };

    /**
     * 小写美元转大写美元
     *
     * @param amount
     * @return
     */
    @NaslLogic
    public static String convertToEnglish(String amount) {
        if (isEmpty(amount)) {
            throw new IllegalArgumentException("传入字符串不能为空");
        }
        BigDecimal value;
        try {
            value = new BigDecimal(amount.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("传入参数只能为数字");
        }
        // 返回值:-1-表示负数;0-表示0;1-表示正数
        int signum = value.signum();
        if (signum < 0) {
            return "NEGATIVE " + convertToEnglish(value.abs().toString());
        }
        if (signum == 0) {
            return "DOLLARS ZERO AND CENTS ZERO";
        }
        value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        String valStr = String.valueOf(value);
        // 取整数部分
        String dollars = valStr.split("\\.")[0];
        // 取小数部分
        String cents = valStr.split("\\.")[1];


        String dollarPart = convertIntegerPart(Long.parseLong(dollars));
        String centPart = convertIntegerPart(Long.parseLong(cents));


        return ("DOLLARS " + dollarPart + " AND CENTS " + centPart).replaceAll("\\s+", " ");
    }


    private static String convertIntegerPart(long amount) {
        if (amount == 0) {
            return "ZERO";
        }

        String result = "";
        int count = 0;

        while (amount > 0) {
            int part = (int) (amount % 1000);
            if (part != 0) {
                result = convertPart(part) + UNITS[count] + " " + result;
            }
            amount /= 1000;
            count++;
        }

        return result.trim();
    }


    private static String convertPart(int part) {
        if (part == 0) {
            return "";
        }
        if (part < 20) {
            return ONES[part] + " ";
        }
        if (part < 100) {
            return TENS[part / 10] + convertPart(part % 10);
        }

        return ONES[part / 100] + " HUNDRED " + convertPart(part % 100);
    }


    /**
     * 大写美元转小写美元
     *
     * @param englishNumber
     * @return
     */
    @NaslLogic
    public static String convertEnglishToNumber(String englishNumber) {
        if (isEmpty(englishNumber)) {
            throw new IllegalArgumentException("传入字符串不能为空");
        }
        englishNumber = englishNumber.toUpperCase();
        //处理负数
        String negative = "";
        if (englishNumber.contains("NEGATIVE")) {
            englishNumber = englishNumber.replace("NEGATIVE", "");
            negative = "-";
        }
        englishNumber = englishNumber.replace(",", "").replace("DOLLARS", "");
        String[] small = new String[0];
        if (englishNumber.contains("CENTS")) {
            String[] cents = englishNumber.split("CENTS");
            englishNumber = cents[0];
            small = cents[1].split(" ");
        }
        String[] words = englishNumber.trim().toLowerCase().replaceAll("\\s+", " ").split(" ");

        double result = 0;
        double currentNumber;

        for (String word : words) {
            if (NUMBER_MAP.containsKey(word)) {
                currentNumber = NUMBER_MAP.get(word);
                if (currentNumber >= 1000000000000d) {
                    result += currentNumber * (result % 1000000000000d);
                    result -= result % 1000000000000d;
                } else if (currentNumber >= 1000000000) {
                    result += currentNumber * (result % 1000000000);
                    result -= result % 1000000000;
                } else if (currentNumber >= 1000000) {
                    result += currentNumber * (result % 1000000);
                    result -= result % 1000000;
                } else if (currentNumber >= 1000) {
                    result += currentNumber * (result % 1000);
                    result -= result % 1000;
                } else if (currentNumber >= 100) {
                    result += currentNumber * (result % 100);
                    result -= result % 100;
                } else {
                    result += currentNumber;
                }
            } else if ("and".equals(word)) {
            } else {
                throw new IllegalArgumentException("Invalid word: " + word);
            }
        }

        // 处理小数位
        double smallPart = 0;
        if (small.length > 0) {
            for (String s : small) {
                if (NUMBER_MAP.containsKey(s.toLowerCase())) {
                    currentNumber = NUMBER_MAP.get(s.toLowerCase());
                    smallPart += currentNumber;
                }
            }
            result += smallPart / 100;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        return negative + df.format(result);
    }

    /**
     * 小写人民币转成大写(BigDecimal_防止浮点型数据最后一位失精问题。) 如果是负数，那么会在结果前加上负
     *
     * @param rmb
     * @return
     */
    @NaslLogic
    public static String rmbToBig(String rmb) {
        if (isEmpty(rmb)) {
            throw new IllegalArgumentException("传入字符串不能为空");
        }
        BigDecimal value;
        try {
            value = new BigDecimal(rmb.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("传入参数只能为数字");
        }
        // 返回值:-1-表示负数;0-表示0;1-表示正数
        int signum = value.signum();
        if (signum == 0) {
            return CN_ZEOR_FULL;
        } else {
            String signNumStr;
            if (signum < 0) {
                signNumStr = CN_NEGATIVE;
                value = value.abs();
            } else {
                signNumStr = "";
            }

            char[] hunit = {'拾', '佰', '仟'};
            char[] vunit = {'万', '亿'};
            char[] digit = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
            // 四舍五入保存两位小数
            value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
            String valStr = String.valueOf(value);
            // 取整数部分
            String head = valStr.split("\\.")[0];
            // 取小数部分
            String rail = valStr.split("\\.")[1];

            // 整数部分转化的结果
            String prefix = "";
            // 小数部分转化的结果
            String suffix;
            // 处理小数点后面的数
            if ("00".equals(rail)) {
                suffix = "整";
            } else {
                if (rail.charAt(0) == '0') {
                    suffix = digit[rail.charAt(1) - '0'] + "分";
                } else if (rail.charAt(1) == '0') {
                    suffix = digit[rail.charAt(0) - '0'] + "角";
                } else {
                    suffix = digit[rail.charAt(0) - '0'] + "角" + digit[rail.charAt(1) - '0'] + "分";
                }
            }
            boolean preZero = true;
            int hang;
            int lie;
            char[] chDig = head.toCharArray();
            int len = chDig.length;
            if (len % 4 != 0) {
                hang = len / 4;
                lie = len % 4 - 2;
            } else {
                hang = len / 4 - 1;
                lie = 2;
            }
            try {
                // 整数部分除了最后一位的所有数
                for (int i = 0; i < len - 1; i++) {
                    if (chDig[i] == '0' && chDig[i + 1] == '0') {
                        preZero = false;
                    } else if (chDig[i] == '0' && chDig[i + 1] != '0') {
                        preZero = true;
                    }
                    if (preZero) {
                        prefix += String.valueOf(digit[chDig[i] - '0']);
                        if (lie >= 0 && chDig[i] != '0') {
                            prefix += String.valueOf(hunit[lie]);
                        }
                    }
                    if (hang > 0 && lie == -1) {
                        prefix += String.valueOf(vunit[hang - 1]);
                        hang--;
                    }
                    lie--;
                    if (lie == -2) {
                        lie = 2;
                    }
                }

                // 整数部分最后一位数
                if (chDig[len - 1] != '0') {
                    prefix += String.valueOf(digit[chDig[len - 1] - '0']);
                }
                if (prefix.length() > 0) {
                    // 如果整数部分存在,则有圆的字样
                    prefix += '元';
                }

                if (prefix.contains("零万") || prefix.contains("零亿")) {
                    prefix = prefix.replaceAll("零万", "万");
                    prefix = prefix.replaceAll("零亿", "亿");
                }

                return signNumStr + prefix + suffix;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /**
     * 大写人民币转小写
     *
     * @param chineseNumber
     * @return
     */
    @NaslLogic
    public static String convertChineseToNumber(String chineseNumber) {
        if (isEmpty(chineseNumber)) {
            throw new IllegalArgumentException("传入字符串不能为空");
        }
        String negative = "";
        if (chineseNumber.contains("负")) {
            chineseNumber = chineseNumber.replace("负", "");
            negative = "-";
        }
        chineseNumber = chineseNumber.replace("整", "");
        String small = "";
        if (chineseNumber.contains("角") || chineseNumber.contains("分")) {
            if (chineseNumber.contains("元")) {
                String[] split = chineseNumber.split("元");
                chineseNumber = split[0];
                small = split[1];
            } else {
                small = chineseNumber;
                chineseNumber = "零";
            }
        }
        char[] words = chineseNumber.toCharArray();
        if ("拾".equals(String.valueOf(words[0]))) {
            throw new IllegalArgumentException("拾前面需要有具体数字，例如壹拾");
        }
        double result = 0;
        double currentNumber;

        for (char word : words) {
            if (NUMBER_MAP2.containsKey(String.valueOf(word))) {
                currentNumber = NUMBER_MAP2.get(String.valueOf(word));
                if (currentNumber >= 100000000d) {
                    result += currentNumber * (result % 100000000d);
                    result -= result % 100000000d;
                } else if (currentNumber >= 10000) {
                    result += currentNumber * (result % 10000);
                    result -= result % 10000;
                } else if (currentNumber >= 1000) {
                    result += currentNumber * (result % 1000);
                    result -= result % 1000;
                } else if (currentNumber >= 100) {
                    result += currentNumber * (result % 100);
                    result -= result % 100;
                } else if (currentNumber >= 10) {
                    result += currentNumber * (result % 10);
                    result -= result % 10;
                } else {
                    result += currentNumber;
                }
            } else if ("元".equals(String.valueOf(word))) {
            } else {
                throw new IllegalArgumentException("Invalid word: " + word);
            }
        }

        if (!isEmpty(small)) {
            char[] chars = small.toCharArray();
            if (small.contains("角")) {
                int index = small.indexOf("角");
                String word = String.valueOf(chars[index - 1]);
                if (NUMBER_MAP2.containsKey(word)) {
                    currentNumber = NUMBER_MAP2.get(word);
                    result += currentNumber / 10;
                } else {
                    throw new IllegalArgumentException("Invalid word: " + word);
                }
            }

            if (small.contains("分")) {
                int index2 = small.indexOf("分");
                String word = String.valueOf(chars[index2 - 1]);
                if (NUMBER_MAP2.containsKey(word)) {
                    currentNumber = NUMBER_MAP2.get(word);
                    result += currentNumber / 100;
                } else {
                    throw new IllegalArgumentException("Invalid word: " + word);
                }
            }
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return negative + df.format(result);
    }

    private static Boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
}