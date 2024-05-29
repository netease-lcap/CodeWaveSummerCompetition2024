package com.fdddf.ocrinvoice.rollticket;

import com.alibaba.fastjson.annotation.JSONField;
import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class RollTicketInvoiceDetail {
    @JSONField(name="项目")
    public String item;

    @JSONField(name="数量")
    public String quantity;

    @JSONField(name="单价")
    public String unitPrice;

    @JSONField(name="金额")
    public String amount;
}