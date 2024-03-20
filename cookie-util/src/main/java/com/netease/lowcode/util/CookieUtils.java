package com.netease.lowcode.util;

import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.http.NaslCookie;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Cookie工具类 支持常规Cookie操作
 */

public class CookieUtils {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CookieUtils.class);

    /**
     * 写 Cookie 后端逻辑操作
     *
     * @param naslCookie Cookie
     */
    @NaslLogic
    public static Boolean setCookie(NaslCookie naslCookie) {

        if (null == naslCookie.getName() || null == naslCookie.getValue()) {
            return false;
        }

        HttpServletResponse response;

        try {
            // 获取 HttpServletResponse
            response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            if (response == null) {
                log.error("HttpServletResponse 对象为空");
                return false;
            }
            // 编码格式替换为 utf-8，避免乱码
            response.setContentType("text/html;charset=utf-8");
        } catch (Exception e) {
            log.error("获取HttpServletResponse 对象异常: ", e.getMessage());
            return false;
        }

        //创建Cookie对象
        Cookie cookie = new Cookie(naslCookie.getName(), naslCookie.getValue());
        // 如果 path 为 null，使用默认值"/"
        cookie.setPath(naslCookie.getCookiePath() == null ? "/" : naslCookie.getCookiePath());
        if (naslCookie.maxAge != null) {
            cookie.setMaxAge(naslCookie.maxAge);
        }
        if (naslCookie.domain != null && naslCookie.domain.isEmpty()) {
            cookie.setDomain(naslCookie.domain);
        }
        if (naslCookie.secure != null) {
            cookie.setSecure(naslCookie.secure);
        }
        // 如果未设置HttpOnly 则默认为True。增加安全性，防止 JavaScript 访问 Cookie
        cookie.setHttpOnly(naslCookie.getHttpOnly() == null ? Boolean.TRUE : naslCookie.httpOnly);

        try {
            response.addCookie(cookie);
        } catch (Exception e) {
            log.error("写 Cookie 异常: ", e.getMessage());
            return false;
        }
        return true;
    }


    /**
     * 根据Cookie名称得到Cookie对象，不存在该对象则返回Null
     *
     * @param cookieName
     */
    @NaslLogic
    public static NaslCookie getCookie(String cookieName) {
        if (cookieName == null) {
            log.error("CookieName为null");
            return null;
        }

        HttpServletRequest request;

        try {
            //获取HttpServletRequest
            request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            if (request == null) {
                log.error("HttpServletRequest 对象为空");
                return null; // 直接返回 null，避免后续可能的空指针异常
            }
        } catch (Exception e) {
            log.error("HttpServletRequest 对象异常: ", e.getMessage());
            return null;
        }

        Cookie[] cookies = request.getCookies();

        if (Objects.isNull(cookies)) {
            return null;
        }

        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(cookieName)) {
                NaslCookie naslCookie = new NaslCookie();
                naslCookie.setName(cookieName);
                if (Objects.nonNull(cookies[i].getValue())) {
                    naslCookie.setValue(cookies[i].getValue());
                }
                if (Objects.nonNull(cookies[i].getMaxAge())) {
                    naslCookie.setMaxAge(cookies[i].getMaxAge());
                }
                if (Objects.nonNull(cookies[i].getPath())) {
                    naslCookie.setCookiePath(cookies[i].getPath());
                }
                if (Objects.nonNull(cookies[i].getSecure())) {
                    naslCookie.setSecure(cookies[i].getSecure());
                }
                if (Objects.nonNull(cookies[i].getDomain())) {
                    naslCookie.setDomain(cookies[i].getDomain());
                }
                if (Objects.nonNull(cookies[i].isHttpOnly())) {
                    naslCookie.setHttpOnly(cookies[i].isHttpOnly());
                }
                return naslCookie;
            }
        }
        return null;
    }

    /**
     * 清除 Cookie 后端逻辑操作
     *
     * @param cookieName
     */
    @NaslLogic
    public static Boolean clearCookie(String cookieName) {

        if (null == cookieName) {
            log.error("CookieName为空");
            return false;
        }

        HttpServletResponse response;
        HttpServletRequest request;
        try {
            //获取HttpServletResponse
            response = (HttpServletResponse) ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            //获取HttpServletRequest
            request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            if (null == request || null == response) {
                log.error("HttpServletRequest、HttpServletResponse 对象为空");
            }

        } catch (Exception e) {
            log.error("HttpServletRequest、HttpServletResponse 对象异常: ", e.getMessage());
            return Boolean.FALSE;
        }

        Cookie[] cookies = request.getCookies();

        if (Objects.isNull(cookies)) {
            log.error("Cookie列表为空");
            return Boolean.FALSE;
        }

        for (int i = 0; i < cookies.length; i++) {
            if (cookieName.equals(cookies[i].getName())) {
                cookies[i].setMaxAge(0);
                cookies[i].setValue("");
                response.addCookie(cookies[i]);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 根据Cookie名称得到Cookie值 不存在则返回null
     *
     * @param cookieName
     */
    @NaslLogic
    public static String getCookieValue(String cookieName) {

        if (null == cookieName) {
            log.error("CookieName为空");
            return null;
        }

        HttpServletRequest request;

        try {
            //获取HttpServletRequest
            request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            if (request == null) {
                log.error("HttpServletRequest 对象为空");
                return null; // 直接返回 null，避免后续可能的空指针异常
            }
        } catch (Exception e) {
            log.error("HttpServletRequest 对象异常: ", e.getMessage());
            return null;
        }

        Cookie[] cookies = request.getCookies();

        if (Objects.isNull(cookies)) {
            return null;
        }

        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(cookieName)) {
                return cookies[i].getValue();
            }
        }
        return null;
    }

    /**
     * 将所有Cookie封装到Map中返回
     * @return Map<String,NaslCookie>
     */
    @NaslLogic
    public static Map<String, NaslCookie> getCookieMap() {
        Map<String, NaslCookie> cookieMap = new HashMap<String, NaslCookie>();

        HttpServletRequest request;

        try {
            //获取HttpServletRequest
            request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            if (request == null) {
                log.error("HttpServletRequest 对象为空");
                return null; // 直接返回 null，避免后续可能的空指针异常
            }
        } catch (Exception e) {
            log.error("HttpServletRequest 对象异常: ", e.getMessage());
            return null;
        }

        Cookie[] cookies = request.getCookies();


        if (!Objects.isNull(cookies)) {
            for (int i = 0; i < cookies.length; i++) {
                NaslCookie naslCookie = new NaslCookie();
                if (Objects.nonNull(cookies[i].getValue())) {
                    naslCookie.setValue(cookies[i].getValue());
                }
                if (Objects.nonNull(cookies[i].getMaxAge())) {
                    naslCookie.setMaxAge(cookies[i].getMaxAge());
                }
                if (Objects.nonNull(cookies[i].getPath())) {
                    naslCookie.setCookiePath(cookies[i].getPath());
                }
                if (Objects.nonNull(cookies[i].getSecure())) {
                    naslCookie.setSecure(cookies[i].getSecure());
                }
                if (Objects.nonNull(cookies[i].getDomain())) {
                    naslCookie.setDomain(cookies[i].getDomain());
                }
                if (Objects.nonNull(cookies[i].isHttpOnly())) {
                    naslCookie.setHttpOnly(cookies[i].isHttpOnly());
                }
                cookieMap.put(cookies[i].getName(),naslCookie);
                return cookieMap;
            }
        }
        return null;
    }
}
