package com.codewave.jsonUtils.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.json.XML;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonUtilApi {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JsonUtilApi.class);

    /**
     * 验证字符串是否为JSON格式
     * @param content 待验证的字符串
     * @return true字符串为JSON格式，false字符串不为JSON格式
     */
    @NaslLogic
    public static Boolean validateJson(String content){
        if(content == null || content.length() == 0){
            return false;
        }
        try {
            JSON.parseObject(content);
        } catch (Exception e){
            log.error("当前content验证json失败,content:{}", content, e);
            return false;
        }

        return true;
    }

    /**
     * 验证字符串是否为JSONArray格式
     * @param content 待验证的字符串
     * @return true字符串为JSONArray格式，false字符串不为JSONArray格式
     */
    @NaslLogic
    public static Boolean validateJsonArray(String content) {
        if(content == null || content.length() == 0){
            return false;
        }
        try {
            JSON.parseArray(content);
        } catch (Exception e){
            log.error("当前content验证jsonArray失败,content:{}", content, e);
            return false;
        }

        return true;
    }

    /**
     * 将xml内容转json
     * @param xmlString xml内容
     * @return json字符串
     */
    @NaslLogic
    public static String convertXmlToJson(String xmlString) {
        try {
            org.json.JSONObject jsonObject = XML.toJSONObject(xmlString);
            return jsonObject.toString();
        } catch (Exception e){
            log.error("xmlString格式错误，xmlString:{}", xmlString, e);
            throw new IllegalArgumentException("非法xml格式");
        }
    }


    /**
     *  获得jsonArray下标的Json内容
     * @param index 下标从0开始
     * @param jsonArrayContent jsonArray
     * @return json内容
     */
    @NaslLogic
    public static String getJsonObject(String jsonArrayContent, Integer index) {
        Boolean jsonArrayFlag = validateJsonArray(jsonArrayContent);
        if(!jsonArrayFlag){
            throw new IllegalArgumentException("当前字符串内容不为jsonArray");
        }
        JSONArray jsonArray = JSON.parseArray(jsonArrayContent);
        if(index == null || index < 0 || index > (jsonArray.size() - 1)){
            throw new IllegalArgumentException("非法下标");
        }
        return jsonArray.getJSONObject(index).toJSONString();
    }

    /**
     * 将json转为Map对象
     * @param jsonContent jsonContent
     * @return Map对象
     */
    @NaslLogic
    public static Map<String, String> jsonObjectToMap(String jsonContent) {
        Boolean jsonFlag = validateJson(jsonContent);
        if(!jsonFlag){
            throw new IllegalArgumentException("传入内容不符合json格式");
        }
        return JSON.parseObject(jsonContent, new TypeReference<Map<String, String>>(){});
    }


    private static JSONObject getJsonObject(String jsonContent){
        Boolean jsonFlag = validateJson(jsonContent);
        if(!jsonFlag){
            throw new IllegalArgumentException("当前字符串不符合json格式");
        }
        return JSON.parseObject(jsonContent);
    }

    private static JSONArray getJsonArrayObject(String jsonArrayContent){
        Boolean jsonFlag = validateJsonArray(jsonArrayContent);
        if(!jsonFlag){
            throw new IllegalArgumentException("当前字符串不符合json格式");
        }
        return JSON.parseArray(jsonArrayContent);
    }
    /**
     * 从JSON字符串获得对应KEY的String类型的Value
     * @param jsonContent jsonContent
     * @return String对象
     */
    @NaslLogic
    public static String getString(String jsonContent, String key) {
        JSONObject jsonObject = getJsonObject(jsonContent);
        return jsonObject.getString(key);
    }


    /**
     * 从JSON字符串获得对应KEY的Boolean类型的Value
     * @param jsonContent jsonContent
     * @return Boolean
     */
    @NaslLogic
    public static Boolean getBoolean(String jsonContent, String key) {
        JSONObject jsonObject = getJsonObject(jsonContent);
        return jsonObject.getBoolean(key);
    }
    /**
     * 从JSON字符串获得对应KEY的Integer类型的Value
     * @param jsonContent jsonContent
     * @return Integer
     */
    @NaslLogic
    public static Integer getInteger(String jsonContent, String key) {
        JSONObject jsonObject = getJsonObject(jsonContent);
        return jsonObject.getInteger(key);
    }

    /**
     * 从JSON字符串获得对应KEY的Long类型的Value
     * @param jsonContent jsonContent
     * @return Long
     */
    @NaslLogic
    public static Long getLong(String jsonContent, String key) {
        JSONObject jsonObject = getJsonObject(jsonContent);
        return jsonObject.getLong(key);
    }


    /**
     * 从JSON字符串获得对应KEY的Double类型的Value
     * @param jsonContent jsonContent
     * @return Double
     */
    @NaslLogic
    public static Double getDouble(String jsonContent, String key) {
        JSONObject jsonObject = getJsonObject(jsonContent);
        return jsonObject.getDouble(key);
    }

    /**
     * 从JSON字符串获得对应KEY的LocalDate类型的Value
     * @param jsonContent jsonContent
     * @return LocalDate
     */
    @NaslLogic
    public static LocalDate getLocalDate(String jsonContent, String key) {
        JSONObject jsonObject = getJsonObject(jsonContent);
        Date date = jsonObject.getDate(key);
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * 从JSON字符串获得对应KEY的LocalTime类型的Value
     * @param jsonContent jsonContent
     * @return LocalTime
     */
    @NaslLogic
    public static LocalTime getLocalTime(String jsonContent, String key) {
        JSONObject jsonObject = getJsonObject(jsonContent);
        Date date = jsonObject.getDate(key);
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalTime();
    }


    /**
     * 从JSON字符串获得对应KEY的ZonedDateTime类型的Value
     * @param jsonContent jsonContent
     * @return ZonedDateTime
     */
    @NaslLogic
    public static ZonedDateTime getZonedDateTime(String jsonContent, String key) {
        JSONObject jsonObject = getJsonObject(jsonContent);
        String zonedDateTimeString = jsonObject.getString(key);
        DateTimeFormatter str1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Shanghai"));
        return ZonedDateTime.parse(zonedDateTimeString, str1);
    }


    /**
     * 从JSON字符串获得对应KEY的BigDecimal类型的Value
     * @param jsonContent jsonContent
     * @return BigDecimal
     */
    @NaslLogic
    public static BigDecimal getBigDecimal(String jsonContent, String key) {
        JSONObject jsonObject = getJsonObject(jsonContent);
        return jsonObject.getBigDecimal(key);
    }



}
