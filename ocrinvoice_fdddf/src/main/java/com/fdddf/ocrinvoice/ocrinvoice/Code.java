package com.fdddf.ocrinvoice.ocrinvoice;

import com.alibaba.fastjson.annotation.JSONField;
import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class Code {
    @JSONField(name = "data")
    public String data;

//    @JSONField(name = "points")
//    public Point[] points;

    @JSONField(name = "type")
    public String type;


}