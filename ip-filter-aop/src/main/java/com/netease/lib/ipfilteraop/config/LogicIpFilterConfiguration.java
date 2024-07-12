package com.netease.lib.ipfilteraop.config;

import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicIpFilterConfiguration {

    /**
     * 开启开始时间
     */
    @NaslConfiguration
    @Value("${forbidTimeStart}")
    private String forbidTimeStart;

    /**
     * 开启结束时间
     */
    @NaslConfiguration
    @Value("${forbidTimeEnd}")
    private String forbidTimeEnd;

    /**
     * 允许IP 中间用 ; 分割
     */

    @NaslConfiguration
    @Value("${allowedIp}")
    private String allowedIp;

    /**
     * 禁用IP 中间用 ; 分割
     */
    @NaslConfiguration
    @Value("${forbidIp}")
    private String forbidIp;

    public String getForbidTimeStart() {
        return forbidTimeStart;
    }

    public void setForbidTimeStart(String forbidTimeStart) {
        this.forbidTimeStart = forbidTimeStart;
    }

    public String getForbidTimeEnd() {
        return forbidTimeEnd;
    }

    public void setForbidTimeEnd(String forbidTimeEnd) {
        this.forbidTimeEnd = forbidTimeEnd;
    }

    public String getAllowedIp() {
        return allowedIp;
    }

    public void setAllowedIp(String allowedIp) {
        this.allowedIp = allowedIp;
    }

    public String getForbidIp() {
        return forbidIp;
    }

    public void setForbidIp(String forbidIp) {
        this.forbidIp = forbidIp;
    }
}
