package com.fdddf.ocrinvoice.jmininvoice;

import com.alibaba.fastjson.annotation.JSONField;
import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class Data {
    @JSONField(name = "result")
    public Integer result;

    @JSONField(name = "message")
    public String ret_message;

    @JSONField(name = "info")
    public Info info;

}