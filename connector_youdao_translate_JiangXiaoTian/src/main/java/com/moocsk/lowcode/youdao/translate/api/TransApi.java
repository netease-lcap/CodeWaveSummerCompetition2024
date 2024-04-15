package com.moocsk.lowcode.youdao.translate.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.moocsk.lowcode.youdao.translate.model.TranslateResult;
import com.moocsk.lowcode.youdao.translate.util.AuthV3Util;
import com.moocsk.lowcode.youdao.translate.util.HttpUtil;
import com.moocsk.lowcode.youdao.translate.util.ModelUtil;

public class TransApi {
    /** 批量文本翻译地址 */
    private static final String TRANS_API_HOST = "https://openapi.youdao.com/v2/api";

    /** 应用 ID */
    private String appid;
    /** 密钥 */
    private String securityKey;

    /**
     * @param appid       应用 ID
     * @param securityKey 密钥
     */
    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    /**
     * 批量文本翻译
     * 
     * @param query 需要翻译的内容
     * @param from  源语言
     * @param to    目标语言
     * @return 翻译结果对象
     */
    public TranslateResult generalTextTranslation(List<String> query, String from, String to) {
        TranslateResult result = new TranslateResult();
        try {
            // 请求参数
            Map<String, String[]> params = this.buildParams(query, from, to);
            // 请求api服务
            String response = HttpUtil.doPost(TRANS_API_HOST, null, params);
            JSONObject jsonObject = JSONObject.parseObject(response);
            // 如果 http 200，接口返回错误信息
            String errorCode = jsonObject.getString("errorCode");
            result.setErrorCode(errorCode);
            if (!errorCode.equals("0")) {
                String errorMsg = jsonObject.getString("msg");
                result.setErrorMsg(errorMsg);
                return result;
            }
            String[] lang = jsonObject.getString("l").split("2");
            result.setFrom(lang[0]);
            result.setTo(lang[1]);
            JSONArray trans = jsonObject.getJSONArray("translateResults");
            result.setTranslateResults(ModelUtil.getTranslateModels(trans));
        } catch (Exception e) {
            result.setErrorCode("500");
            result.setErrorMsg(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 构建查询参数 Map
     * 
     * @param query 需要翻译的内容
     * @param from  源语言
     * @param to    目标语言
     * @return 查询参数 Map
     */
    private Map<String, String[]> buildParams(List<String> query, String from, String to) throws Exception {
        // 添加请求参数
        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("q", query.toArray(new String[query.size()]));
        params.put("from", new String[] { from });
        params.put("to", new String[] { to });
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(this.appid, this.securityKey, params);
        return params;
    }

}
