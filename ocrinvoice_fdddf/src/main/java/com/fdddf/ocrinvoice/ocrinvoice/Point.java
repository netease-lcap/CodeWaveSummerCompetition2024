package com.fdddf.ocrinvoice.ocrinvoice;

import com.alibaba.fastjson.annotation.JSONField;
import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class Point
{
    @JSONField(name = "x")
    public Integer x;

    @JSONField(name = "y")
    public Integer y;
}