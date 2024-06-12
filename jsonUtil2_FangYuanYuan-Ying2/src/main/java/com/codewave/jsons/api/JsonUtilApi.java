package com.codewave.jsons.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsonUtilApi {
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");


    /**
     * 将xml内容转json
     * @param xmlString xml内容
     * @return json字符串,当返回错误，返回null并且打印error日志
     */
    @NaslLogic
    public static String convertXmlToJson(String xmlString) {
        try {
            org.json.JSONObject jsonObject = XML.toJSONObject(xmlString);
            return jsonObject.toString();
        } catch (Exception e){
            log.error("XmlString format error, xmlString: {}", xmlString, e);
            return null;
        }
    }


    /**
     * 通过JSONPath 表达式在json内容获得特定的KEY
     * 实例：
     * json: "{"name":"John","age":30,"address":{"city":"Hangzhou"}}"
     * key: $.name -> 表示顶层的 name key
     * key: $.address.city -> 表示嵌套在 address 中的 city key
     *
     * @param json json内容
     * @param key 通过JSONPath表达式
     * @return 返回对应key的内容,当json或者key为空，返回null，当key格式错误返回null,并且打印错误日志
     */
    @NaslLogic
    public static String getXPathKey(String json, String key) {
        if(!validateJson(json)){
            log.error("The current incoming JSON content does not conform to JSON format,json:{}", json);
            return null;
        }
        if(key == null || key.length() == 0){
            log.error("The key is empty");
            return null;
        }
        return JSONPath.eval(json, key).toString();
    }


    /**
     * 验证字符串是否为JSON格式
     * @param content 待验证的字符串
     * @return true字符串为JSON格式，false字符串不为JSON格式
     */
    private static Boolean validateJson(String content){
        if(content == null || content.length() == 0){
            return false;
        }
        try {
            JSON.parseObject(content);
        } catch (Exception e) {
            log.error("The current incoming JSON content does not conform to JSON format,json:{}", content);
            return false;
        }
        return true;
    }


}
