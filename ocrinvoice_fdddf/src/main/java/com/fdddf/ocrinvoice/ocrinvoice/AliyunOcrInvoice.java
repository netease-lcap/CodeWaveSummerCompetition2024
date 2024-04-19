package com.fdddf.ocrinvoice.ocrinvoice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fdddf.ocrinvoice.rollticket.AliyunOcrRollTicket;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 【阿里官方】增值税发票OCR文字识别
 * <a href="https://market.aliyun.com/apimarket/detail/cmapi027758">API</a>
 */
public class AliyunOcrInvoice {
    private static final Logger log = LoggerFactory.getLogger(AliyunOcrRollTicket.class);

    public OcrInvoice request(String appCode, String url) throws RuntimeException {
        if (appCode.isEmpty() || url.isEmpty()) {
            throw new RuntimeException("appCode or url is empty");
        }

        try {
            OkHttpClient client = new OkHttpClient();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", url);
            jsonObject.put("img", "");

            RequestBody requestJsonBody = RequestBody.create(
                    jsonObject.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url("https://dgfp.market.alicloudapi.com/ocrservice/invoice")
                    .post(requestJsonBody)
                    .addHeader("Authorization", "APPCODE " + appCode)
                    .addHeader("Content-Type", "application/json; charset=UTF-8")
                    .build();

            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();

            assert body != null;
            String bodyString = body.string();
            System.out.printf("API response body %s\n", bodyString);
            log.info("API response body {}", bodyString);
            return JSONObject.toJavaObject(JSON.parseObject(bodyString), OcrInvoice.class);
        } catch (IOException e) {
            System.out.printf("API request failed %s\n", e);
            log.info("API request exception: {}", e.getMessage());
            return null;
        }
    }
}
