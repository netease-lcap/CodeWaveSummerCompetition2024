package com.codewave.json.api;

import org.junit.Assert;
import org.junit.Test;


public class TestJsonUtilApi {


    @Test
    public void testConvertXmlToJson(){
        // XML 格式字符串
        String xmlString = "<root><name>John</name><age>30</age></root>";
        // 验证 XML 格式是否正确
        String json = "{\"root\":{\"name\":\"John\",\"age\":30}}";
        Assert.assertEquals(json, JsonUtilApi.convertXmlToJson(xmlString));

        String errorXmlString = "<root><name>John<name><age>30</age></root>";
        Assert.assertNull(JsonUtilApi.convertXmlToJson(errorXmlString));
    }

    /**
     * 从json获得对应xpath
     */
    @Test
    public void testGetKey(){
        String json = "{\"name\":\"John\",\"age\":30,\"address\":{\"city\":\"Hangzhou\"}}";
        Assert.assertEquals("John", JsonUtilApi.getXPathKey(json, "$.name"));
        Assert.assertEquals("30", JsonUtilApi.getXPathKey(json, "$.age"));
        Assert.assertEquals("Hangzhou", JsonUtilApi.getXPathKey(json, "$.address.city"));
    }



}
