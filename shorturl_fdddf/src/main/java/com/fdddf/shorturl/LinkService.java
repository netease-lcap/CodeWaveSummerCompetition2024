package com.fdddf.shorturl;

import com.fdddf.shorturl.api.ShortUrlApi;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class LinkService {
    private static final Logger log = LoggerFactory.getLogger(LinkService.class);

    private final String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private LinkRepository linkRepository;

    private static final long timeout = 10;

    public String getLongUrl(String shortURL) {
        // 查找Redis中是否有缓存
        String longUrl = redisTemplate.opsForValue().get(shortURL);
        if (longUrl != null) {
            // 有缓存,增加缓存时间
            redisTemplate.expire(shortURL, timeout, TimeUnit.MINUTES);
            return longUrl;
        }
        // Redis没有缓存,查数据库
        Link link = linkRepository.findByShortUrl(shortURL);
        if (link == null) {
            return null;
        }
        if (link.accessCount + 1 > link.maxAccessCount) {
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
        longUrl = link.longUrl;
        // 将短链接添加缓存
        redisTemplate.opsForValue().set(shortURL,longUrl,timeout,TimeUnit.MINUTES);
        return longUrl;
    }

    public void updateAccessCount(String shortURL) {
        linkRepository.incrementAccessCountByShortUrl(shortURL);
    }

    public Link saveUrlMap(ShortUrlRequest request) {
        String shortURL = HashUtils.hashToBase62(request.longUrl);
        // check exist in db
        Link link = linkRepository.findByShortUrl(shortURL);
        if (link != null) {
            throw new DuplicateKeyException("the short url exists");
        }

        link = new Link();
        link.shortUrl = shortURL;
        link.accessCount = 0L;
        link.maxAccessCount = request.maxAccessCount;
        link.longUrl = request.longUrl;
        link.expirationTime = getExpiredTime(request.days);
        linkRepository.save(link);

        // 添加缓存
        redisTemplate.opsForValue().set(shortURL,request.longUrl,timeout,TimeUnit.MINUTES);

        return link;
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