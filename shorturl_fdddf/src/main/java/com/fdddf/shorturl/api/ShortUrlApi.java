package com.fdddf.shorturl.api;


import com.fdddf.shorturl.model.Link;
import com.fdddf.shorturl.model.ShortUrlRequest;
import com.fdddf.shorturl.LinkService;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ShortUrlApi {
    @Autowired
    LinkService linkService;

    private static final Logger log = LoggerFactory.getLogger(ShortUrlApi.class);
    private static final Pattern URL_REGEX = Pattern.compile("^(((ht|f)tps?):\\/\\/)?[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$");


    @NaslLogic
    public String generateShortLink(ShortUrlRequest request) {
        if (!checkUrl(request.longUrl)) {
            throw new RuntimeException("Invalid URL");
        }

        if (!request.longUrl.startsWith("http")) {
            request.longUrl = "http://" + request.longUrl;
        }

        linkService = new LinkService();
        Link link = linkService.saveUrlMap(request);

        return link.shortUrl;
    }

    private static Boolean checkUrl(String url) {
        return URL_REGEX.matcher(url).matches();
    }

}
