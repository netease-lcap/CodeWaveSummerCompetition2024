package com.fdddf.emailfetcher;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import com.netease.lowcode.core.annotation.NaslStructure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NaslStructure
public class EmailConfig {
    /**
     * 邮箱协议 imap 或 pop3
     */
    @Value("${protocol}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = "imap"))
    public String protocol;

    /**
     * 是否开启ssl true或false
     */
    @Value("${sslEnable}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = "true"))
    public Boolean sslEnable;

    /**
     * 邮箱服务器地址
     */
    @Value("${host}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = "imap.163.com"))
    public String host;

    /**
     * 邮箱服务器端口
     */
    @Value("${port}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = "993"))
    public String port;

    /**
     * 邮箱账号
     */
    @Value("${username}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String username;

    /**
     * 邮箱密码
     */
    @Value("${password}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String password;

    /**
     * OSS配置 域名
     */
    @Value("${ossBucketDomain}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String ossBucketDomain;

    /**
     * OSS配置 endpoint
     */
    @Value("${ossEndpoint}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String ossEndpoint;

    /**
     * OSS配置 accessKeyId
     */
    @Value("${ossAccessKeyId}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String ossAccessKeyId;

    /**
     * OSS配置 accessKeySecret
     */
    @Value("${ossAccessKeySecret}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String ossAccessKeySecret;

    /**
     * OSS配置 bucketName
     */
    @Value("${ossBucketName}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String ossBucketName;

    /**
     * OSS配置 目录名, 可为空
     */
    @Value("${ossFolder}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = ""))
    public String ossFolder;

    @Override
    public String toString() {
        return "EmailConfig{" +
                "protocol='" + protocol + '\'' +
                ", sslEnable=" + sslEnable +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", ossBucketDomain='" + ossBucketDomain + '\'' +
                ", ossEndpoint='" + ossEndpoint + '\'' +
                ", ossAccessKeyId='" + ossAccessKeyId + '\'' +
                ", ossAccessKeySecret='" + ossAccessKeySecret + '\'' +
                ", ossBucketName='" + ossBucketName + '\'' +
                ", ossFolder='" + ossFolder + '\'' +
                '}';
    }

    public Boolean validate() {
        if (this.protocol == null || this.host == null || this.port == null || this.username == null || this.password == null) {
            return false;
        }
        if (!(this.protocol.equals("imap") || this.protocol.equals("pop3"))) {
            return false;
        }
        if (ossBucketDomain == null || ossEndpoint == null || ossAccessKeyId == null || ossAccessKeySecret == null || ossBucketName == null) {
            return false;
        }

        return true;
    }
}
