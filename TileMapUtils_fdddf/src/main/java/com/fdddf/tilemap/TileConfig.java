package com.fdddf.tilemap;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import com.netease.lowcode.core.annotation.NaslStructure;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
@NaslStructure
public class TileConfig {
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
     * 图片最大宽度
     */
    @Value("${imageMaxWidth}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = "4096"))
    public Integer imageMaxWidth = 4096;

    /**
     * 图片最大高度
     */
    @Value("${imageMaxHeight}")
    @NaslConfiguration(defaultValue = @Environment(type = EnvironmentType.DEV, value = "4096"))
    public Integer imageMaxHeight = 4096;
}
