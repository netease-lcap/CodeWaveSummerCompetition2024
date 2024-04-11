package com.lowcode.netease.api;

import java.util.HashMap;
import java.util.Map;

import com.lowcode.netease.utils.HttpUtil;
import com.lowcode.netease.utils.MD5;

public class TransApi {
    /** 通用文本翻译地址 */
    private static final String TRANS_API_HOST = "https://fanyi-api.baidu.com/api/trans/vip/translate";

    /** 应用 ID */
    private String appid;
    /** 密钥 */
    private String securityKey;

    /**
     * @param appid 应用 ID
     * @param securityKey 密钥
     */
    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    /**
     * 获取翻译结果
     * @param query 需要翻译的内容
     * @param from 源语言
     * @param to 目标语言
     */
    public String getTransResult(String query, String from, String to) {
        Map<String, String> params;
        try {
            params = buildParams(query, from, to);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return HttpUtil.get(TRANS_API_HOST, params);
    }

    private Map<String, String> buildParams(String query, String from, String to) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appid);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = appid + query + salt + securityKey; // 加密前的原文
        params.put("sign", MD5.md5(src));

        return params;
    }

}
