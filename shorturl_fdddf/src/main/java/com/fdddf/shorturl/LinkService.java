package com.fdddf.shorturl;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.regex.Pattern;

public class LinkService {
    private static final Logger log = LoggerFactory.getLogger(LinkService.class);

    private static final String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

    private static final Pattern URL_REGEX = Pattern.compile("^(((ht|f)tps?):\\/\\/)?[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$");

    /**
     * 校验长链接
     *
     * @param url 长链接
     * @return boolean
     */
    @NaslLogic
    public static Boolean checkUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        return URL_REGEX.matcher(url).matches();
    }

    /**
     * 校验过期时间
     *
     * @param expiredTime 过期时间 格式 yyyy-MM-dd HH:mm:ss
     * @return boolean
     */
    @NaslLogic
    public static Boolean checkExpiredTime(String expiredTime) {
        if (expiredTime == null || expiredTime.isEmpty()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(datetimeFormat);
        try {
            Date date = sdf.parse(expiredTime);
            return date.after(new Date());
        } catch (Exception e) {
            log.error("date time parse failed");
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取过期时间
     *
     * @param days 天数
     * @return string
     */
    @NaslLogic
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