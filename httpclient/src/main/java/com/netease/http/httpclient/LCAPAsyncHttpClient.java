package com.netease.http.httpclient;

import com.netease.http.dto.RequestParam;
import com.netease.http.dto.RequestParamAllBodyTypeInner;
import com.netease.http.exception.TransferCommonException;
import com.netease.http.util.FileNameValidator;
import com.netease.http.util.FileUtil;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Component
@EnableRetry
public class LCAPAsyncHttpClient {
    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Autowired
    private LCAPHttpClient lcapHttpClient;
    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FileUtil httpClientFileUtils;
/**
 * 本地文件上传至nos- todo 需要使用s3分块上传
 */

    /**
     * 本地文件上传至第三方接口
     *
     * @param fileTimeMillisKey 文件key，由downloadFileToLocalAsync返回
     * @param fileKey           form-data请求中文件的key，默认:file
     * @param requestParam      请求信息
     * @return
     */
    @NaslLogic
    public String uploadLocalFileToThirdInterface(String fileTimeMillisKey, String fileKey, RequestParam requestParam) throws TransferCommonException {
//        UploadFileParam uploadFileParam = new UploadFileParam();
//        uploadFileParam.setFileKey("file");
//        return lcapHttpClient.uploadNosExchangeCommon(uploadFileParam, fileName, requestParam);
//        todo
        return null;
    }

    /**
     * 移步下载文件到本地
     *
     * @param fileName     文件名称，可空
     * @param requestParam 请求信息
     * @return 文件key-时间戳
     */
    @NaslLogic
    public String downloadFileToLocalAsync(String fileName, RequestParam requestParam) throws TransferCommonException {
        File file = null;
        if (!FileNameValidator.isValidFilename(fileName, 0)) {
            throw new TransferCommonException(-1, "fileName文件名称不合法");
        }
        try {
            RequestParamAllBodyTypeInner requestParamGetFile = new RequestParamAllBodyTypeInner();
            requestParamGetFile.setUrl(requestParam.getUrl());
            requestParamGetFile.setHeader(requestParam.getHeader());
            if (StringUtils.isEmpty(requestParam.getHttpMethod())) {
                requestParamGetFile.setHttpMethod(HttpMethod.GET.name());
            }
            requestParamGetFile.setHttpMethod(requestParam.getHttpMethod());
            requestParamGetFile.setBody(requestParam.getBody());
            return httpClientService.asyncDownloadFile(requestParamGetFile, restTemplate, fileName);
        } catch (HttpClientErrorException e) {
            logger.error("", e);
            throw new TransferCommonException(e.getStatusCode().value(), e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("", e);
            throw new TransferCommonException(e.getMessage(), e);
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

}
