package com.fdddf.ocrinvoice.jmininvoice;

import com.alibaba.fastjson.annotation.JSONField;
import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class InvoiceCheckResult {
    @JSONField(name = "data")
    public Data data;

    @JSONField(name = "msg")
    public String ret_message;

    @JSONField(name = "code")
    public Integer code;

    @JSONField(name = "taskNo")
    public String taskNo;

}
