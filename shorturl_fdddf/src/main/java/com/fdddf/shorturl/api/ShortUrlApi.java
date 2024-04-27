package com.fdddf.shorturl.api;


import com.fdddf.shorturl.model.Link;
import com.fdddf.shorturl.model.ShortUrlRequest;
import com.fdddf.shorturl.LinkService;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShortUrlApi {

    @Autowired
    private LinkService linkService;

    private static final Logger logger = LoggerFactory.getLogger(ShortUrlApi.class);

    /**
     * 生成短链接即哈希值
     * @param request ShortUrlRequest
     * @return String
     */
    @NaslLogic
    public String generateShortCode(ShortUrlRequest request) {
        Link link = linkService.saveUrlMap(request);
        return link.shortCode;
    }

    /**
     * 获取长链接即原始链接
     * @param shortCode String
     * @return 长链接
     */
    @NaslLogic
    public String getLongUrl(String shortCode) {
        return linkService.getLongUrl(shortCode);
    }

}
