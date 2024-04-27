package com.fdddf.shorturl;

import com.fdddf.shorturl.model.Link;
import com.fdddf.shorturl.model.ShortUrlRequest;
import com.fdddf.shorturl.utils.HashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Pattern;

@Service
public class LinkService {
    private static final Logger log = LoggerFactory.getLogger(LinkService.class);

    private static final String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private LinkRepository linkRepository;

    private static final Pattern URL_REGEX = Pattern.compile("^(((ht|f)tps?):\\/\\/)?[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$");

    private static Boolean checkUrl(String url) {
        return URL_REGEX.matcher(url).matches();
    }

    public String getLongUrl(String shortCode) {
        Link link = linkRepository.findByShortCode(shortCode);
        if (link == null) {
            return null;
        }
        // 判断是否超过最大访问次数
        if (link.maxAccessCount > 0 && link.accessCount + 1 > link.maxAccessCount) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(datetimeFormat);
        try {
            // 判断是否过期
            if (sdf.parse(link.expirationTime).before(new Date())) {
                return null;
            }
        } catch (Exception e) {
            log.error("date time parse failed");
//            e.printStackTrace();
        }
        return link.longUrl;
    }

    public void updateAccessCount(String shortURL) {
        linkRepository.incrementAccessCountByShortUrl(shortURL);
    }

    public Link saveUrlMap(ShortUrlRequest request) {
        if (!checkUrl(request.longUrl)) {
            throw new RuntimeException("Invalid URL");
        }

        if (!request.longUrl.startsWith("http")) {
            request.longUrl = "http://" + request.longUrl;
        }

        String shortCode = HashUtils.hashToBase62(request.longUrl);
        // check exist in db
        Link link = linkRepository.findByShortCode(shortCode);
        if (link != null) {
            throw new DuplicateKeyException("the short url exists");
        }

        link = new Link();
        link.shortCode = shortCode;
        link.accessCount = 0L;
        link.maxAccessCount = request.maxAccessCount;
        link.longUrl = request.longUrl;
        link.expirationTime = getExpiredTime(request.days);
        return linkRepository.save(link);
    }

    public static String getExpiredTime(Integer days) {
        LocalDateTime now = LocalDateTime.now();
        // Add seconds
        int secondsToAdd = 3600*24*days; // Change this to the number of seconds you want to add
        LocalDateTime futureTime = now.plusSeconds(secondsToAdd);

        // Format the future time as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datetimeFormat);
        return futureTime.format(formatter);
    }

}