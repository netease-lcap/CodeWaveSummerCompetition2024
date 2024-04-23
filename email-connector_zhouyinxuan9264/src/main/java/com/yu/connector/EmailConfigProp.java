package com.yu.connector;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/23 14:54
 **/
@Getter
@Setter
@Component
public class EmailConfigProp {
    /**
     * 你的邮箱账号 必填
     */
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = "xxx@qq.com")
    })
    @Value("${account}")
    private String account;
    /**
     * 你授权给第三方的授权码
     */
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = "xxxx")
    })
    @Value("${authCode}")
    private String authCode;
    /**
     * 你的邮箱的smtp服务器地址
     */
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = "smtp.qq.com")
    })
    @Value("${smtpHost}")
    private String smtpHost;

    /**
     * imap服务器地址
     */
    @NaslConfiguration(defaultValue = {
            @Environment(type = EnvironmentType.DEV, value = "imap.qq.com")
    })
    @Value("${imapHost}")
    private String imapHost;
}
