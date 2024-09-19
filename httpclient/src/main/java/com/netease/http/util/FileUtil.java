package com.netease.http.util;

import com.netease.http.dto.UploadResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component("httpClientFileUtils")
public class FileUtil {

    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    // 注入应用的存储类型，可选的有local/s3
    @Value("${lcp.upload.sinkType}")
    private String sinkType;
    // 注入应用的存储路径信息，默认 /app
    @Value("${lcp.upload.sinkPath}")
    private String sinkPath;
    // 注入spring上下文，用于获取bean示例信息
    @Autowired
    private ApplicationContext applicationContext;

    public UploadResponseDTO uploadStream(InputStream fis, String fileName)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 获取实例com.defaulttenant.exporttest.filestorage.FileStorageClientManager
        Object clientManager = applicationContext.getBean("fileStorageClientManager");
        // 获取文件系统spi方法，取决于sinkType，目前有local/s3两种实现
        Method getFileSystemSpi = clientManager.getClass().getMethod("getFileSystemSpi", String.class);
        // 调用文件系统spi，获取真正的文件系统实例
        Object fileStorageClient = getFileSystemSpi.invoke(clientManager, sinkType);
        // 获取文件实例的upload方法
        Method upload = fileStorageClient.getClass().getMethod("upload", InputStream.class, String.class, Map.class);
        // 只要拼接 sinkPath+fileName+时间+后缀即可。本案例中fileName就是时间戳，因此不需要拼接时间。
        String fileExt = "";
        if (fileName.contains(".")) {
            int i = fileName.lastIndexOf(".");
            fileExt = fileName.substring(i);
            fileName = fileName.substring(0, i);
        }

        // 组装文件上传位置
        String savePath = String.join("/", sinkPath, fileName + fileExt);
        // 调用upload实现文件上传，返回保存文件的相对地址 /app/xxx
        String filePath = (String) upload.invoke(fileStorageClient, fis, savePath, new HashMap<>());
        // 组装链接
        UploadResponseDTO responseDTO = new UploadResponseDTO();
        // 设置文件相对下载路径，用户拼接制品域名后可下载。
        responseDTO.setFilePath("/upload" + filePath);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = requestAttributes.getRequest();
            // 设置文件完整下载url，自动获取请求地址
            responseDTO.setResult(request.getScheme() + "://" + request.getServerName() +
                    (80 == request.getServerPort() ? "" : ":" + request.getServerPort()) +
                    "/upload" + filePath);
        } else {
            responseDTO.setResult(responseDTO.getFilePath());
        }
        return responseDTO;
    }
}
