package com.netease.http.httpclient;

import com.alibaba.fastjson.JSONObject;
import com.netease.http.dto.DtoConvert;
import com.netease.http.dto.RequestParamAllBodyTypeInner;
import com.netease.http.util.FileNameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class HttpClientService {
    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    public <T> ResponseEntity<T> exchangeInner(RequestParamAllBodyTypeInner requestParam, RestTemplate restTemplateFinal, Class<T> responseType) {
        try {
            if (Objects.isNull(requestParam.getHeader())) {
                requestParam.setHeader(new HashMap<>());
            }
            if (Objects.isNull(requestParam.getHttpMethod())) {
                requestParam.setHttpMethod("");
            }
            if (Objects.isNull(requestParam.getUrl())) {
                requestParam.setUrl("");
            }
            URI uri = UriComponentsBuilder.fromUriString(requestParam.getUrl()).build().encode().toUri();
            HttpMethod requestMethod = HttpMethod.resolve(requestParam.getHttpMethod().toUpperCase());
            HttpHeaders httpHeaders = new HttpHeaders();
            requestParam.getHeader().forEach((headerName, headerValue) -> httpHeaders.add((String) headerName, (String) headerValue));
            HttpEntity<T> httpEntity = new HttpEntity<>(DtoConvert.convertToGeneric(requestParam.getBody()), httpHeaders);
            if (requestParam.getBody() == null) {
                httpEntity = new HttpEntity<>(httpHeaders);
            }
            return restTemplateFinal.exchange(uri, Objects.requireNonNull(requestMethod), httpEntity, responseType);
        } catch (Exception e) {
            logger.error("请求http失败", e);
            throw e;
        }
    }

    /**
     * 下载文件
     *
     * @param requestParam
     * @param restTemplateFinal
     * @param fileName          仅取后缀名
     * @return
     */
    public File downloadFile(RequestParamAllBodyTypeInner requestParam, RestTemplate restTemplateFinal, String fileName) {
        ResponseEntity<byte[]> response = exchangeInner(requestParam, restTemplateFinal, byte[].class);
        if (response.getStatusCode() == HttpStatus.OK) {
            byte[] fileData = response.getBody();
            List<String> resHeaders = response.getHeaders().get("Content-Disposition");
            if (StringUtils.isEmpty(fileName)) {
                fileName = System.currentTimeMillis() + ".xlsx";
                if (resHeaders != null) {
                    for (String resHeader : resHeaders) {
                        if (resHeader.startsWith("filename") || resHeader.startsWith("attachment")) {
                            String fileNameTmp = resHeader.split("filename=")[1].replace("\"", "");
                            if (FileNameValidator.isValidFileName(fileNameTmp)) {
                                fileName = URLDecoder.decode(fileNameTmp);
                            }
                        }
                    }
                }
            }
            File file = null;
            try {
                String fileExt = "";
                if (fileName.contains(".")) {
                    int i = fileName.lastIndexOf(".");
                    fileExt = fileName.substring(i);
                    fileName = fileName.substring(0, i);
                }
                if (fileName.length() < 3) {
                    fileName = fileName + "-" + System.currentTimeMillis();
                }
                file = File.createTempFile(fileName, fileExt);
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    assert fileData != null;
                    outputStream.write(fileData);
                }
            } catch (IOException e) {
                logger.error("文件操作异常:", e);
            }
            return file;
        } else {
            logger.error("请求http失败,返回：{}", JSONObject.toJSONString(response));
        }
        return null;
    }

    public <T> ResponseEntity<T> exchangeWithoutUriEncode(RequestParamAllBodyTypeInner requestParam, RestTemplate restTemplateFinal, Class<T> responseType) {
        if (Objects.isNull(requestParam.getHeader())) {
            requestParam.setHeader(new HashMap<>());
        }
        if (Objects.isNull(requestParam.getHttpMethod())) {
            requestParam.setHttpMethod("");
        }
        if (Objects.isNull(requestParam.getUrl())) {
            requestParam.setUrl("");
        }
        URI uri = URI.create(requestParam.getUrl());
        HttpMethod requestMethod = HttpMethod.resolve(requestParam.getHttpMethod().toUpperCase());
        HttpHeaders httpHeaders = new HttpHeaders();
        requestParam.getHeader().forEach((headerName, headerValue) -> httpHeaders.add((String) headerName, (String) headerValue));
        HttpEntity<T> httpEntity = new HttpEntity<>(DtoConvert.convertToGeneric(requestParam.getBody()), httpHeaders);
        if (requestParam.getBody() == null) {
            httpEntity = new HttpEntity<>(httpHeaders);
        }
        return restTemplateFinal.exchange(uri, Objects.requireNonNull(requestMethod), httpEntity, responseType);
    }
}
