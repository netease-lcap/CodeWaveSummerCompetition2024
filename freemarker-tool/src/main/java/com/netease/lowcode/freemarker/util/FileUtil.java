package com.netease.lowcode.freemarker.util;

import com.netease.lowcode.freemarker.dto.UploadResponseDTO;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static InputStream getFileInputStream(String urlStr) throws IOException {
        URL url = new URL(getTrueUrl(urlStr));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(3 * 1000);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.82 Safari/537.36");
        return url.openStream();
    }

    public static String getTrueUrl(String urlStr) throws UnsupportedEncodingException {
        int lastIndexOf = urlStr.lastIndexOf("/");
        int queryIndexOf = urlStr.indexOf("?");
        if (queryIndexOf == -1) queryIndexOf = urlStr.length();

        String prefix = urlStr.substring(0, lastIndexOf);
        String suffix = urlStr.substring(queryIndexOf);
        String fileName = urlStr.substring(lastIndexOf + 1, queryIndexOf);

        String urlFileName = getTrueFileName(fileName);
        String trueUrlStr = prefix + "/" + urlFileName + suffix;
        if (!trueUrlStr.startsWith("http") && trueUrlStr.startsWith("/upload")) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            int port = request.getLocalPort();
            return "http://127.0.0.1:" + port + trueUrlStr;
        }
        return trueUrlStr;
    }

    public static String getTrueFileName(String fileName) throws UnsupportedEncodingException {
        if (fileName.equals(URLDecoder.decode(fileName, "UTF-8"))) {
            return URLEncoder.encode(fileName, "UTF-8");
        }
        return fileName;
    }

    public static UploadResponseDTO uploadStream(InputStream inputStream, String fileName) throws IOException {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int port = httpServletRequest.getLocalPort();

        String uploadUrl = "http://127.0.0.1:" + port + "/gateway/lowcode/api/v1/app/upload";
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
        // RequestBody requestBody = RequestBody.create(fileBytes, MediaType.parse("application/octet-stream"));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), fileBytes);
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
            return JsonUtil.fromJson(response.body().string(), UploadResponseDTO.class);
        }
        logger.error(String.format("文件上传失败,%s",response));
        throw new RuntimeException(String.format("文件上传失败,%s",response));
    }
}
