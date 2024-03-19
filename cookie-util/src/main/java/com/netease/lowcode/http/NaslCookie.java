package com.netease.lowcode.http;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * @Date: 2024/3/17 - 03 - 17 - 22:31
 * @Description: com.netease.lowcode.http
 * @version: 1.0
 */
@NaslStructure
public class NaslCookie {

    public String name;
    public String value;
    public String domain;
    public String cookiePath;
    public Boolean httpOnly;
    public Boolean secure;
    public Integer maxAge;

    public NaslCookie() {
    }

    public NaslCookie(String name, String value, String domain, String cookiePath, Boolean httpOnly, Boolean secure, Integer maxAge) {
        this.name = name;
        this.value = value;
        this.domain = domain;
        this.cookiePath = cookiePath;
        this.httpOnly = httpOnly;
        this.secure = secure;
        this.maxAge = maxAge;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取
     * @return domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * 设置
     * @param domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * 获取
     * @return cookiePath
     */
    public String getCookiePath() {
        return cookiePath;
    }

    /**
     * 设置
     * @param cookiePath
     */
    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    /**
     * 获取
     * @return httpOnly
     */
    public Boolean getHttpOnly() {
        return httpOnly;
    }

    /**
     * 设置
     * @param httpOnly
     */
    public void setHttpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    /**
     * 获取
     * @return secure
     */
    public Boolean getSecure() {
        return secure;
    }

    /**
     * 设置
     * @param secure
     */
    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    /**
     * 获取
     * @return maxAge
     */
    public Integer getMaxAge() {
        return maxAge;
    }

    /**
     * 设置
     * @param maxAge
     */
    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public String toString() {
        return "NaslCookie{name = " + name + ", value = " + value + ", domain = " + domain + ", cookiePath = " + cookiePath + ", httpOnly = " + httpOnly + ", secure = " + secure + ", maxAge = " + maxAge + "}";
    }
}
