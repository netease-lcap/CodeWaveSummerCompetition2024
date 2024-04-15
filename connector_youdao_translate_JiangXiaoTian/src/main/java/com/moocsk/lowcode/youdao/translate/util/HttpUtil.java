package com.moocsk.lowcode.youdao.translate.util;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {

    /** HttpClient 实例 */
    private static OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

    /**
     * 发送 GET 请求
     * 
     * @param url    请求地址
     * @param header 请求头
     * @param params 查询参数
     * @return 响应结果
     */
    public static String doGet(String url, Map<String, String[]> header, Map<String, String[]> params) {
        Request.Builder builder = new Request.Builder();
        addHeader(builder, header);
        addUrlParams(builder, url, params);
        return request(builder.build());
    }

    /**
     * 发送 post 请求
     * 
     * @param url    请求地址
     * @param header 请求头
     * @param body   请求体
     * @return 响应结果
     */
    public static String doPost(String url, Map<String, String[]> header, Map<String, String[]> body) {
        Request.Builder builder = new Request.Builder().url(url);
        addHeader(builder, header);
        addBodyParam(builder, body, "POST");
        return request(builder.build());
    }

    /**
     * 设置请求头
     * 
     * @param builder 请求构造器
     * @param header  请求投
     */
    private static void addHeader(Request.Builder builder, Map<String, String[]> header) {
        if (header == null) {
            return;
        }
        for (String key : header.keySet()) {
            String[] values = header.get(key);
            if (values != null) {
                for (String value : values) {
                    builder.addHeader(key, value);
                }
            }
        }
    }

    /**
     * 设置请求查询参数
     * 
     * @param builder 请求构造器
     * @param url     请求地址
     * @param params  查询参数
     */
    private static void addUrlParams(Request.Builder builder, String url, Map<String, String[]> params) {
        if (params == null) {
            return;
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        for (String key : params.keySet()) {
            String[] values = params.get(key);
            if (values != null) {
                for (String value : values) {
                    urlBuilder.addQueryParameter(key, value);
                }
            }
        }
        builder.url(urlBuilder.build());
    }

    /**
     * 设置请求体
     * 
     * @param builder 请求构造器
     * @param body    请求体
     * @param method  请求方法
     */
    private static void addBodyParam(Request.Builder builder, Map<String, String[]> body, String method) {
        if (body == null) {
            return;
        }
        FormBody.Builder formBodyBuilder = new FormBody.Builder(StandardCharsets.UTF_8);
        for (String key : body.keySet()) {
            String[] values = body.get(key);
            if (values != null) {
                for (String value : values) {
                    formBodyBuilder.add(key, value);
                }
            }
        }
        builder.method(method, formBodyBuilder.build());
    }

    /**
     * 发送请求
     * 
     * @param request 请求体
     * @return 响应消息
     */
    private static String request(Request request) {
        String result = null;
        try {
            Response response = httpClient.newCall(request).execute();
            if (response.code() == 200) {
                ResponseBody body = response.body();
                result = new String(body.bytes(), StandardCharsets.UTF_8);
            } else {
                throw new RuntimeException(response.message());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
