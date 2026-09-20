package com.netease.http.httpclient;

import com.netease.http.dto.DtoConvert;
import com.netease.http.dto.LocalFileCacheDto;
import com.netease.http.dto.RequestParam;
import com.netease.http.dto.RequestParamAllBodyTypeInner;
import com.netease.http.exception.TransferCommonException;
import com.netease.http.util.FileNameValidator;
import com.netease.http.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLSocketFactory;

@Component
public class HttpClientService {
    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    private final Map<String, LocalFileCacheDto> fileCache = new ConcurrentHashMap<>();

    public LocalFileCacheDto getFileCache(String fileKey) {
        return fileCache.get(fileKey);
    }

    public void removeFileCache(String fileKey) {
        if (fileCache.containsKey(fileKey)) {
            fileCache.remove(fileKey);
        }
    }

    public LocalFileCacheDto asyncUploadFileExchangeCommon(String fileTimeMillisKey, RestTemplate restTemplateFinal, RequestParam requestParam, String fileKey, File file) {
        LocalFileCacheDto localFileCacheDto = new LocalFileCacheDto(null, file.getName(), 3);
        fileCache.put(fileTimeMillisKey, localFileCacheDto);
        CompletableFuture.runAsync(() -> {
            logger.info("开始上传文件");
            try {
                String resBody = uploadFileExchangeCommon(restTemplateFinal, requestParam, fileKey, file);
                localFileCacheDto.setResBody(resBody);
                localFileCacheDto.setDownloadStatus(4);
                localFileCacheDto.setFileName(file.getName());
                fileCache.put(fileTimeMillisKey, localFileCacheDto);
            } catch (Exception e) {
                logger.error("上传文件失败Exception", e);
                localFileCacheDto.setDownloadStatus(6);
                localFileCacheDto.setFileName(file.getName());
                localFileCacheDto.setResBody("上传文件失败");
                fileCache.put(fileTimeMillisKey, localFileCacheDto);
            } catch (Throwable t) { // 捕获所有Throwable包括Error
                logger.error("上传文件失败Throwable", t);
                localFileCacheDto.setDownloadStatus(6);
                localFileCacheDto.setFileName(file.getName());
                localFileCacheDto.setResBody("上传文件失败");
                fileCache.put(fileTimeMillisKey, localFileCacheDto);
            } finally {
                file.delete();
            }
        });
        return localFileCacheDto;
    }

