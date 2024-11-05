package com.netease.lowcode.extensions;

import com.netease.lowcode.extensions.util.FileConnectorUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
    private FileConnectorUtils fileConnectorUtils;
    @Autowired
    private ApplicationContext applicationContext;

    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        for (File listFile : file.listFiles()) {
            delete(listFile);
        }
    }

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
        if (!containsBean) {
            fileName = fileName + "_" + curTime + fileExt;//防止文件被覆盖，可按需选择
            return fileConnectorUtils.Base64FileUploadV2(fis, fileName, new HashMap<>());
        }

        Object clientManager = applicationContext.getBean("fileStorageClientManager");
        Method getFileSystemSpi = clientManager.getClass().getMethod("getFileSystemSpi", String.class);
        Object fileStorageClient = getFileSystemSpi.invoke(clientManager, sinkType);

        Method upload = fileStorageClient.getClass().getMethod("upload", InputStream.class, String.class, Map.class);


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
}

