package com.yu.api;


import com.alibaba.fastjson.JSONException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jayway.jsonpath.JsonPath;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    /**
     * 将xml 字符串转成json字符串
     *
     * @param xml 符合格式的xml字符串   入参示例：<root><element1>value1</element1><element2>value2</element2></root>
     * @return 返回示例：{"element1":"value1","element2":"value2"}
     */
    @NaslLogic
    public static String xmlToJson(String xml) throws IllegalArgumentException {
        try {
            // 将XML字符串解析为DOM对象
            XmlMapper xmlMapper = new XmlMapper();
            xml = "<root>" + xml + "</root>";
            JsonNode node = xmlMapper.readTree(xml.getBytes());
            // 将DOM对象转换为JSON对象
            ObjectMapper jsonMapper = new ObjectMapper();
            return jsonMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            log.error("出现JsonProcessingException异常：", e);
            throw new IllegalArgumentException("xml传入格式异常");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将json字符串按照指定的jsonPath取出
     *
     * @param jsonStr  json字符串 示例 {"element1":"value1","element2":"value2"}
     * @param jsonPath json路径  示例 $.element1
     * @return 返回值示例  value1
     */
    @NaslLogic
    public static String queryFromJson(String jsonStr, String jsonPath) throws JSONException {
        try {
            Object result = JsonPath.read(jsonStr, jsonPath);
            if (result != null) {
                return result.toString();
            } else {
                return null;
            }
        } catch (JSONException e) {
            log.error("出现JSONException异常：", e);
            throw e;
        }
    }

    public static void main(String[] args) throws IOException {
        String s = xmlToJson("<root><element1>value1</element1><element2>value2</element2></root>");
        System.out.println(s);
        String s1 = queryFromJson("{\"properties\":{\"shiro.version\":\"1.7.1\",\"java.version\":\"1.8\",\"graalvm.version\":\"20.1.0\",\"jwt.version\":\"3.12.1\"}}", "$.properties.[\"jwt.version\"]");
        System.out.println("s1 = " + s1);

    }


}
