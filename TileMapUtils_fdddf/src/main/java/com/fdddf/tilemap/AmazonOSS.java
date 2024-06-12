package com.fdddf.tilemap;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class AmazonOSS {

    private final String accessKeyId;
    private final String accessKeySecret;
    private final String endpoint;
    private final String bucketName;
    private final String bucketDomain;
    private final AmazonS3 s3Client;

    private static final Logger logger = LoggerFactory.getLogger(AmazonOSS.class);

    public AmazonOSS(String accessKeyId, String accessKeySecret, String endpoint, String bucketName, String bucketDomain) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        if (!endpoint.startsWith("http")) {
            endpoint = "https://" + endpoint;
        }
        if (!bucketDomain.startsWith("http")) {
            bucketDomain = "https://" + bucketDomain;
        }
        this.bucketDomain = bucketDomain;
        this.endpoint = endpoint;
        this.bucketName = bucketName;

        this.s3Client = this.getS3Client();
    }

    public String getBucketDomain() {
        return bucketDomain;
    }

    private AmazonS3 getS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyId, accessKeySecret);
        AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(provider)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, ""))
                .withPathStyleAccessEnabled(false)
                .withChunkedEncodingDisabled(true)
                .build();
    }

    /**
     * 上传文件到OSS
     *
     * @param filepath 文件路径
     * @return 文件访问地址
     */
    public String putFile(String filepath, InputStream inputStream, ObjectMetadata metadata) {
        logger.info("will put attachment to oss: {}", filepath);

        String url = bucketDomain + "/" + filepath;
        if (s3Client.doesObjectExist(bucketName, filepath)) {
            return url;
        }

        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, filepath, inputStream, metadata);
            s3Client.putObject(request);
            return url;
        } catch (AmazonServiceException e) {
            logger.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.", e);
            System.out.printf("Error Message:%s%n", e.getErrorMessage());
            throw new RuntimeException(e);
        }
    }

    public void clean() {
        s3Client.shutdown();
    }

}
