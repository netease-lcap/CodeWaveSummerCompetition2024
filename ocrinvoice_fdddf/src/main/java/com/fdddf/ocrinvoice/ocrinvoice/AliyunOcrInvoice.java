package com.fdddf.ocrinvoice.ocrinvoice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fdddf.ocrinvoice.rollticket.AliyunOcrRollTicket;
import com.fdddf.ocrinvoice.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 【阿里官方】增值税发票OCR文字识别
 * <a href="https://market.aliyun.com/apimarket/detail/cmapi027758">API</a>
 */
public class AliyunOcrInvoice {
    private static final Logger log = LoggerFactory.getLogger(AliyunOcrRollTicket.class);

    public OcrInvoiceResponse request(String appCode, OcrInvoiceRequest req) throws RuntimeException {
        if (appCode.isEmpty() || !req.validate()) {
            throw new RuntimeException("appCode or request is empty");
        }

        try {
            String host = "https://dgfp.market.alicloudapi.com";
            String path = "/ocrservice/invoice";
            String method = "POST";

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "APPCODE " + appCode);
            headers.put("Content-Type", "application/json; charset=UTF-8");
            Map<String, String> querys = new HashMap<String, String>();
            String body = JSON.toJSONString(req);

            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, body);
            System.out.println(response.toString());

            String bodyString = EntityUtils.toString(response.getEntity());
            OcrInvoiceResponse ocrInvoiceResponse = JSONObject.toJavaObject(JSON.parseObject(bodyString), OcrInvoiceResponse.class);
            if (ocrInvoiceResponse == null) {
                log.error("ocrInvoiceResponse is null,response:{}", response);
            }
            return ocrInvoiceResponse;
        } catch (Exception e) {
            log.error("request error", e);
            throw new RuntimeException(e);
        }

    }
}
