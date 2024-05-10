package com.fdddf.ocrinvoice;

import com.fdddf.ocrinvoice.jmininvoice.AliyunJminInvocieValidate;
import com.fdddf.ocrinvoice.jmininvoice.InvoiceCheckRequest;
import com.fdddf.ocrinvoice.jmininvoice.InvoiceCheckResponse;
import com.fdddf.ocrinvoice.ocrinvoice.AliyunOcrInvoice;
import com.fdddf.ocrinvoice.ocrinvoice.OcrInvoiceRequest;
import com.fdddf.ocrinvoice.ocrinvoice.OcrInvoiceResponse;
import com.fdddf.ocrinvoice.rollticket.AliyunOcrRollTicket;
import com.fdddf.ocrinvoice.rollticket.RollTicketResponse;
import com.fdddf.ocrinvoice.rollticket.RollTicketRequest;
import com.netease.lowcode.core.annotation.NaslConnector;

import java.util.function.Function;

@NaslConnector(connectorKind = "invoiceOcr")
public class InvoiceOcrConnector {
    private String appCode;

    @NaslConnector.Creator
    public InvoiceOcrConnector initBean(String appCode) {
        InvoiceOcrConnector connector = new InvoiceOcrConnector();
        connector.appCode = appCode;
        return connector;
    }

    @NaslConnector.Tester
    public Boolean test(String appCode) {
        return null != appCode;
    }

    /**
     * 卷票识别
     *
     * @param request RollTicketRequest
     * @return RollTicketInvoice
     */
    @NaslConnector.Logic
    public RollTicketResponse rollTicketOcr(RollTicketRequest request) {
        AliyunOcrRollTicket aliyunOcrRollTicket = new AliyunOcrRollTicket();
        return aliyunOcrRollTicket.request(this.appCode, request);
    }

    /**
     * 发票识别
     *
     * @param request OcrInvoiceRequest
     * @return OcrInvoiceResponse
     */
    @NaslConnector.Logic
    public OcrInvoiceResponse invoiceOcr(OcrInvoiceRequest request) {
        AliyunOcrInvoice aliyunOcrInvoice = new AliyunOcrInvoice();
        return aliyunOcrInvoice.request(this.appCode, request);
    }

    /**
     * 发票校验
     *
     * @param req InvoiceCheckRequest
     * @return InvoiceCheckResult
     */
    @NaslConnector.Logic
    public InvoiceCheckResponse invoiceCheck(InvoiceCheckRequest req) {
        AliyunJminInvocieValidate aliyunJminInvocieValidate = new AliyunJminInvocieValidate();
        return aliyunJminInvocieValidate.request(this.appCode, req);
    }

    /**
     * 测试触发器，暂无用
     *
     * @param msg      String
     * @param handleMsg Function<String, String>
     */
    @NaslConnector.Trigger
    public void testTrigger(String msg, Function<String, String> handleMsg) {
        handleMsg.apply(msg);
    }

    public static void main(String[] args) {
        System.out.println("hello world");

        InvoiceOcrConnector connector = new InvoiceOcrConnector().initBean("ffbd4a857cf045219e2cb6a8970c0f56");
        RollTicketRequest request = new RollTicketRequest("https://lankuaiji.cn/img/roll_ticket.jpg", "");
        RollTicketResponse result = connector.rollTicketOcr(request);
        System.out.println(result.data.invoiceNumber);
        System.out.println(result.data.buyerName);
        System.out.println(result.data.sellerName);

        OcrInvoiceRequest request2 = new OcrInvoiceRequest("https://fapiao.youshang.com/zx/wp-content/uploads/2020/01/20160831113346-1157589472.png", "", 1);
        OcrInvoiceResponse result2 = connector.invoiceOcr(request2);
        System.out.println(result2.data.invoiceCode);
        System.out.println(result2.data.invoiceNumber);
        System.out.println(result2.data.invoiceDate);

        InvoiceCheckRequest request3 = new InvoiceCheckRequest("24617000000027471236", "20240418", "", "", "55.93");
        InvoiceCheckResponse result3 = connector.invoiceCheck(request3);
        System.out.println(result3.ret_message);
        System.out.println(result3.code);
        System.out.println(result3.data.result);

        connector.testTrigger("123", new Function<String, String>() {
            @Override
            public String apply(String s) {
                return null;
            }
        });
    }
}

