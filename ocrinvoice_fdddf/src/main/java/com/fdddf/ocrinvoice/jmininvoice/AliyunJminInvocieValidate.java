package com.fdddf.ocrinvoice.jmininvoice;

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
 * 【聚美智数】发票查验-增值税发票查验
 * <a href="https://market.aliyun.com/apimarket/detail/cmapi00050226">API</a>
 */
public class AliyunJminInvocieValidate {
    private static final Logger log = LoggerFactory.getLogger(AliyunOcrRollTicket.class);

    /**
     * @param appCode appCode
     * @param req InvoiceCheckRequest
     * @return InvoiceCheckResponse
     */
    public InvoiceCheckResponse request(String appCode, InvoiceCheckRequest req) throws RuntimeException {
        if (appCode.isEmpty() || !req.validate()) {
            throw new RuntimeException("appCode、fphm、fprq不能为空");
        }
        try {
            String host = "https://jminvoice.market.alicloudapi.com";
            String path = "/invoice/validate";
            String method = "POST";
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "APPCODE " + appCode);
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            Map<String, String> querys = new HashMap<String, String>();
            Map<String, String> bodys = new HashMap<String, String>();
            bodys.put("bhsje", req.bhsje);
            bodys.put("fpdm", req.fpdm);
            bodys.put("fphm", req.fphm);
            bodys.put("kprq", req.kprq);
            bodys.put("xym", req.xym);

            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            String bodyString = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(bodyString);
            return JSONObject.toJavaObject(JSON.parseObject(bodyString), InvoiceCheckResponse.class);
        } catch (Exception e) {
            log.error("request error", e);
            throw new RuntimeException(e);
        }
    }
}
