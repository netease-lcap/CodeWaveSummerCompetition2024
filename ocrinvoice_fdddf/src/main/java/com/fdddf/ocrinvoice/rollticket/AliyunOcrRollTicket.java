package com.fdddf.ocrinvoice.rollticket;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fdddf.ocrinvoice.utils.HttpUtils;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;


/**
 * 【阿里官方】增值税发票卷票OCR文字识别
 * <a href="https://market.aliyun.com/apimarket/detail/cmapi00042852">API</a>
 */
public class AliyunOcrRollTicket {

    private static final Logger log = LoggerFactory.getLogger(AliyunOcrRollTicket.class);

    public RollTicketResponse request(String appCode, RollTicketRequest req) throws RuntimeException {
        if (appCode.isEmpty() || !req.validate()) {
            log.error("appCode or url in request is empty");
            throw new RuntimeException("appCode or url is empty");
        }

        try {
            String host = "https://rollticket.market.alicloudapi.com";
            String path = "/ocrservice/rollTicket";
            String method = "POST";

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "APPCODE " + appCode);
            headers.put("Content-Type", "application/json; charset=UTF-8");
            Map<String, String> querys = new HashMap<String, String>();
            String body = JSON.toJSONString(req);

            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, body);
            System.out.println(response.toString());

            String bodyString = EntityUtils.toString(response.getEntity());
            return JSONObject.toJavaObject(JSON.parseObject(bodyString), RollTicketResponse.class);
        } catch (Exception e) {
            log.error("request error", e);;
            throw new RuntimeException(e);
        }
    }
}

