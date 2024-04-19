package com.fdddf.ocrinvoice;

import com.fdddf.ocrinvoice.jmininvoice.AliyunJminInvocieValidate;
import com.fdddf.ocrinvoice.jmininvoice.InvoiceCheckResult;
import com.fdddf.ocrinvoice.ocrinvoice.AliyunOcrInvoice;
import com.fdddf.ocrinvoice.ocrinvoice.OcrInvoice;
import com.fdddf.ocrinvoice.rollticket.AliyunOcrRollTicket;
import com.fdddf.ocrinvoice.rollticket.RollTicketInvoice;
import com.netease.lowcode.core.annotation.NaslConnector;

import java.util.function.Function;

import java.io.IOException;

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
     * @param url 图片地址
     * @return RollTicketInvoice
     */
    @NaslConnector.Logic
    public RollTicketInvoice rollTicketOcr(String url) {
        AliyunOcrRollTicket aliyunOcrRollTicket = new AliyunOcrRollTicket();
        return aliyunOcrRollTicket.request(this.appCode, url);
    }

    /**
     * 发票识别
     *
     * @param url 图片地址
     * @return OcrInvoice
     */
    @NaslConnector.Logic
    public OcrInvoice invoiceOcr(String url) {
        AliyunOcrInvoice aliyunOcrInvoice = new AliyunOcrInvoice();
        return aliyunOcrInvoice.request(this.appCode, url);
    }

    /**
     * 发票校验
     *
     * @param fphm  发票号码 必填
     * @param kprq  发票日期 必填
     * @param fpdm  发票代码
     * @param xym   校验码
     * @param bhsje 不含税金额
     * @return InvoiceCheckResult
     */
    @NaslConnector.Logic
    public InvoiceCheckResult invoiceCheck(String fphm, String kprq, String fpdm, String xym, String bhsje) {
        AliyunJminInvocieValidate aliyunJminInvocieValidate = new AliyunJminInvocieValidate();
        return aliyunJminInvocieValidate.request(this.appCode, fphm, kprq, fpdm, xym, bhsje);
    }

    @NaslConnector.Trigger
    public void testTrigger(String msg, Function<String, String> handleMsg) {
        handleMsg.apply(msg);
    }

    public static void main(String[] args) {
        System.out.println("hello world");

        InvoiceOcrConnector connector = new InvoiceOcrConnector().initBean("ffbd4a857cf045219e2cb6a8970c0f56");
        RollTicketInvoice result = connector.rollTicketOcr("https://lankuaiji.cn/img/roll_ticket.jpg");
        System.out.println(result.data.invoiceNumber);

        OcrInvoice result2 = connector.invoiceOcr("https://fapiao.youshang.com/zx/wp-content/uploads/2020/01/20160831113346-1157589472.png");
        System.out.println(result2.data.invoiceCode);

        InvoiceCheckResult result3 = connector.invoiceCheck("24617000000027471236", "20240418", "", "", "55.93");
        System.out.println(result3.ret_message);
        System.out.println(result3.code);

        connector.testTrigger("123", new Function<String, String>() {
            @Override
            public String apply(String s) {
                return null;
            }
        });
    }
}

