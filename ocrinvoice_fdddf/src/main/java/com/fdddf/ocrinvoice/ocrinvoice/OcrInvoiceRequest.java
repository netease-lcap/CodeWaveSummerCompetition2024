package com.fdddf.ocrinvoice.ocrinvoice;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class OcrInvoiceRequest {

    /**
     * 图像或PDF数据：base64编码，要求base64编码后大小不超过10M，
     * 最短边至少15px，最长边最大8192px，支持jpg/png/bmp格式，和url参数只能同时存在一个
     */
    public String img;


    /**
     *  图像或PDF的url地址：图片/PDF完整URL，URL长度不超过1024字节，
     *  URL对应的图片base64编码后大小不超过10M，最短边至少15px，最长边最大8192px，支持jpg/png/bmp格式，和img参数只能同时存在一个
     */
    public String url;

    /**
     * 如果识别的是PDF/OFD文件，指定PDF/OFD文件页码。默认识别第1页。
     */
    public Integer page_no = 1;


    public OcrInvoiceRequest() {
    }

    public OcrInvoiceRequest(String url, String img, Integer pdf_no) {
        this.img = img;
        this.url = url;
        this.page_no = pdf_no;
    }

    public Boolean validate() {
        return img != null || url != null;
    }

}
