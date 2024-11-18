package com.netease.lowcode.extensions.util;

import com.netease.cloud.codewave.file.connector.AbstractFileConnector;
import com.netease.cloud.codewave.file.connector.CodeWaveFileConstants;
import com.netease.cloud.codewave.file.connector.FileConnectionManager;
import com.netease.cloud.codewave.file.connector.FileDownloadResult;
import com.netease.cloud.codewave.file.connector.utils.CodeWaveFileUrl;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.extensions.UploadResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component("easyExcelFileConnectorUtils")
public class FileConnectorUtils {
    private final static Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    /**
     * 文件上传-311及之后版本
     *
     * @param fis      文件输入流，不能为空
     * @param fileName 文件名称，包含文件后缀，不能为空
     * @return success true时上传成功，false时上传失败
     */
    public UploadResponseDTO Base64FileUploadV2(InputStream fis, String fileName, Map<String, String> payloads) {
        UploadResponseDTO responseDTO = new UploadResponseDTO();
        // 获取文件连接器
        AbstractFileConnector defaultConnector = FileConnectionManager.getDefaultFileConnector();
        CodeWaveFileUrl fileUrl = new CodeWaveFileUrl(fileName);
        //指定默认文件连接器的唯一标志。用在url拼接上。
        fileUrl.addQueryString(CodeWaveFileConstants.FILE_CONNECTION, defaultConnector.fileStorageCode());
        //调用制品配置的文件连接器的上传方法
        CodeWaveFileUrl result = defaultConnector.upload(fis, fileUrl, payloads);
        //处理文件url
        responseDTO.setFilePath("/upload/" + result.toUrl());
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = requestAttributes.getRequest();
            responseDTO.setResult(request.getScheme() + "://" + request.getServerName() +
                    (80 == request.getServerPort() ? "" : ":" + request.getServerPort()) +
                    responseDTO.getFilePath());
        } else {
            responseDTO.setResult(responseDTO.getFilePath());
        }
        responseDTO.setSuccess(true);
        responseDTO.setCode(200);
        return responseDTO;
    }

    /**
     * 文件下载-311及之后版本
     *
     * @param fileUrl 符合文件连接器规范的文件url
     * @return base64String
     */
//    @NaslLogic
    public String Base64FileDownload(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            log.error("参数不能为空");
            return null;
        }
        try {
            // 获取文件连接器
            AbstractFileConnector defaultConnector = FileConnectionManager.getDefaultFileConnector();
            //将fileUrl构造成CodeWaveFileUrl
            CodeWaveFileUrl fileUrlCodeWaveFileUrl = CodeWaveFileUrl.fromUri(fileUrl);
            //调用制品配置的文件连接器的下载方法
            FileDownloadResult result = defaultConnector.download(fileUrlCodeWaveFileUrl, new HashMap<>());
            InputStream inputStream = result.getInputStream();
            return Base64Util.convertImageToBase64String(inputStream);
        } catch (Exception e) {
            log.error("uploadBase64File error", e);
        }
        return null;
    }

}
