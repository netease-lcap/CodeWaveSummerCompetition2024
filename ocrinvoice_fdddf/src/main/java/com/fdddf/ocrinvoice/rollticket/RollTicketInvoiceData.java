package com.fdddf.ocrinvoice.rollticket;

import com.alibaba.fastjson.annotation.JSONField;
import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class RollTicketInvoiceData {
    @JSONField(name="发票代码")
    public String invoiceCode;

    @JSONField(name="发票号码")
    public String invoiceNumber;

    @JSONField(name="机打号码")
    public String machineNumber;

    @JSONField(name="机器编号")
    public String machineID;

    @JSONField(name="销售方名称")
    public String sellerName;

    @JSONField(name="销售方税号")
    public String sellerTaxNumber;

    @JSONField(name="购买方名称")
    public String buyerName;

    @JSONField(name="总价")
    public String totalPrice;

    @JSONField(name="购买方税号")
    public String buyerTaxNumber;

    @JSONField(name="开票日期")
    public String invoiceDate;

    @JSONField(name="收款员")
    public String cashier;

    @JSONField(name="合计金额(大写)")
    public String totalAmountWords;

    @JSONField(name="合计金额(小写)")
    public String totalAmountFigures;

    @JSONField(name="校验码")
    public String verificationCode;

    // Adding field for invoice details
//    @JSONField(name="发票详单")
//    public RollTicketInvoiceDetail[] invoiceDetails;
}