package com.netease.lowcode.pdf.extension.utils;

import com.alibaba.fastjson2.JSON;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component("pdfGeneratorFileUtils")
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    @Value("${lcp.upload.sinkType}")
    private String sinkType;
    @Value("${lcp.upload.sinkPath}")
    private String sinkPath;
    @Value("${lcp.upload.access}")
    private String access;
    @Autowired
    private FileConnectorUtils pdfGeneratorFileConnectorUtils;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Environment env;

    public static String DEFAULT_TEMPLATE_DIR = "/data/template";

    /**
     * 文件上传
     *
     * @param file
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws FileNotFoundException
     */
    public UploadResponseDTO uploadFileV2(File file) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, FileNotFoundException {

        FileInputStream fis = new FileInputStream(file);
        String fileName = file.getName();
        String fileExt = "";
        if (fileName.contains(".")) {
            int i = fileName.lastIndexOf(".");
            fileExt = fileName.substring(i);
            fileName = fileName.substring(0, i);
        }
        // 只要拼接 sinkPath+fileName+时间+后缀即可。
        String curTime = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");

        boolean containsBean = applicationContext.containsBean("fileStorageClientManager");
        if(!containsBean){
            fileName = fileName + "_" + curTime + fileExt;//防止文件被覆盖，可按需选择
            return pdfGeneratorFileConnectorUtils.Base64FileUploadV2(fis,fileName,new HashMap<>());
        }

        Object clientManager = applicationContext.getBean("fileStorageClientManager");
        Method getFileSystemSpi = clientManager.getClass().getMethod("getFileSystemSpi", String.class);
        Object fileStorageClient = getFileSystemSpi.invoke(clientManager, sinkType);

        Method upload = fileStorageClient.getClass().getMethod("upload", InputStream.class, String.class, Map.class);
        // http://dev.exporttest.defaulttenant.lcap.codewave-dev.163yun.com/upload/app/%E5%A4%A7%E6%95%B0%E6%8D%AE%E5%AF%BC%E5%87%BA%E6%B5%8B%E8%AF%95_20240106093632186.xlsx

        String savePath = String.join("/", sinkPath, fileName + "_" + curTime + fileExt);
        String filePath = (String) upload.invoke(fileStorageClient, fis, savePath, new HashMap<>());

        // 组装链接
        UploadResponseDTO responseDTO = new UploadResponseDTO();
        responseDTO.setFilePath("/upload" + filePath);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = requestAttributes.getRequest();
            responseDTO.setResult(request.getScheme() + "://" + request.getServerName() +
                    (80 == request.getServerPort() ? "" : ":" + request.getServerPort()) +
                    "/upload" + filePath);
        } else {
            responseDTO.setResult(responseDTO.getFilePath());
        }

        return responseDTO;
    }

    public static UploadResponseDTO uploadStream(InputStream inputStream, String fileName) throws IOException {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String uploadUrl = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" +
                httpServletRequest.getServerPort() + "/upload";
        OkHttpClient client = new OkHttpClient();

        byte[] fileBytes;
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int read;
            byte[] data = new byte[1024];
            while ((read = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }
            buffer.flush();
            fileBytes = buffer.toByteArray();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),fileBytes);
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, requestBody)
                .build();

        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(multipartBody)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        if (response.isSuccessful()) {
            return JSON.parseObject(response.body().string(), UploadResponseDTO.class);
        }
        throw new RuntimeException(String.format("文件上传失败,%s",response));
    }

    public static UploadResponseDTO uploadStreamV2(InputStream inputStream, String fileName) throws IOException {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String uploadUrl = "http://" + "127.0.0.1:8080" + "/upload";
        logger.info("内部地址:{}",uploadUrl);
        // http是域名80端口，https可能是域名443 验证下，然后替换地址
        logger.info("外部地址:{}", uploadUrl.replace("127.0.0.1:8080", httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort()));
        byte[] fileBytes;
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int read;
            byte[] data = new byte[1024];
            while ((read = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }
            buffer.flush();
            fileBytes = buffer.toByteArray();
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("lcapIsCompress", "false")
                .addFormDataPart("viaOriginURL", "false")
                .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse("application/octet-stream"), fileBytes))
                .build();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("accept", httpServletRequest.getHeader("accept"));
        headers.put("accept-language", httpServletRequest.getHeader("accept-language"));
        headers.put("cache-control", httpServletRequest.getHeader("cache-control"));
        // headers.put("content-length", httpServletRequest.getHeader("Accept"));
        headers.put("content-type", httpServletRequest.getHeader("content-type"));
        String cookie = httpServletRequest.getHeader("cookie");
        if (StringUtils.isNotBlank(cookie)) {
            StringBuilder sb = new StringBuilder();
            for (String s : StringUtils.split(cookie, "; ")) {
                if (s.startsWith("locale=") ||
                        s.startsWith("wyy_uid=") ||
                        s.startsWith("domain_authorization=") ||
                        s.contains("auth")) {
                    sb.append(s).append("; ");
                }
            }
            if (sb.length() > 2) {
                cookie = sb.substring(0, sb.length() - 2);
            }
        }
        headers.put("cookie", cookie);
        headers.put("domainname", httpServletRequest.getHeader("domainname"));
        headers.put("host", httpServletRequest.getHeader("host"));
        headers.put("origin", httpServletRequest.getHeader("origin"));
        headers.put("pragma", httpServletRequest.getHeader("pragma"));
        headers.put("referer", httpServletRequest.getHeader("referer"));
        headers.put("user-agent", httpServletRequest.getHeader("user-agent"));
        headers.entrySet().removeIf(entry -> Objects.isNull(entry.getValue()));
        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(requestBody)
                .headers(Headers.of(headers))
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        if (response.isSuccessful()) {
            byte[] bytes = response.body().bytes();
            logger.info("响应结果: {}",new String(bytes, StandardCharsets.UTF_8));
            return JSON.parseObject(new String(bytes, StandardCharsets.UTF_8), UploadResponseDTO.class);
        }
        logger.error("文件上传失败,{}",response);
        throw new RuntimeException(String.format("文件上传失败,%s", response));
    }

    public static File downloadFile(String urlStr) throws IOException {
        URI uri = null;
        try {
            uri = new URI(urlStr);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String path = uri.getPath();
        String query = uri.getQuery();
        // 获取文件名,3.11之前可以根据url中的参数获取文件名，3.11之后需要根据fileName参数获取文件名
        String fileName = query != null ? Arrays.stream(query.split("&"))
                .map(param -> param.split("="))
                .filter(keyValue -> keyValue.length == 2 && keyValue[0].equalsIgnoreCase("fileName"))
                .map(keyValue -> keyValue[1])
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("The provided URL must contain a fileName parameter.")) : path.substring(path.lastIndexOf('/') + 1);
        URL url = new URL(getTrueUrl(urlStr,fileName));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(3 * 1000);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.82 Safari/537.36");
        InputStream inputStream = url.openStream();
        byte[] getData = readInputStream(inputStream);
        // 文件保存位置
        File saveDir = new File(DEFAULT_TEMPLATE_DIR);
        if (!saveDir.exists()) {
            // 这里可能会应为目录权限 导致无法创建目录。下面逻辑读取文件时报FileNotFoundException
            saveDir.mkdirs();
            // 这里增加校验
            if (!saveDir.exists()) {
                throw new RuntimeException(String.format("目录创建失败%s，请检查目录权限", DEFAULT_TEMPLATE_DIR));
            }
        }
        File file = new File(saveDir + File.separator + fileName);
        if (file.exists()) file.delete();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        return file;
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static String getTrueUrl(String urlStr,String fileName) throws UnsupportedEncodingException {

        // 自动检测 URL 是否已经编码
        if (!isUrlEncoded(fileName)) {
            String fileNameEncoded = java.net.URLEncoder.encode(fileName, "UTF-8");
            urlStr = urlStr.replace(fileName, fileNameEncoded);
        }
        if (!urlStr.startsWith("http") && urlStr.startsWith("/upload")) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            int port = request.getLocalPort();
            return "http://127.0.0.1:" + port + urlStr;
        }
        return urlStr;
    }
    // 新增方法：检测 URL 是否已经编码
    private static boolean isUrlEncoded(String url) {
        try {
            String decodedUrl = java.net.URLDecoder.decode(url, "UTF-8");
            return !decodedUrl.equals(url);
        } catch (Exception e) {
            return false;
        }
    }
    public static void delete(File file) {
        if(file.isFile()){
            file.delete();
            return;
        }
        for (File listFile : file.listFiles()) {
            delete(listFile);
        }
    }
}

