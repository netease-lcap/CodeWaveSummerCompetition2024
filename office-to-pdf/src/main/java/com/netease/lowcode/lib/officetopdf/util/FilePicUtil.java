package com.netease.lowcode.lib.officetopdf.util;

import com.netease.lowcode.lib.officetopdf.dto.UploadResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Component
public class FilePicUtil {
    public static Map<String, List<String>> picUrlListMap = new HashMap<>();
    public static Map<String, Long> picCountMap = new HashMap<>();
    public static Map<String, String> fileUrlMap = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Resource
    private FileUtil officeBigFileUtils;

    /**
     * 保存图片到nos和数据库
     *
     * @param args
     * @return
     */
    public String saveImage(Object[] args) {
        try {
            File imageFile = (File) args[1];
            String key = (String) args[0];
            UploadResponseDTO uploadResponseDTO = officeBigFileUtils.uploadStream(Files.newInputStream(imageFile.toPath()), imageFile.getName());
            String path = uploadResponseDTO.getResult();
            List<String> picUrlList = picUrlListMap.get(key);
            if (CollectionUtils.isEmpty(picUrlList)) {
                picUrlList = new ArrayList<>();
                picUrlListMap.put(key, picUrlList);
            }
            picUrlList.add(uploadResponseDTO.getResult());
            return path;
        } catch (Exception e) {
            log.error("saveImage error", e);
            throw new RuntimeException("saveImage error", e);
        }
    }

    public Boolean saveCountFunc(Object[] itemArgs) {
        String key = (String) itemArgs[0];
        Long count = (Long) itemArgs[1];
        picCountMap.put(key, count);
        return true;
    }

    public void compressPic(String imageFileName, BufferedImage img) throws IOException {
        ImageWriter writer = null;
        ImageOutputStream ios = null;
        ByteArrayOutputStream baos = null;
        FileOutputStream fos = null;
        try {
            // 文件压缩
            // 创建一个输出流
            baos = new ByteArrayOutputStream();
            // 获取 ImageWriter
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            writer = writers.next();
            // 设置压缩参数
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f); // 压缩质量，0.5 表示压缩一半
            // 将原始图片写入输出流
            ios = ImageIO.createImageOutputStream(baos);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(img, null, null), param);
            // 将压缩后的图像写入文件
            fos = new FileOutputStream(imageFileName);
            fos.write(baos.toByteArray());
        } catch (Exception e) {
            log.error("compressPic error", e);
            throw e;
        } finally {
            // 关闭流
            if (writer != null) {
                writer.dispose();
            }
            if (ios != null) {
                ios.close();
            }
            if (baos != null) {
                baos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }
}
