package com.fdddf.shorturl.model;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.io.Serializable;

@NaslStructure
public class Link implements Serializable {
    public Long id;

    /**
     * 长链接
     */
    public String longUrl;

    /**
     * 短链接
     */
    public String shortCode;

    /**
     * 过期时间
     */
    public String expirationTime;

    /**
     * 最大访问次数
     */
    public Long maxAccessCount;

    /**
     * 已访问次数
     */
    public Long accessCount;
}