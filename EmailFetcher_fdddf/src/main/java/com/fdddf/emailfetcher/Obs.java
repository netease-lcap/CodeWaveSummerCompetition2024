package com.fdddf.emailfetcher;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.fdddf.emailfetcher.api.EmailExtractor;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Obs {
    private final EmailConfig cfg;

    public Obs(EmailConfig cfg) {
        this.cfg = cfg;
    }

    private static final Logger logger = LoggerFactory.getLogger(EmailExtractor.class);

    /**
     * Save attachments to OSS
     * @param attachments List of attachments in /tmp folder
     * @return List of saved attachments
     */
    public List<String> saveAttachmentToOSS(Map<String, InputStream> attachments) {
        DefaultCredentialProvider provider = CredentialsProviderFactory.newDefaultCredentialProvider(cfg.ossAccessKeyId, cfg.ossAccessKeySecret);
        String endpoint = cfg.ossEndpoint;
        if (!endpoint.startsWith("http")) {
            endpoint = "https://" + endpoint;
        }
        String bucketDomain = cfg.ossBucketDomain;
        if (!bucketDomain.startsWith("http")) {
            bucketDomain = "https://" + bucketDomain;
        }

        OSS ossClient = new OSSClientBuilder().build(endpoint, provider);
        List<String> savedAttachments = new ArrayList<>();
        for (String filename : attachments.keySet()) {
            logger.info("will put attachment to oss: {}", filename);
            String objectName = FilenameUtils.getName(filename);
            if (cfg.ossFolder != null && !cfg.ossFolder.isEmpty()) {
                objectName = cfg.ossFolder + "/" + objectName;
            }

            try {
                if (ossClient.doesObjectExist(cfg.ossBucketName, objectName)) {
                    savedAttachments.add(bucketDomain + "/" + objectName);
                    continue;
                }
                ossClient.putObject(cfg.ossBucketName, objectName, attachments.get(filename));
            } catch (OSSException oe) {
                logger.error("Caught an OSSException, which means your request made it to OSS, "
                        + "but was rejected with an error response for some reason.", oe);
                oe.printStackTrace();
                continue;
            }

            savedAttachments.add(bucketDomain + "/" + objectName);
        }
        ossClient.shutdown();

        return savedAttachments;
    }
}
