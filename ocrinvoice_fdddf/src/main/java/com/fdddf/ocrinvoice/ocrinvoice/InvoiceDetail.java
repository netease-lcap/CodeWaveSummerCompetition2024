package com.fdddf.ocrinvoice.ocrinvoice;

import com.alibaba.fastjson.annotation.JSONField;
import com.netease.lowcode.core.annotation.NaslStructure;


@NaslStructure
public class InvoiceDetail {
    @JSONField(name="货物或应税劳务、服务名称")
    public String goodsOrServiceName;

    @JSONField(name="规格型号")
    public String specificationsAndModels;

    @JSONField(name="单位")
    public String unit;

    @JSONField(name="数量")
    public String quantity;

    @JSONField(name="单价")
    public String unitPrice;

    @JSONField(name="金额")
    public String amount;

    @JSONField(name="税率")
    public String taxRate;

    @JSONField(name="税额")
    public String taxAmount;
}