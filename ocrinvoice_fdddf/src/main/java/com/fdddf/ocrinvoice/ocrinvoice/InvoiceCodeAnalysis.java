package com.fdddf.ocrinvoice.ocrinvoice;

import com.alibaba.fastjson.annotation.JSONField;
import com.netease.lowcode.core.annotation.NaslStructure;


@NaslStructure
public class InvoiceCodeAnalysis {
    @JSONField(name="批次号")
    public String batchNumber;

    @JSONField(name="年份")
    public String year;

    @JSONField(name="税务局代码")
    public String taxOfficeCode;

    @JSONField(name="发票行业代码")
    public String invoiceIndustryCode;

    @JSONField(name="金额版")
    public String amountEdition;

    @JSONField(name="行政区划代码")
    public String administrativeRegionCode;

    @JSONField(name="发票类别代码")
    public String invoiceCategoryCode;
}