package com.fdddf.shorturl;

import com.fdddf.shorturl.model.Link;
import com.fdddf.shorturl.model.ShortUrlRequest;
import com.fdddf.shorturl.utils.HashUtils;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.function.Function;
import java.util.regex.Pattern;

public class LinkService {
    private static final Logger log = LoggerFactory.getLogger(LinkService.class);

    private static final String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

    private static final Pattern URL_REGEX = Pattern.compile("^(((ht|f)tps?):\\/\\/)?[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$");

    private static Boolean checkUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        return URL_REGEX.matcher(url).matches();
    }

    /**
     * 根据短链接获取长链接
     *
     * @param shortCode           短链接码
     * @param findLinkByShortCode 根据短链接码查询逻辑
     * @param updateLink          更新短链接逻辑
     * @return string
     */
    @NaslLogic
    public static String getLongUrl(String shortCode, Function<String, Link> findLinkByShortCode,
                             Function<Link, Boolean> updateLink) {
        if (shortCode == null || shortCode.isEmpty()) {
            return null;
        }
        Link link = findLinkByShortCode.apply(shortCode);
        if (link == null) {
            log.error("shortCode not found");
            return null;
        }
        // 判断是否超过最大访问次数
        if (link.maxAccessCount > 0 && link.accessCount + 1 > link.maxAccessCount) {
            log.error("max access count exceeded");
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(datetimeFormat);
        try {
            // 判断是否过期
            if (sdf.parse(link.expirationTime).before(new Date())) {
                log.error("expired");
                return null;
            }
        } catch (Exception e) {
            log.error("date time parse failed");
            e.printStackTrace();
        }
        if (!updateLink.apply(link)) {
            log.error("update access count failed");
        }
        return link.longUrl;
    }


    /**
     * 保存短链接
     *
     * @param request                ShortUrlRequest
     * @param saveShortUrlLogic      保存短链接到数据库逻辑
     * @param checkLongUrlExistLogic 检查长链接是否存在逻辑
     * @return Link
     */
    @NaslLogic
    public static Link saveUrlMap(ShortUrlRequest request,
                                  Function<Link, Link> saveShortUrlLogic,
                                  Function<String, Boolean> checkLongUrlExistLogic
    ) throws LinkRuntimeException, LinkDuplicateException {
        if (request == null || !checkUrl(request.longUrl)) {
            throw new LinkRuntimeException("Invalid URL");
        }

        if (!request.longUrl.startsWith("http")) {
            request.longUrl = "http://" + request.longUrl;
        }

        String shortCode = HashUtils.hashToBase62(request.longUrl);
        // check exist in db
        if (checkLongUrlExistLogic.apply(request.longUrl)) {
            throw new LinkDuplicateException("the long url exists");
        }

        Link link = new Link();
        link.shortCode = shortCode;
        link.accessCount = 0L;
        link.maxAccessCount = request.maxAccessCount;
        link.longUrl = request.longUrl;
        link.expirationTime = getExpiredTime(request.days);
        return saveShortUrlLogic.apply(link);
    }

    public static String getExpiredTime(Integer days) {
        LocalDateTime now = LocalDateTime.now();
        // Add seconds
        int secondsToAdd = 3600 * 24 * days; // Change this to the number of seconds you want to add
        LocalDateTime futureTime = now.plusSeconds(secondsToAdd);

        // Format the future time as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datetimeFormat);
        return futureTime.format(formatter);
    }

}