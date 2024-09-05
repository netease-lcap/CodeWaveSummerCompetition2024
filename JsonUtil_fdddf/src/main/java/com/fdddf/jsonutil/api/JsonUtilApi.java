package com.fdddf.jsonutil.api;

import com.alibaba.fastjson.JSONPath;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtilApi {

    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    /**
     * 将XML格式的字符串转换为JSON格式的字符串
     *
     * @param xml 需要转换的XML格式字符串
     * @return 转换后的JSON格式字符串
     */
    @NaslLogic
    public static String xmlToJson(String xml) {
        try {
            // 将XML字符串转换为JSONObject
            JSONObject json = XML.toJSONObject(xml);
            return json.toString();
        } catch (JSONException e) {
            logger.error("Convert xml to json failed:", e);
            return null;
        }
    }

    /**
     * 从JSON字符串中查询指定键值对应的值
     *
     * @param json 输入的JSON字符串。
     * @param key 需要查询的键。
     * @return 如果找到对应的键值，则返回其字符串形式；如果未找到或发生异常，则返回null。
     */
    @NaslLogic
    public static String queryJson(String json, String key) {
        try {
            // 使用JSONPath表达式来查询指定键的值
            return JSONPath.eval(json, key).toString();
        } catch (Exception e) {
            // 查询失败或出现异常时返回null
            logger.error("Query json failed:", e);
            return null;
        }
    }
}
