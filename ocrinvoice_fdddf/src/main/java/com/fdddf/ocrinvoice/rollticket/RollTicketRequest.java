package com.fdddf.ocrinvoice.rollticket;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class RollTicketRequest {
    /**
     * 图像数据：base64编码，要求base64编码后大小不超过4M，最短边至少15px，最长边最大4096px，
     * 支持jpg/png/bmp格式，和url参数只能同时存在一个
     */
    public String img;

    /**
     * 图像url地址：图片完整URL，URL长度不超过1024字节，URL对应的图片base64编码后大小不超过4M，
     * 最短边至少15px，最长边最大4096px，支持jpg/png/bmp格式，和img参数只能同时存在一个
     */
    public String url;

    public RollTicketRequest() {
    }

    public RollTicketRequest(String url, String img) {
        this.img = img;
        this.url = url;
    }
    public Boolean validate() {
        return img != null || url != null;
    }
}
