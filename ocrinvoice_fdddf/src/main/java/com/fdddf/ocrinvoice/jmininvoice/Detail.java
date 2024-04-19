package com.fdddf.ocrinvoice.jmininvoice;

import com.alibaba.fastjson.annotation.JSONField;
import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class Detail {
    @JSONField(name = "unitPrice")
    public String unitPrice;

    @JSONField(name = "taxRate")
    public String taxRate;

    @JSONField(name = "taxUnitPrice")
    public String taxUnitPrice;

    @JSONField(name = "unit")
    public String unit;

    @JSONField(name = "expenseItem")
    public String expenseItem;

    @JSONField(name = "plateNo")
    public String plateNo;

    @JSONField(name = "type")
    public String type;

    @JSONField(name = "trafficDateStart")
    public String trafficDateStart;

    @JSONField(name = "trafficDateEnd")
    public String trafficDateEnd;

    @JSONField(name = "specificationModel")
    public String specificationModel;

    @JSONField(name = "num")
    public String num;

    @JSONField(name = "detailNo")
    public String detailNo;

    @JSONField(name = "detailAmount")
    public String detailAmount;

    @JSONField(name = "taxAmount")
    public String taxAmount;

    @JSONField(name = "goodsName")
    public String goodsName;

    @JSONField(name = "flbm")
    public String flbm;

    @JSONField(name = "taxDetailAmount")
    public String taxDetailAmount;
}