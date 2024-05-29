package com.fdddf.ocrinvoice.ocrinvoice;

import com.alibaba.fastjson.annotation.JSONField;
import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class OcrInvoiceData {
    @JSONField(name="发票代码")
    public String invoiceCode;

    @JSONField(name="发票号码")
    public String invoiceNumber;

    @JSONField(name="开票日期")
    public String invoiceDate;

    @JSONField(name="校验码")
    public String verificationCode;

    @JSONField(name="发票金额")
    public String invoiceAmount;

    @JSONField(name="发票税额")
    public String invoiceTaxAmount;

    @JSONField(name="不含税金额")
    public String amountExcludingTax;

    @JSONField(name="受票方名称")
    public String receiverName;

    @JSONField(name="受票方税号")
    public String receiverTaxNumber;

    @JSONField(name="受票方地址、电话")
    public String receiverAddressAndPhone;

    @JSONField(name="受票方开户行、账号")
    public String receiverBankAndAccount;

    @JSONField(name="销售方名称")
    public String sellerName;

    @JSONField(name="销售方税号")
    public String sellerTaxNumber;

    @JSONField(name="销售方地址、电话")
    public String sellerAddressAndPhone;

    @JSONField(name="销售方开户行、账号")
    public String sellerBankAndAccount;

//    @JSONField(name="发票详单")
//    public InvoiceDetail[] invoiceDetails;

    @JSONField(name="发票代码解析")
    public InvoiceCodeAnalysis invoiceCodeAnalysis;

}