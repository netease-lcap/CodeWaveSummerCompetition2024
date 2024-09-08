package com.yu.connector;

import com.alibaba.fastjson.JSON;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.netease.lowcode.core.annotation.NaslConnector;
import com.netease.lowcode.core.util.FilenameUtils;
import com.netease.lowcode.core.util.StringUtils;
import com.yu.dto.UploadDto;
import com.yu.vo.BucketVo;
import com.yu.vo.OssFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/30 16:44
 **/
@NaslConnector(connectorKind = "oss-connector")
public class OssConnector {
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    private static final Long FILE_SIZE_THRESHOLD = 5L * 1024L * 1024L;
    private static final String S3_TYPE = "s3";
    private static final String NOS_TYPE = "nos";
    private AmazonS3 s3Client;
    public final String OSS_OBJECT_ACL = "x-oss-object-acl";
    public final String OSS_STORAGE_CLASS = "x-oss-storage-class";

    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    @NaslConnector.Creator
    public OssConnector init(String accessKey, String accessKeySecret, String endpoint, String storageType) {
        if (!isEmpty(accessKey) && !isEmpty(accessKeySecret) && !isEmpty(endpoint)) {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessKeySecret);
            s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withPathStyleAccessEnabled(S3_TYPE.equals(storageType))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, null))
                    .withChunkedEncodingDisabled(true)
                    .build();
        }
        return this;
    }

    @NaslConnector.Tester
    public Boolean connectTest(String accessKey, String accessKeySecret, String endpoint, String storageType) {
        init(accessKey, accessKeySecret, endpoint, storageType);
        try {
            listBuckets();
            return true;
        } catch (Exception e) {
            log.error("列举Bucket失败：", e);
            return false;
        }
    }

    /**
     * 列举所有的bucket
     *
     * @return
     */
    @NaslConnector.Logic
    public List<BucketVo> listBuckets() {
        try {
            // 列举当前账号所有地域下的存储空间。
            List<BucketVo> result = new ArrayList<>();
            List<Bucket> buckets = s3Client.listBuckets();
            UserConvertUtils convertInstance = UserConvertUtils.INSTANCE;
            for (Bucket bucket : buckets) {
                result.add(convertInstance.convertBucket(bucket));
            }
            return result;
        } catch (AmazonServiceException oe) {
            log.error("列举bucket失败：", oe);
            throw new RuntimeException(oe);
        } catch (SdkClientException ce) {
            log.error("oss连接失败");
            throw new RuntimeException(ce);
        }
    }

    //  1、为了兼容 Windos的 上传路径为 "\" 的情况，NOS 或者 S3 只认知 "/", 所以需要对windows进行转换
    //  2、key 是不允许出现 / 为前缀的，所以需要对key进行转换
    private String formatUploadKey(String key) {
        key = key.replace(File.separator, "/");
        if (key.startsWith("/")) {
            key = key.substring(1);
        }
        return key;
    }


    /**
     * 文件上传到服务器
     *
     * @param uploadDto
     * @return
     */
    @NaslConnector.Logic
    public String upload(UploadDto uploadDto) {
        InputStream inputStream = null;
        try {
            Util.validRequire(uploadDto);
            String encodedUrl = Util.tranformStyle(uploadDto.filePath);
            URL url = new URL(encodedUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
            String key = formatPath(uploadDto.objectName, uploadDto.bucketName);
            //对上传key进行标准化
            key = formatUploadKey(key);
            ObjectMetadata metadata = new ObjectMetadata();
            //设置文件ACL
            Optional.ofNullable(uploadDto.acl).ifPresent(e -> metadata.setHeader(OSS_OBJECT_ACL, e));
            //设置存储类型
            Optional.ofNullable(uploadDto.storeClass).ifPresent(e -> metadata.setHeader(OSS_STORAGE_CLASS, e));
            long contentLength = inputStream.available();
            if (contentLength > FILE_SIZE_THRESHOLD * 20) {
                log.info("大文件使用分片上传");
                multipartUpload(inputStream, key, uploadDto.bucketName);
                log.info("大文件上传成功");
            } else {
                // 存储S3对象
                PutObjectRequest putObjectRequest = new PutObjectRequest(uploadDto.bucketName, key, inputStream, metadata);
                putObjectRequest.getRequestClientOptions().setReadLimit(FILE_SIZE_THRESHOLD.intValue());
                PutObjectResult result = s3Client.putObject(putObjectRequest);
                log.info(JSON.toJSONString(result));
            }
            return uploadDto.objectName;
        } catch (IOException ex) {
            log.error("上传的文件资源未找到");
            throw new OssException(ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    /**
     * 大文件分段上传
     *
     * @param inputStream
     */
    private void multipartUpload(InputStream inputStream, String key, String bucket) throws IOException {
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucket, key);
        initRequest.getRequestClientOptions().setReadLimit(FILE_SIZE_THRESHOLD.intValue());
        InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);
        // Upload the file parts.
        List<PartETag> partETags = new ArrayList<>();
        byte[] bytes = new byte[FILE_SIZE_THRESHOLD.intValue()];
        int read;
        int partIndex = 1;
        while ((read = inputStream.read(bytes)) > 0) {
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
                // Create the request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(bucket)
                        .withKey(key)
                        .withUploadId(initResponse.getUploadId())
                        .withPartNumber(partIndex++)
                        .withInputStream(byteArrayInputStream)
                        .withPartSize(read);

                UploadPartResult uploadResult = s3Client.uploadPart(uploadRequest);
                partETags.add(uploadResult.getPartETag());
            }
        }

        // Complete the multipart upload.
        CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucket, key,
                initResponse.getUploadId(), partETags);
        s3Client.completeMultipartUpload(compRequest);
    }

    /**
     * 修改文件读写权限
     *
     * @param bucketName bucket名称
     * @param objectName 对象名
     * @param acl        权限 取值为 default private public-read public-read-write
     * @return
     */
    @NaslConnector.Logic
    public Boolean setFileAcl(String bucketName, String objectName, String acl) {
        if (bucketName == null || objectName == null) throw new IllegalArgumentException("请传入 bucket名称和对象名称");
        if (acl == null)
            throw new IllegalArgumentException("请传入文件访问控制权限  取值为 default private public-read public-read-write");
        try {
            // 设置Object的访问权限。
            this.s3Client.setObjectAcl(bucketName, objectName, parse(acl));
            return true;
        } catch (AmazonServiceException oe) {
            log.error("修改文件读写权限失败：", oe);
            throw new RuntimeException(oe);
        } catch (SdkClientException ce) {
            log.error("oss连接失败");
            throw new RuntimeException(ce);
        }
    }

    private static CannedAccessControlList parse(String acl) {
        for (CannedAccessControlList cacl : CannedAccessControlList.values()) {
            if (cacl.toString().equals(acl)) {
                return cacl;
            }
        }

        throw new IllegalArgumentException("Unable to parse the provided acl " + acl);
    }

    /**
     * 删除上传的文件或目录 如果要删除目录，目录必须为空。
     *
     * @param bucketName bucket名称
     * @param objectName 对象名
     * @return
     */
    @NaslConnector.Logic
    public Boolean deleteFile(String bucketName, String objectName) {
        if (bucketName == null || objectName == null) throw new IllegalArgumentException("请传入 bucket名称和对象名称");
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            this.s3Client.deleteObject(bucketName, objectName);
            return true;
        } catch (AmazonServiceException oe) {
            log.error("删除文件失败：", oe);
            throw new RuntimeException(oe);
        } catch (SdkClientException ce) {
            log.error("oss连接失败");
            throw new RuntimeException(ce);
        }
    }

    /**
     * 获取文件信息列表
     *
     * @param bucketName bucket名称
     * @param keyPrefix  文件前缀 如果不设置keyPrefix，则列举存储空间下的所有文件。如果设置keyPrefix，则列举包含指定前缀的文件。
     * @return
     */
    @NaslConnector.Logic
    public List<OssFileVo> getFileList(String bucketName, String keyPrefix) throws IllegalArgumentException {
        if (isEmpty(bucketName)) throw new IllegalArgumentException("bucketName 不能为空");
        try {
            // 列举文件。如果不设置keyPrefix，则列举存储空间下的所有文件。如果设置keyPrefix，则列举包含指定前缀的文件。
            ObjectListing objectListing = this.s3Client.listObjects(bucketName, keyPrefix);
            List<S3ObjectSummary> sums = objectListing.getObjectSummaries();
            UserConvertUtils convertInstance = UserConvertUtils.INSTANCE;
            List<OssFileVo> result = new ArrayList<>();
            for (S3ObjectSummary s : sums) {
                OssFileVo ossFileVo = convertInstance.convertOSSObjectSummary(s);
                result.add(ossFileVo);
            }
            return result;
        } catch (AmazonServiceException oe) {
            log.error("获取文件信息列表失败：", oe);
            throw new RuntimeException(oe);
        } catch (SdkClientException ce) {
            log.error("oss连接失败");
            throw new RuntimeException(ce);
        }
    }


    private String formatPath(String path, String bucket) {
        /**
         * @since 非oss上传, 兼容历史数据(因为发了一版之后, 既有带/, 也有不带的)
         * 旧数据下载时, path一定带有/, 不带/的不需要拼接
         */
        if (StringUtils.isBlank(path)) {
            return path;
        }
        /**
         * 先判原本的path是否存在文件
         */
        if (this.s3Client.doesObjectExist(bucket, path)) {
            return path;
        } else {
            /**
             * 文件不存在, 则判断是否是以/开头
             */
            boolean startWithTag = path.startsWith("/");
            if (startWithTag) {
                /**
                 * 需要把/截取
                 */
                path = path.substring(1);
                return path;
            }
            /**
             * 需要拼接/
             */
            return FilenameUtils.concat(File.separator, path);
        }
    }


}
