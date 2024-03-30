package com.fdddf.jsonutil;

import com.fdddf.jsonutil.api.JsonUtilApi;

public class Main {
    public static void main(String[] args) throws Exception {
        String xml = "<root><name>张三</name><age>20</age><sex>男</sex><a name=\"张三\"></a>\n" +
                "<a name=\"李四\"></a>\n" +
                "<b name=\"王五\"></b></root>";
        String json = JsonUtilApi.xmlToJson(xml);
        System.out.println(json);

        System.out.println(JsonUtilApi.queryJson(json, "root.b.name"));
    }

}
