package com.netease.http.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NosConfig {

    @Value("${lcp.upload.s3Bucket}")
    public String nosBucket;
    @Value("${lcp.upload.s3AccessKey}")
    public String nosAccessKey;
    @Value("${lcp.upload.s3SecretKey}")
    public String nosSecretKey;
    @Value("${lcp.upload.s3Address}")
    public String nosAddress;
    @Value("${lcp.upload.sinkType}")
    public String sinkType;
}
