package com.netease.http.httpclient;

import com.alibaba.fastjson.JSONObject;
import com.netease.http.dto.*;
import com.netease.http.util.NosUtil;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.core.annotation.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

/**
 * desc
 *
 * @author jingzhix
 * @date 2023/7/5
 * @since
 */
@Component
@EnableRetry
public class LCAPHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(LCAPHttpClient.class);

    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    @Qualifier("restTemplatePrimary")
    private RestTemplate restTemplatePrimary;

    @Autowired
    @Qualifier("restTemplateIgnoreCrt")
    private RestTemplate restTemplateIgnoreCrt;

    /**
     * http/https调用（非form使用）
     *
     * @param url
     * @param httpMethod
     * @param header
     * @param body
     * @return
     * @throws URISyntaxException
     */
    @NaslLogic
    @Deprecated
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L))
    public String exchange(@Required String url, @Required String httpMethod, @Required Map<String, String> header, @Required Map<String, String> body) throws IllegalArgumentException {
        try {
            RequestParamAllBodyTypeInner requestParam = new RequestParamAllBodyTypeInner();
            requestParam.setBody(body);
            //填充requestParam参数
            requestParam.setUrl(url);
            requestParam.setHttpMethod(httpMethod);
            requestParam.setHeader(header);
            ResponseEntity<String> exchange = httpClientService.exchangeInner(requestParam, restTemplatePrimary, String.class);
            if (exchange.getStatusCode() == HttpStatus.OK) {
                return exchange.getBody();
            } else {
                throw new IllegalArgumentException("请求http失败,返回：" + JSONObject.toJSONString(exchange));
            }
        } catch (HttpClientErrorException e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    /**
     * http/https调用（非form使用，异常时返回http错误码）
     *
     * @param url
     * @param httpMethod
     * @param header
     * @param body
     * @return
     * @throws URISyntaxException
     */
    @NaslLogic
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L))
    public String exchangeV2(@Required String url, @Required String httpMethod, @Required Map<String, String> header, @Required Map<String, String> body) throws IllegalArgumentException {
        try {
            RequestParamAllBodyTypeInner requestParam = new RequestParamAllBodyTypeInner();
            requestParam.setBody(body);
            //填充requestParam参数
            requestParam.setUrl(url);
            requestParam.setHttpMethod(httpMethod);
            requestParam.setHeader(header);
            ResponseEntity<String> exchange = httpClientService.exchangeInner(requestParam, restTemplatePrimary, String.class);
            return exchange.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("", e);
            return e.getResponseBodyAsString();
        } catch (Exception e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    /**
     * 下载文件并上传到nos（默认格式xlsx）
     *
     * @param fileUrl
     * @param fileName 文件名，可空
     * @param header
     * @return
     */
    @NaslLogic
    public String downloadFileUploadNos(String fileUrl, String fileName, Map<String, String> header) throws IllegalArgumentException {
        File file = null;
        try {
            RequestParamAllBodyTypeInner requestParam = new RequestParamAllBodyTypeInner();
            requestParam.setUrl(fileUrl);
            requestParam.setHeader(header);
            requestParam.setHttpMethod(HttpMethod.GET.name());
            file = httpClientService.downloadFile(requestParam, restTemplatePrimary, fileName);
            String key = "/extension_" + file.getName();
            NosUtil.put(key, file);
            return NosUtil.generateUrl(key);
        } catch (HttpClientErrorException e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * nos url文件上传到第三方
     *
     * @param fileUrl      文件url
     * @param requestUrl   当前请求的url
     * @param requestParam 请求信息
     * @return
     */
    @NaslLogic
    public String uploadNosExchange(String fileUrl, String requestUrl, RequestParam requestParam) throws IllegalArgumentException {
        File file = null;
        try {
            RequestParamAllBodyTypeInner requestParamGetFile = new RequestParamAllBodyTypeInner();
            URL url = null;
            try {
                url = new URL(requestUrl);
            } catch (MalformedURLException e) {
                logger.error("requestUrl必须是一个url", e);
            }
            requestParamGetFile.setUrl(url.getProtocol() + "://" + url.getHost() + fileUrl);
            requestParamGetFile.setHttpMethod(HttpMethod.GET.name());
            file = httpClientService.downloadFile(requestParamGetFile, restTemplatePrimary, null);
            RequestParamAllBodyTypeInner requestParamInner = new RequestParamAllBodyTypeInner();
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            requestParam.getBody().forEach(body::add);
            body.add("file", new FileSystemResource(file));
            requestParamInner.setBody(body);
            requestParamInner.setHttpMethod(requestParam.getHttpMethod());
            requestParamInner.setUrl(requestParam.getUrl());
            requestParamInner.setHeader(requestParam.getHeader());
            ResponseEntity<String> exchange = httpClientService.exchangeInner(requestParamInner, restTemplatePrimary, String.class);
            file.delete();
            if (exchange.getStatusCode() == HttpStatus.OK) {
                return exchange.getBody();
            } else {
                throw new IllegalArgumentException("请求http失败,返回：" + JSONObject.toJSONString(exchange));
            }
        } catch (HttpClientErrorException e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    // TODO: 2024/5/17 该方式平台不支持，已反馈平台，待平台支持后调整
//    @NaslLogic
    public String test(Integer a) throws HttpClientRuntimeException {
        if (a == 1) {
            throw new HttpClientRuntimeException("test errorKey1");
        } else if (a == 2) {
            throw new HttpClientRuntimeException(203, "test errorKey2");
        } else if (a == 3) {
            throw new HttpClientRuntimeException(203, "test errorKey3", new Throwable("test Throwable3"));
        } else if (a == 4) {
            throw new HttpClientRuntimeException("test errorKey4", new Throwable("test Throwable4"));
        } else if (a == 5) {
            throw new HttpClientRuntimeException(203, new Throwable("test Throwable5"));
        }
        return "test";
    }

    /**
     * 证书校验https请求（非form使用）
     *
     * @param requestParam
     * @return
     */
    @NaslLogic(enhance = false)
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L))
    public String exchangeCrt(RequestParam requestParam) throws IllegalArgumentException {
        try {
            RestTemplate restTemplateFinal = restTemplatePrimary;
            try {
                if (requestParam.getIsIgnoreCrt() == null) {
                    requestParam.setIsIgnoreCrt(false);
                }
                if (requestParam.getIsIgnoreCrt()) {
                    restTemplateFinal = restTemplateIgnoreCrt;
                }
            } catch (Exception e) {
                logger.error("", e);
            }
            ResponseEntity<String> exchange = httpClientService
                    .exchangeInner(DtoConvert.convertToRequestParamAllBodyTypeInner(requestParam), restTemplateFinal, String.class);
            if (exchange.getStatusCode() == HttpStatus.OK) {
                return exchange.getBody();
            } else {
                throw new IllegalArgumentException("请求http失败,返回：" + JSONObject.toJSONString(exchange));
            }
        } catch (HttpClientErrorException e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    /**
     * https请求，body支持String类型（form使用）
     *
     * @param requestParam
     * @return
     */
    @NaslLogic
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L))
    public String exchangeAllBodyType(RequestParamAllBodyType requestParam) throws IllegalArgumentException {
        try {
            RestTemplate restTemplateFinal = restTemplatePrimary;
            try {
                if (requestParam.getIsIgnoreCrt() == null) {
                    requestParam.setIsIgnoreCrt(false);
                }
                if (requestParam.getIsIgnoreCrt()) {
                    restTemplateFinal = restTemplateIgnoreCrt;
                }
            } catch (Exception e) {
                logger.info("", e);
            }
            ResponseEntity<String> exchange = httpClientService
                    .exchangeInner(DtoConvert.convertToRequestParamAllBodyTypeInner(requestParam), restTemplateFinal, String.class);
            if (exchange.getStatusCode() == HttpStatus.OK) {
                return exchange.getBody();
            } else {
                throw new IllegalArgumentException("请求http失败,返回：" + JSONObject.toJSONString(exchange));
            }
        } catch (HttpClientErrorException e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * http/https调用（非form使用，url不编码）
     *
     * @param url
     * @param httpMethod
     * @param header
     * @param body
     * @return
     * @throws URISyntaxException
     */
    @NaslLogic
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L))
    public String exchangeWithoutUriEncode(@Required String url, @Required String httpMethod, @Required Map<String, String> header, @Required Map<String, String> body) throws IllegalArgumentException {
        try {
            RequestParamAllBodyTypeInner requestParam = new RequestParamAllBodyTypeInner();
            requestParam.setBody(body);
            //填充requestParam参数
            requestParam.setUrl(url);
            requestParam.setHttpMethod(httpMethod);
            requestParam.setHeader(header);
            ResponseEntity<String> exchange = httpClientService.exchangeWithoutUriEncode(requestParam, restTemplatePrimary, String.class);
            if (exchange.getStatusCode() == HttpStatus.OK) {
                return exchange.getBody();
            } else {
                throw new IllegalArgumentException("请求http失败,返回：" + JSONObject.toJSONString(exchange));
            }
        } catch (HttpClientErrorException e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