    /**
     * 上传文件到第三方系统通用方法
     *
     * @param requestParam
     * @param fileKey
     * @param file
     * @return
     */
    public String uploadFileExchangeCommon(RestTemplate restTemplateFinal, RequestParam requestParam, String fileKey, File file) {
        RequestParamAllBodyTypeInner requestParamInner = new RequestParamAllBodyTypeInner();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        if (requestParam.getBody() != null) {
            requestParam.getBody().forEach(body::add);
        }
        if (StringUtils.isEmpty(fileKey)) {
            fileKey = "file";
        }
        body.add(fileKey, new FileSystemResource(file));
        requestParamInner.setBody(body);
        if (StringUtils.isEmpty(requestParam.getHttpMethod())) {
            requestParam.setHttpMethod(HttpMethod.GET.name());
        }
        requestParamInner.setHttpMethod(requestParam.getHttpMethod());
        requestParamInner.setUrl(requestParam.getUrl());
        requestParamInner.setHeader(requestParam.getHeader());
        ResponseEntity<String> exchange = this.exchangeInner(requestParamInner, restTemplateFinal, String.class);
        if (exchange.getStatusCode() == HttpStatus.OK) {
            return exchange.getBody();
        } else {
            throw new TransferCommonException(exchange.getStatusCodeValue(), JsonUtil.toJson(exchange));
        }
    }

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
     * 不自动补 Content-Type 的 http/https 调用：
     * 调用方 header 中未显式传 Content-Type 时，发送 body 但不携带 Content-Type（绕过 HttpMessageConverter 的自动补全）。
     * body 为 null 或空白字符串时按无 body 处理。
     *
     * @param requestParam
     * @param restTemplateFinal
     * @return
     */
    public ResponseEntity<String> exchangeInnerWithoutContentType(RequestParamAllBodyTypeInner requestParam, RestTemplate restTemplateFinal) throws IOException {
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
            Object body = requestParam.getBody();
            boolean noBody = body == null || (body instanceof String && ((String) body).trim().isEmpty());
            boolean hasContentType = httpHeaders.getContentType() != null;
            if (noBody) {
                if (hasContentType) {
                    return restTemplateFinal.exchange(uri, Objects.requireNonNull(requestMethod), new HttpEntity<>(httpHeaders), String.class);
                }
                // 无 body 走 RestTemplate 时，JDK HttpURLConnection 对 POST 会自动补
                // Content-Type: application/x-www-form-urlencoded 和 Content-Length: 0，故同样走原始 Socket
                return sendRawWithoutContentType(uri, requestMethod, httpHeaders, null);
            }
            if (hasContentType) {
                return restTemplateFinal.exchange(uri, Objects.requireNonNull(requestMethod), new HttpEntity<>(body, httpHeaders), String.class);
            }
            String bodyStr = body instanceof String ? (String) body : JsonUtil.toJson(body);
            byte[] bodyBytes = bodyStr.getBytes(StandardCharsets.UTF_8);
            // 不走 RestTemplate：其底层 HttpURLConnection 会在写 body 且未设 Content-Type 时
            // 强制补 application/x-www-form-urlencoded，无法去除，故用原始 Socket 手写报文
            return sendRawWithoutContentType(uri, requestMethod, httpHeaders, bodyBytes);
        } catch (Exception e) {
            logger.error("请求http失败", e);
            throw e;
        }
    }

    /**
     * 使用原始 Socket 按字节发送 http/https 请求，保证不携带 Content-Type。
     * 注意：不跟随重定向、不使用代理，https 使用默认证书校验。
     *
     * @param uri
     * @param method
     * @param headers
     * @param bodyBytes
     * @return
     */
    private ResponseEntity<String> sendRawWithoutContentType(URI uri, HttpMethod method, HttpHeaders headers, byte[] bodyBytes) throws IOException {
        boolean https = "https".equalsIgnoreCase(uri.getScheme());
        String host = uri.getHost();
        int port = uri.getPort() != -1 ? uri.getPort() : (https ? 443 : 80);
        try (Socket socket = https
                ? SSLSocketFactory.getDefault().createSocket(host, port)
                : new Socket(host, port)) {
            OutputStream out = socket.getOutputStream();
            String path = (uri.getRawPath() == null || uri.getRawPath().isEmpty() ? "/" : uri.getRawPath())
                    + (uri.getRawQuery() != null ? "?" + uri.getRawQuery() : "");
            StringBuilder head = new StringBuilder();
            head.append(method.name()).append(' ').append(path).append(" HTTP/1.1\r\n");
            String userHost = getFirstIgnoreCase(headers, HttpHeaders.HOST);
            if (userHost != null) {
                head.append(HttpHeaders.HOST).append(": ").append(userHost).append("\r\n");
            } else {
                String portPart = (https && port == 443) || (!https && port == 80) ? "" : ":" + port;
                head.append("Host: ").append(host).append(portPart).append("\r\n");
            }
            Set<String> skip = new HashSet<>(Arrays.asList(HttpHeaders.HOST, HttpHeaders.CONTENT_LENGTH, HttpHeaders.TRANSFER_ENCODING, HttpHeaders.CONNECTION));
            headers.forEach((name, values) -> {
                if (skip.stream().noneMatch(s -> s.equalsIgnoreCase(name))) {
                    values.forEach(v -> head.append(name).append(": ").append(v).append("\r\n"));
                }
            });
            if (bodyBytes != null) {
                head.append(HttpHeaders.CONTENT_LENGTH).append(": ").append(bodyBytes.length).append("\r\n");
            }
            head.append("Connection: close\r\n\r\n");
            out.write(head.toString().getBytes(StandardCharsets.ISO_8859_1));
            if (bodyBytes != null) {
                out.write(bodyBytes);
            }
            out.flush();
            byte[] respBytes = readAll(socket.getInputStream());
            return parseRawResponse(respBytes);
        }
    }

    private String getFirstIgnoreCase(HttpHeaders headers, String name) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name) && !entry.getValue().isEmpty()) {
                return entry.getValue().get(0);
            }
        }
        return null;
    }

    private byte[] readAll(InputStream in) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] chunk = new byte[8192];
        int n;
        while ((n = in.read(chunk)) != -1) {
            buf.write(chunk, 0, n);
        }
        return buf.toByteArray();
    }

    private ResponseEntity<String> parseRawResponse(byte[] respBytes) throws IOException {
        int headerEnd = indexOf(respBytes, 0, "\r\n\r\n");
        if (headerEnd < 0) {
            throw new IOException("http响应格式异常: " + new String(respBytes, StandardCharsets.ISO_8859_1));
        }
        String headPart = new String(respBytes, 0, headerEnd, StandardCharsets.ISO_8859_1);
        String[] lines = headPart.split("\r\n");
        String statusLine = lines[0];
        int code = Integer.parseInt(statusLine.split(" ")[1]);
        HttpStatus status = HttpStatus.resolve(code);
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        HttpHeaders respHeaders = new HttpHeaders();
        for (int i = 1; i < lines.length; i++) {
            int idx = lines[i].indexOf(':');
            if (idx > 0) {
                respHeaders.add(lines[i].substring(0, idx).trim(), lines[i].substring(idx + 1).trim());
            }
        }
        byte[] body = Arrays.copyOfRange(respBytes, headerEnd + 4, respBytes.length);
        String transferEncoding = getFirstIgnoreCase(respHeaders, HttpHeaders.TRANSFER_ENCODING);
        if (transferEncoding != null && transferEncoding.toLowerCase().contains("chunked")) {
            body = dechunk(body);
        }
        String contentEncoding = getFirstIgnoreCase(respHeaders, HttpHeaders.CONTENT_ENCODING);
        if (contentEncoding != null && contentEncoding.toLowerCase().contains("gzip")) {
            try (GZIPInputStream gin = new GZIPInputStream(new ByteArrayInputStream(body))) {
                body = readAll(gin);
            }
        }
        String resBody;
        MediaType contentType = respHeaders.getContentType();
        Charset charset = contentType != null && contentType.getCharset() != null ? contentType.getCharset() : StandardCharsets.UTF_8;
        resBody = new String(body, charset);
        return new ResponseEntity<>(resBody, respHeaders, status);
    }

    private int indexOf(byte[] data, int from, String target) {
        byte[] t = target.getBytes(StandardCharsets.ISO_8859_1);
        outer:
        for (int i = from; i <= data.length - t.length; i++) {
            for (int j = 0; j < t.length; j++) {
                if (data[i + j] != t[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    private byte[] dechunk(byte[] body) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int pos = 0;
        while (true) {
            int lineEnd = indexOf(body, pos, "\r\n");
            if (lineEnd < 0) {
                break;
            }
            String sizeLine = new String(body, pos, lineEnd - pos, StandardCharsets.ISO_8859_1).trim();
            int semi = sizeLine.indexOf(';');
            if (semi >= 0) {
                sizeLine = sizeLine.substring(0, semi);
            }
            if (sizeLine.isEmpty()) {
                pos = lineEnd + 2;
                continue;
            }
            int size = Integer.parseInt(sizeLine, 16);
            pos = lineEnd + 2;
            if (size == 0) {
                break;
            }
            if (pos + size > body.length) {
                break;
            }
            out.write(body, pos, size);
            pos += size + 2;
        }
        return out.toByteArray();
    }

    /**
     * 下载文件
     *
     * @param requestParam
     * @param restTemplateFinal
     * @param fileName
     * @return 文件路径
     */
    public String asyncDownloadFile(RequestParamAllBodyTypeInner requestParam, RestTemplate restTemplateFinal, String fileName) throws IOException {
        String fileKey = System.currentTimeMillis() + "d";
        fileCache.put(fileKey, new LocalFileCacheDto(null, fileName, 1));
//  流式下载
        Path parentFile = Paths.get("./local_file").toAbsolutePath().normalize();
        File file = new File(parentFile.toUri());
        if (!file.exists()) {
            Files.createDirectories(parentFile);
        }
        CompletableFuture.runAsync(() -> {
            try {
                downloadFileBigFile(requestParam, restTemplateFinal, fileName, fileKey, parentFile);
            } catch (Exception e) {
                logger.error("下载文件失败", e);
                fileCache.put(fileKey, new LocalFileCacheDto(null, fileName, 5));
            }
        });
        return fileKey;
    }

    public void downloadFileBigFile(RequestParamAllBodyTypeInner requestParam, RestTemplate restTemplateFinal, String fileName, String fileKey, Path parentFile) {
        RequestCallback requestCallback = request ->
                request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        AtomicInteger count = new AtomicInteger();
        restTemplateFinal.execute(requestParam.getUrl(), HttpMethod.resolve(requestParam.getHttpMethod().toUpperCase()), requestCallback, response -> {
            String fileNameFinal = fileName;
            if (count.get() == 0) {
                if (StringUtils.isEmpty(fileName)) {
                    // 解析文件名
                    fileNameFinal = Optional.ofNullable(response.getHeaders().getContentDisposition())
                            .map(ContentDisposition::getFilename)
                            .orElseGet(() -> {
                                String path = requestParam.getUrl().split("\\?")[0];
                                return path.substring(path.lastIndexOf('/') + 1);
                            });
                }
                if (StringUtils.isEmpty(fileNameFinal)) {
                    fileNameFinal = System.currentTimeMillis() + ".xlsx";
                }
                fileCache.put(fileKey, new LocalFileCacheDto(null, fileNameFinal, 1));
            } else {
                fileNameFinal = fileCache.get(fileKey).getFileName();
            }
            count.getAndIncrement();

            Path targetFile = parentFile.resolve(fileNameFinal);
            // 初始8KB，根据下载速度动态扩大（上限256KB）
            int bufferSize = 8 * 1024;
            long lastTime = System.currentTimeMillis();
            long totalRead = 0;
            try (InputStream is = response.getBody();
                 OutputStream os = Files.newOutputStream(targetFile, StandardOpenOption.CREATE)) {
                byte[] buffer = new byte[bufferSize];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                    // 每1MB数据评估一次
                    totalRead += bytesRead;
                    if (totalRead % (1024 * 1024) == 0) {
                        long timeSpent = System.currentTimeMillis() - lastTime;
                        double speedKBps = 1024 / (timeSpent / 1000.0);
                        // 动态调整缓冲区（范围8KB~256KB）
                        bufferSize = (int) Math.min(256 * 1024, Math.max(8 * 1024, speedKBps * 10));
                        buffer = new byte[bufferSize];
                        lastTime = System.currentTimeMillis();
                    }
                }
            }
            fileCache.put(fileKey, new LocalFileCacheDto(null, fileNameFinal, 2));
            return null;
        });
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
            if (fileData == null) {
                logger.error(requestParam.getUrl() + "请求返回文件为空");
                return null;
            }
            fileName = getFileNameFromResponse(fileName, response);
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
                    outputStream.write(fileData);
                }
            } catch (IOException e) {
                logger.error("文件操作异常:", e);
            }
            return file;
        } else {
            logger.error("请求http失败,返回：{}", JsonUtil.toJson(response));
        }
        return null;
    }

    public <T> String getFileNameFromResponse(String fileName, ResponseEntity<T> response) {
        List<String> resHeaders = response.getHeaders().get("Content-Disposition");
        if (StringUtils.isEmpty(fileName)) {
            fileName = System.currentTimeMillis() + ".xlsx";
            if (resHeaders != null) {
                for (String resHeader : resHeaders) {
                    if (resHeader.startsWith("filename") || resHeader.startsWith("attachment")) {
                        String fileNameTmp = resHeader.split("filename=")[1].replace("\"", "");
                        if (FileNameValidator.isValidFilename(fileNameTmp, 0)) {
                            fileName = URLDecoder.decode(fileNameTmp);
                        }
                    }
                }
            }
        }
        return fileName;
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
