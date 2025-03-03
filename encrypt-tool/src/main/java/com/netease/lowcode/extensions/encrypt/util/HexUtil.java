package com.netease.lowcode.extensions.encrypt.util;

import javax.xml.bind.DatatypeConverter;

/**
 * 16进制工具类
 */
public class HexUtil {
    private HexUtil() {
    }

    /**
     * 将字节转换为16进制字符串
     *
     * @param sourceBytes 待转换字节数组
     * @return 16进制字符串
     */
    public static String encode(byte[] sourceBytes) {
        return DatatypeConverter.printHexBinary(sourceBytes);
    }

    /**
     * 将16进制字符串转换为字节数组
     *
     * @param hexStr
     * @return
     */
    public static byte[] decode(String hexStr) {
        return DatatypeConverter.parseHexBinary(hexStr);
    }

    public static String encode2(byte[] sourceBytes) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < sourceBytes.length; i++) {
            String plainText = Integer.toHexString(0xff & sourceBytes[i]);
            if(plainText.length() < 2){
                plainText = "0"+plainText;
            }
            hexString.append(plainText);
        }
        return hexString.toString();
    }

}
