package com.fdddf.ocrinvoice.ocrinvoice;

import com.netease.lowcode.core.annotation.NaslStructure;
import com.alibaba.fastjson.annotation.JSONField;


@NaslStructure
public class OcrInvoice {
    @JSONField(name = "angle")
    public Integer angle;

//    @JSONField(name = "codes")
//    public Code[] codes;

    @JSONField(name = "data")
    public OcrInvoiceData data;

    @JSONField(name = "height")
    public Integer height;

    @JSONField(name = "orgHeight")
    public Integer orgHeight;

    @JSONField(name = "orgWidth")
    public Integer orgWidth;

    @JSONField(name = "sid")
    public String sid;

    @JSONField(name = "width")
    public Integer width;

}
