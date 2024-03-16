package com.codewave.jsonUtils.api;

import com.alibaba.fastjson.JSONException;
import org.junit.Assert;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestJsonUtilApi {


    /**
     * 验证字符串是否为JSON格式
     */
    @Test
    public void testVerifyJson(){
        List<String> nonJsonStringList = Arrays.asList(null, "", "test", "{test}", "{\"test\":2323,","[{\"key1\": \"value1\"}]");
        List<String> jsonStringList = Arrays.asList("{\"je\":\"fdfd\"}","{\"key1\": \"value1\"}","{\"key1\": \"value1\",\"key1\": \"value2\"}","{\"key1\": {\"key2\":\"value2\"}}");

        for(String nonJson : nonJsonStringList){
            Boolean verifyFlag = JsonUtilApi.validateJson(nonJson);
            Assert.assertEquals(false, verifyFlag);
        }

        for(String json : jsonStringList){
            Boolean verifyFlag = JsonUtilApi.validateJson(json);
            Assert.assertEquals(true, verifyFlag);
        }
    }

    /**
     * 验证字符串是否为JSONArray格式
     */
    @Test
    public void testVerifyJsonArray(){
        List<String> nonJsonStringList = Arrays.asList(null, "", "test", "[{test}]", "[{\"test\":2323,]");
        List<String> jsonStringList = Arrays.asList("[{\"key1\": \"value1\"}]","[{\"key1\": {\"key2\":\"value2\"}}]");

        for(String nonJson : nonJsonStringList){
            Boolean verifyFlag = JsonUtilApi.validateJsonArray(nonJson);
            Assert.assertEquals(false, verifyFlag);
        }

        for(String jsonArray : jsonStringList){
            Boolean verifyFlag = JsonUtilApi.validateJsonArray(jsonArray);
            Assert.assertEquals(true, verifyFlag);
        }
    }

    /**
     * 获得JSOnArray的下标JSON字符串
     */
    @Test
    public void testGetJSONObject(){
        String jsonArrayContent = "[{\"KEY\":\"value\"},{\"KEY\":\"value2\"}, {\"KEY\":\"value3\"}]";
        String content0 = JsonUtilApi.getJsonObject(jsonArrayContent,0);
        Assert.assertEquals("{\"KEY\":\"value\"}", content0);
        String content1 = JsonUtilApi.getJsonObject(jsonArrayContent, 1);
        Assert.assertEquals("{\"KEY\":\"value2\"}", content1);
        String content2 = JsonUtilApi.getJsonObject(jsonArrayContent, 2);
        Assert.assertEquals("{\"KEY\":\"value3\"}", content2);
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            JsonUtilApi.getJsonObject(jsonArrayContent, -1);
        });
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            JsonUtilApi.getJsonObject(jsonArrayContent, null);
        });
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            JsonUtilApi.getJsonObject(jsonArrayContent, 3);
        });
    }

    /**
     * 将JsonObject转Map对象
     */
    @Test
    public void testJsonObjectToMap(){
        String jsonContent = "{\"KEY_\":\"value1\", \"date\":\"2023-02-01\",  \"datetime\":\"2023-02-01 11:12:23\"}";
        Map<String, String> expectedMap = new HashMap<String, String>(){{ put("KEY_","value1"); put("date","2023-02-01");  put("datetime","2023-02-01 11:12:23");}};
        Map<String, String> jsonMap = JsonUtilApi.jsonObjectToMap(jsonContent);
        System.out.println("testJsonArrayToList" + jsonMap);
        Assert.assertEquals(expectedMap, jsonMap);
    }




    @Test
    public void testGetString(){
        String jsonContent = "{\"key\":\"value1\", \"date\":\"2023-02-01\",  \"flag\":false}";
        Assert.assertEquals("value1", JsonUtilApi.getString(jsonContent, "key"));
        Assert.assertEquals("2023-02-01", JsonUtilApi.getString(jsonContent, "date"));
        Assert.assertEquals("false", JsonUtilApi.getString(jsonContent, "flag"));
    }



    @Test
    public void testGetBoolean(){
        String jsonContent = "{\"key\":\"value1\", \"date\":\"2023-02-01\",  \"flag\":false,  \"flag2\":\"true\", \"flag3\":\"TRUE\"}";

        Assert.assertThrows(JSONException.class, () -> {
            JsonUtilApi.getBoolean(jsonContent, "key");
        });
        Assert.assertThrows(JSONException.class, () -> {
            JsonUtilApi.getBoolean(jsonContent, "date");
        });
        Assert.assertFalse(JsonUtilApi.getBoolean(jsonContent, "flag"));
        Assert.assertTrue(JsonUtilApi.getBoolean(jsonContent, "flag2"));
        Assert.assertTrue(JsonUtilApi.getBoolean(jsonContent, "flag3"));
    }

    @Test
    public void testGetInteger(){
        String jsonContent = "{\"key\":23}";
        Assert.assertEquals(Optional.of(23).get(),  JsonUtilApi.getInteger(jsonContent, "key"));
    }

    @Test
    public void testGetLong(){
        String jsonContent = "{\"key\":23}";
        Assert.assertEquals(Optional.of(23L).get(),  JsonUtilApi.getLong(jsonContent, "key"));
    }

    @Test
    public void testGetDouble(){
        String jsonContent = "{\"key\":23.23}";
        Assert.assertEquals(Optional.of(23.23).get(),  JsonUtilApi.getDouble(jsonContent, "key"));
    }

    @Test
    public void testGetLocalDate(){
        String jsonContent = "{\"key\":\"2023-02-01\"}";
        LocalDate localDate = LocalDate.of(2023,2, 1);
        Assert.assertEquals(localDate,  JsonUtilApi.getLocalDate(jsonContent, "key"));
    }

    @Test
    public void testGetLocalTime(){
        String jsonContent = "{\"key\":\"2023-02-01 11:12:13\"}";
        LocalTime localDate = LocalTime.of(11, 12, 13);
        Assert.assertEquals(localDate,  JsonUtilApi.getLocalTime(jsonContent, "key"));
    }


    @Test
    public void testGetZonedDateTime(){
        String jsonContent = "{\"key\":\"2023-02-01 11:12:13\"}";
        String str="2023-02-01 11:12:13";
        DateTimeFormatter str1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Shanghai"));
        ZonedDateTime zdt = ZonedDateTime.parse(str, str1);
        Assert.assertEquals(zdt,  JsonUtilApi.getZonedDateTime(jsonContent, "key"));
    }

    @Test
    public void testConvertXmlToJson(){
        // XML 格式字符串
        String xmlString = "<root><name>John</name><age>30</age></root>";
        // 验证 XML 格式是否正确
        String json = "{\"root\":{\"name\":\"John\",\"age\":30}}";
        Assert.assertEquals(json, JsonUtilApi.convertXmlToJson(xmlString));


        Assert.assertThrows(IllegalArgumentException.class, () -> {
            String errorXmlString = "<root><name>John<name><age>30</age></root>";
            JsonUtilApi.convertXmlToJson(errorXmlString);
        });
    }


}
