package com.fdddf.ocrinvoice.rollticket;

import com.alibaba.fastjson.annotation.JSONField;
import com.netease.lowcode.core.annotation.NaslStructure;


@NaslStructure
public class RollTicketResponse {
    @JSONField(name="data")
    public RollTicketInvoiceData data;
    public Integer angle;
    public Integer height;
    public Integer orgHeight;
    public Integer orgWidth;
    public Integer width;
    public String sid;
}

