package com.moocsk.lowcode.youdao.translate.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

public class AuthV3Util {

    /**
     * 添加鉴权相关参数
     * 
     * @param appKey    应用ID
     * @param appSecret 应用密钥
     * @param params    请求参数
     */
    public static void addAuthParams(String appKey, String appSecret, Map<String, String[]> params)
            throws NoSuchAlgorithmException {
        String[] qArray = params.get("q");
        StringBuilder q = new StringBuilder();
        for (String item : qArray) {
            q.append(item);
        }
        // 签名
        String salt = UUID.randomUUID().toString();
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        String sign = calculateSign(appKey, appSecret, q.toString(), salt, curtime);
        // 添加请求参数
        params.put("appKey", new String[] { appKey }); // 应用ID
        params.put("salt", new String[] { salt }); // 随机值
        params.put("curtime", new String[] { curtime }); // 当前时间戳(秒)
        params.put("signType", new String[] { "v3" }); // 签名版本
        params.put("sign", new String[] { sign }); // 请求签名
    }

    /**
     * 计算鉴权签名
     * 计算方式 : sign = sha256(appKey + input(q) + salt + curtime + appSecret)
     *
     * @param appKey    应用ID
     * @param appSecret 应用密钥
     * @param q         请求内容
     * @param salt      随机值
     * @param curtime   当前时间戳(秒)
     * @return 鉴权签名sign
     */
    public static String calculateSign(String appKey, String appSecret, String q, String salt, String curtime) {
        String strSrc = appKey + truncate(q) + salt + curtime + appSecret;
        try {
            return encrypt(strSrc);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密字符串
     * 
     * @param strSrc 字符串
     * @return 加密后字符串
     */
    private static String encrypt(String strSrc) throws NoSuchAlgorithmException {
        byte[] bt = strSrc.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bt);
        byte[] bts = md.digest();
        StringBuilder des = new StringBuilder();
        for (byte b : bts) {
            String tmp = (Integer.toHexString(b & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    /**
     * 获取签名需要的翻译文本
     * 
     * @param input 翻译文本
     * @return 签名需要的翻译文本
     */
    private static String truncate(String input) {
        if (input == null) {
            return null;
        }
        String result;
        int len = input.length();
        if (len <= 20) {
            result = input;
        } else {
            String startStr = input.substring(0, 10);
            String endStr = input.substring(len - 10, len);
            result = startStr + len + endStr;
        }
        return result;
    }
}
