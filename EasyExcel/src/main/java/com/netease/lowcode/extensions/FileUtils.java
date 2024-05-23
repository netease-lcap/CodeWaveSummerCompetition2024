package com.netease.lowcode.extensions;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component("easyExcelFileUtils")
public class FileUtils {

    @Value("${lcp.upload.sinkType}")
    private String sinkType;
    @Value("${lcp.upload.sinkPath}")
    private String sinkPath;
    @Value("${lcp.upload.access}")
    private String access;

    @Autowired
    private ApplicationContext applicationContext;

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

        Object clientManager = applicationContext.getBean("fileStorageClientManager");
        Method getFileSystemSpi = clientManager.getClass().getMethod("getFileSystemSpi", String.class);
        Object fileStorageClient = getFileSystemSpi.invoke(clientManager, sinkType);

        Method upload = fileStorageClient.getClass().getMethod("upload", InputStream.class, String.class, Map.class);
        // http://dev.exporttest.defaulttenant.lcap.codewave-dev.163yun.com/upload/app/%E5%A4%A7%E6%95%B0%E6%8D%AE%E5%AF%BC%E5%87%BA%E6%B5%8B%E8%AF%95_20240106093632186.xlsx

        // 只要拼接 sinkPath+fileName+时间+后缀即可。
        String curTime = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");

        String fileName = file.getName();
        String fileExt = "";
        if (fileName.contains(".")) {
            int i = fileName.lastIndexOf(".");
            fileExt = fileName.substring(i);
            fileName = fileName.substring(0, i);
        }

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

