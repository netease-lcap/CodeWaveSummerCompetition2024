package com.fdddf.ocrinvoice.jmininvoice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fdddf.ocrinvoice.rollticket.AliyunOcrRollTicket;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 【聚美智数】发票查验-增值税发票查验
 * <a href="https://market.aliyun.com/apimarket/detail/cmapi00050226">API</a>
 */
public class AliyunJminInvocieValidate {
    private static final Logger log = LoggerFactory.getLogger(AliyunOcrRollTicket.class);

    /**
     * @param appCode appCode
     * @param fphm    发票号码
     * @param kprq    开票日期 格式YYYYMMDD
     * @param fpdm    发票代码 【注意：全电票可不传，其他必传】
     * @param xym     校验码 【注意：专票、全电票可不传，其他必填。校验码支持全位和后6位】
     * @param bhsje   不含税金额 【注意：普票可不传，其他发票必填 。全电票请传含税金额，其他发票需传 不含税金额】
     * @return InvoiceCheckResult
     */
    public InvoiceCheckResult request(String appCode, String fphm, String kprq,
                                      String fpdm, String xym, String bhsje) throws RuntimeException {

        if (appCode.isEmpty() || fphm.isEmpty() || kprq.isEmpty()) {
            throw new RuntimeException("appCode、fphm、fprq不能为空");
        }

        try {
            OkHttpClient client = new OkHttpClient();
            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("fpdm", fpdm);
            formBuilder.add("fphm", fphm);
            formBuilder.add("kprq", kprq);
            formBuilder.add("xym", xym);
            formBuilder.add("bhsje", bhsje);

            RequestBody requestBody = formBuilder.build();
            Request request = new Request.Builder()
                    .url("https://jminvoice.market.alicloudapi.com/invoice/validate")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Authorization", "APPCODE " + appCode)
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();

            assert body != null;
            String bodyString = body.string();
            System.out.printf("API response body %s\n%d\n%s", bodyString, response.code(), response.message());
            log.info("API response body {}, response code {}", bodyString, response.code());
            return JSONObject.toJavaObject(JSON.parseObject(bodyString), InvoiceCheckResult.class);
        } catch (IOException e) {
            System.out.printf("API request failed %s\n", e);
            log.info("API request exception: {}", e.getMessage());
            return null;
        }
    }
}
