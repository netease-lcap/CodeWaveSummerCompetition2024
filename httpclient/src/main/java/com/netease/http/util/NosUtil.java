package com.netease.http.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.netease.http.config.NosConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Component
public class NosUtil {

    private static final String S3_TYPE = "s3";
    public static NosConfig nosConfig;
    private static volatile AmazonS3 s3Client;

    public static List<Bucket> listBuckets() {
        return s3Client.listBuckets();
    }

    public static void put(String key, InputStream inputStream) {
        init();
        PutObjectRequest putObjectRequest = new PutObjectRequest(nosConfig.nosBucket, key, inputStream, new ObjectMetadata());
        s3Client.putObject(putObjectRequest);
    }

    public static void put(String key, File file) {
        init();
        s3Client.putObject(nosConfig.nosBucket, key, file);
    }

    public static void delete(String key) {
        s3Client.deleteObject(nosConfig.nosBucket, key);
    }

    public static String generateUrl(String key) {
        init();
        // 生成下载的url
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(nosConfig.nosBucket, key);
        // 设置可下载URL的过期时间为1天后
        generatePresignedUrlRequest.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60));
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);//生成URL

        return url.toString();
    }

    public static S3Object get(String key) {
        return s3Client.getObject(nosConfig.nosBucket, key);
    }

    public static void listObjects() {
        ObjectListing objectListing = s3Client.listObjects(nosConfig.nosBucket);
    }

    public static void init() {
        if (s3Client == null) {
            synchronized (NosUtil.class) {
                if (s3Client == null) {
                    s3Client = newS3Client();
                }
            }
        }
    }

    private static AmazonS3 newS3Client() {
        AmazonS3 client = null;
        if (!StringUtils.isEmpty(nosConfig.nosAccessKey) && !StringUtils.isEmpty(nosConfig.nosSecretKey) && !StringUtils.isEmpty(nosConfig.nosAddress)) {
            AWSCredentials credentials = new BasicAWSCredentials(nosConfig.nosAccessKey, nosConfig.nosSecretKey);

            client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withPathStyleAccessEnabled(S3_TYPE.equals(nosConfig.sinkType))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(nosConfig.nosAddress, "eastchina1"))
                    .withChunkedEncodingDisabled(true)
                    .build();
        }

        return client;
    }

    @Autowired
    public void setNocConfig(NosConfig nosConfig) {
        NosUtil.nosConfig = nosConfig;
    }
}
