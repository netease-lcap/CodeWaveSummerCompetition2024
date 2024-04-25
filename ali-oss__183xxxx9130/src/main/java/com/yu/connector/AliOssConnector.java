package com.yu.connector;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import com.netease.lowcode.core.annotation.NaslConnector;
import com.yu.dto.UploadDto;
import com.yu.vo.BucketVo;
import com.yu.vo.OssFileVo;
import com.yu.vo.UploadReturnVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @date 2024/4/23 9:45
 **/
@NaslConnector(connectorKind = "ali-oss-connector")
public class AliOssConnector {
    private static final Logger log = LoggerFactory.getLogger(AliOssConnector.class);
    private String accessKey;
    private String accessKeySecret;
    private String endpoint;
    private OSS ossClient;

    @NaslConnector.Creator
    public AliOssConnector init(String accessKey, String accessKeySecret, String endpoint) {
        this.accessKey = accessKey;
        this.accessKeySecret = accessKeySecret;
        this.endpoint = endpoint;
        DefaultCredentialProvider credentialsProvider = CredentialsProviderFactory.newDefaultCredentialProvider(this.accessKey, this.accessKeySecret);
        // 创建OSSClient实例。
        this.ossClient = new OSSClientBuilder().build(this.endpoint, credentialsProvider);
        return this;
    }

    @NaslConnector.Tester
    public Boolean connectTest(String accessKey, String accessKeySecret, String endpoint) {
        init(accessKey, accessKeySecret, endpoint);
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
     * @return
     */
    @NaslConnector.Logic
    public List<BucketVo> listBuckets() {
        try {
            // 列举当前账号所有地域下的存储空间。
            List<BucketVo> result = new ArrayList<>();
            List<Bucket> buckets = ossClient.listBuckets();
            UserConvertUtils convertInstance = UserConvertUtils.INSTANCE;
            for (Bucket bucket : buckets) {
                result.add(convertInstance.convertBucket(bucket));
            }
            return result;
        } catch (OSSException oe) {
            log.error("列举bucket失败：", oe);
            throw new RuntimeException(oe);
        } catch (ClientException ce) {
            log.error("阿里云 oss连接失败");
            throw new RuntimeException(ce);
        }
    }

    /**
     * 文件上传到服务器
     *
     * @param uploadDto
     * @return
     */
    @NaslConnector.Logic
    public UploadReturnVo simpleUpload(UploadDto uploadDto) {
        InputStream inputStream = null;
        try {
            Util.validRequire(uploadDto);
            String encodedUrl = Util.tranformStyle(uploadDto.filePath);
            URL url = new URL(encodedUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
            PutObjectRequest putObjectRequest = new PutObjectRequest(uploadDto.bucketName, uploadDto.objectName, inputStream);
            ObjectMetadata metadata = new ObjectMetadata();
            putObjectRequest.setMetadata(metadata);
            //设置文件ACL
            Optional.ofNullable(uploadDto.acl).ifPresent(e -> metadata.setObjectAcl(CannedAccessControlList.parse(e)));
            //设置存储类型
            Optional.ofNullable(uploadDto.storeClass).ifPresent(e -> metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, e));
            PutObjectResult result = this.ossClient.putObject(putObjectRequest);
            return UserConvertUtils.INSTANCE.convertPutObjRes(result);
        } catch (OSSException oe) {
            log.error("文件上传失败：", oe);
            throw new RuntimeException(oe);
        } catch (ClientException ce) {
            log.error("阿里云 oss连接失败");
            throw new RuntimeException(ce);
        } catch (IOException e) {
            log.error("文件流获取失败：", e);
            throw new RuntimeException(e);
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
            VoidResult voidResult = this.ossClient.setObjectAcl(bucketName, objectName, CannedAccessControlList.parse(acl));
            return voidResult.getResponse().isSuccessful();
        } catch (OSSException oe) {
            log.error("修改文件读写权限失败：", oe);
            throw new RuntimeException(oe);
        } catch (ClientException ce) {
            log.error("阿里云 oss连接失败");
            throw new RuntimeException(ce);
        }
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
            VoidResult voidResult = ossClient.deleteObject(bucketName, objectName);
            return voidResult.getResponse().isSuccessful();
        } catch (OSSException oe) {
            log.error("删除文件失败：", oe);
            throw new RuntimeException(oe);
        } catch (ClientException ce) {
            log.error("阿里云 oss连接失败");
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
    public List<OssFileVo> getFileList(String bucketName, String keyPrefix) {
        try {
            // 列举文件。如果不设置keyPrefix，则列举存储空间下的所有文件。如果设置keyPrefix，则列举包含指定前缀的文件。
            ObjectListing objectListing = ossClient.listObjects(bucketName, keyPrefix);
            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
            UserConvertUtils convertInstance = UserConvertUtils.INSTANCE;
            List<OssFileVo> result = new ArrayList<>();
            for (OSSObjectSummary s : sums) {
                OssFileVo ossFileVo = convertInstance.convertOSSObjectSummary(s);
                result.add(ossFileVo);
            }
            return result;
        } catch (OSSException oe) {
            log.error("获取文件信息列表失败：", oe);
            throw new RuntimeException(oe);
        } catch (ClientException ce) {
            log.error("阿里云 oss连接失败");
            throw new RuntimeException(ce);
        }
    }




}
