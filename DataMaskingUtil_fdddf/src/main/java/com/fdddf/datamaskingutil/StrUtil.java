package com.fdddf.datamaskingutil;


public class StrUtil {

    public static final String EMPTY = "";

    public static final String SPACE = " ";

    /**
     * 首位隐藏方法
     * 该方法用于将传入字符串的除首字符外的所有字符替换为星号("*")，用于隐藏信息。
     * @param str 待处理的字符串。可以为null或空字符串。
     * @return 处理后的字符串。如果输入为null或空字符串，则返回空字符串。
     */
    public static String firstMask(String str)
    {
        if (str == null || str.isEmpty())
        {
            return ""; // 输入为空时，直接返回空字符串
        }
        // 保留首位字符，用星号替换其他所有字符
        return str.charAt(0) + str.substring(1).replaceAll("\\S", "*");
    }


    /**
     * 对输入的字符串进行处理，将除最后一位外的所有字符替换为星号，最后一位字符保持不变。
     * 该方法主要用于隐藏字符串的大部分内容，只显示最后一位，达到保护隐私的目的。
     *
     * @param str 需要进行处理的字符串。可以为null或者空字符串，如果是null或者空字符串，则直接返回空字符串。
     * @return 处理后的字符串，如果输入为null或空字符串，则返回空字符串；否则返回替换后的字符串，其中除最后一位外的所有字符都被替换为星号。
     */
    public static String lastMask(String str)
    {
        // 判断输入字符串是否为空，为空则直接返回空字符串
        if (str == null || str.isEmpty())
        {
            return "";
        }
        // 返回处理后的字符串，将所有字符替换为星号，除了最后一位
        return "******" + str.charAt(str.length() - 1);
    }



    /**
     * 对字符串的指定部分进行隐藏处理。
     * @param str 需要处理的原始字符串。
     * @param start 需要隐藏的起始位置（包含）。
     * @param end 需要隐藏的结束位置（包含）。
     * @return 返回处理后的字符串。如果输入字符串为空或无效，返回空字符串；如果起始位置或结束位置不合法，返回原始字符串。
     */
    public static String hide(String str, int start, int end)
    {
        // 判断输入字符串是否为空
        if (str == null || str.isEmpty())
        {
            return "";
        }
        // 判断起始和结束位置是否合法
        if (start < 0 || end < 0 || start > end || end > str.length())
        {
            return str;
        }
        // 对指定范围内的字符串进行隐藏处理
        return str.substring(0, start) + "****" + str.substring(end);
    }


    /**
     * 判断给定的字符序列是否为空或只包含空格。
     * @param str 待检查的字符序列。
     * @return 如果字符序列为空或只包含空格，则返回true；否则返回false。
     */
    public static boolean isBlank(CharSequence str) {
        final int length;
        // 检查字符序列是否为空或长度为0
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        // 遍历字符序列，检查是否有非空白字符
        for (int i = 0; i < length; i++) {
            // 只要发现一个非空白字符，即可判定为非空字符串
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }


    /**
     * 截取字符串，在遇到指定分隔符之前的所有字符。
     * @param str 待截取的字符串。
     * @param separator 分隔符，截取到该字符前为止。
     * @return 截取后的字符串。如果输入字符串或分隔符为空/白空间，或未找到分隔符，则返回原字符串。
     */
    public static String subBefore(String str, String separator) {
        // 检查输入字符串或分隔符是否为空/白空间
        if (isBlank(str) || isBlank(separator)) {
            return str;
        }
        // 查找分隔符的位置
        int pos = str.indexOf(separator);
        // 如果未找到分隔符，则返回原字符串
        if (pos == -1) {
            return str;
        }
        // 截取到分隔符之前的部分并返回
        return str.substring(0, pos);
    }


    /**
     * 清除字符串中的空白符
     * 该方法会首先判断传入的 CharSequence 是否为空白，若为空白则直接返回空字符串，
     * 否则会将传入的 CharSequence 转换为字符串，并替换其中的所有连续空白符为一个空格，
     * 最后返回处理后的字符串。
     *
     * @param str 待处理的 CharSequence 对象，可能为 null 或包含空白符的字符串
     * @return 处理后的字符串，如果原字符串为空白则返回空字符串
     */
    public static String cleanBlank(CharSequence str) {
        // 判断传入的字符串是否为空白
        if (isBlank(str)) {
            return EMPTY;
        }
        // 替换字符串中的连续空白符为一个空格
        return str.toString().replaceAll("\\s+", EMPTY);
    }
}

