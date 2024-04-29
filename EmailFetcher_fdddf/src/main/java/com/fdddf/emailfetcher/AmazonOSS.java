package com.fdddf.emailfetcher;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fdddf.emailfetcher.api.EmailExtractor;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AmazonOSS {
    protected final EmailConfig cfg;

    private static final Logger logger = LoggerFactory.getLogger(EmailExtractor.class);

    public AmazonOSS(EmailConfig cfg) {
        this.cfg = cfg;
    }

    public List<String> saveAttachmentToOSS(Map<String, InputStream> attachments) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(cfg.ossAccessKeyId, cfg.ossAccessKeySecret);
        AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);

        String endpoint = cfg.ossEndpoint;
        if (!endpoint.startsWith("http")) {
            endpoint = "https://" + endpoint;
        }
        String bucketDomain = cfg.ossBucketDomain;
        if (!bucketDomain.startsWith("http")) {
            bucketDomain = "https://" + bucketDomain;
        }

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(provider)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        endpoint,
                        ""))
                .withPathStyleAccessEnabled(false)
                .withChunkedEncodingDisabled(true)
                .build();

        List<String> savedAttachments = new ArrayList<>();

        for (String filename : attachments.keySet()) {
            logger.info("will put attachment to oss: {}", filename);
            String objectName = FilenameUtils.getName(filename);
            if (cfg.ossFolder != null && !cfg.ossFolder.isEmpty()) {
                objectName = cfg.ossFolder + "/" + objectName;
            }

            try {
                if (s3Client.doesObjectExist(cfg.ossBucketName, objectName)) {
                    savedAttachments.add(bucketDomain + "/" + objectName);
                    continue;
                }
                s3Client.putObject(cfg.ossBucketName, objectName, attachments.get(filename), null);
            } catch (AmazonServiceException e) {
                logger.error("Caught an OSSException, which means your request made it to OSS, "
                        + "but was rejected with an error response for some reason.", e);
                e.printStackTrace();
                continue;
            }

            savedAttachments.add(bucketDomain + "/" + objectName);
        }
        s3Client.shutdown();

        return savedAttachments;
    }

}

