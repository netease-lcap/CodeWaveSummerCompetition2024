package com.fdddf.shorturl.model;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class ShortUrlRequest {
    /**
     * 长链接
     */
    public String longUrl;

    /**
     * 有效期 天数
     */
    public Integer days;

    /**
     * 最大访问次数
     */
    public Long maxAccessCount;
}
