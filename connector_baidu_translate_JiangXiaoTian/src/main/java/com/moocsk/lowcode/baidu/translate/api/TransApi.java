package com.moocsk.lowcode.baidu.translate.api;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.moocsk.lowcode.baidu.translate.model.TranslateResult;
import com.moocsk.lowcode.baidu.translate.util.HttpUtil;
import com.moocsk.lowcode.baidu.translate.util.MD5;
import com.moocsk.lowcode.baidu.translate.util.ModelUtil;

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
     * 通用文本翻译
     * @param query 需要翻译的内容
     * @param from 源语言
     * @param to 目标语言
     * @return 翻译结果对象
     */
    public TranslateResult generalTextTranslation(String query, String from, String to) {
        TranslateResult result = new TranslateResult();
        try {
            Map<String, String> params = this.buildParams(query, from, to);
            String response = HttpUtil.get(TRANS_API_HOST, params);
            JSONObject jsonObject = JSONObject.parseObject(response);
            // 如果 http 200，接口返回错误信息
            String errorCode = jsonObject.getString("error_code");
            if (errorCode != null) {
                String errorMsg = jsonObject.getString("error_msg");
                result.setErrorCode(errorCode);
                result.setErrorMsg(errorMsg);;
                return result;
            }
            result.setFrom(jsonObject.getString("from"));
            result.setTo(jsonObject.getString("to"));
            JSONArray array = jsonObject.getJSONArray("trans_result");
            result.setTransResult(ModelUtil.getTranslateModels(array));
        } catch (Exception e) {
            result.setErrorCode("500");
            result.setErrorMsg(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 构建查询参数 Map
     * @param query 需要翻译的内容
     * @param from 源语言
     * @param to 目标语言
     * @return 查询参数 Map
     */
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
