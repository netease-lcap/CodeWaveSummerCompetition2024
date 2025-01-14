package com.netease.lowcode.lib.officetopdf.util.office;

import com.aspose.slides.ISlideCollection;
import com.aspose.slides.License;
import com.aspose.slides.Presentation;
import com.netease.lowcode.lib.officetopdf.dto.FileTransferConfig;
import com.netease.lowcode.lib.officetopdf.util.FileUtil;
import com.netease.lowcode.lib.officetopdf.util.FilePicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class AsposePPTUtil {
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    private volatile static License licenseInstance;
    private volatile static Boolean licenseInitEnable = true;

    @Resource
    private FileUtil officeBigFileUtils;
    @Resource
    private FilePicUtil filePicUtil;
    @Resource
    private FileTransferConfig fileTransferConfig;

    private void loadLicence() {
        //获取 license 去除水印,若不验证则转化出的pdf文档会有水印产生
        if (StringUtils.isEmpty(fileTransferConfig.getLicenseUrl())) {
            licenseInitEnable = false;
            return;
        }
        if (licenseInstance == null && licenseInitEnable) {
            synchronized (this) {
                InputStream is;
                try {
                    is = officeBigFileUtils.downloadFile(fileTransferConfig.getLicenseUrl());
                } catch (Exception e) {
                    licenseInitEnable = false;
                    log.error("下载license文件失败", e);
                    return;
                }
                try {
                    licenseInstance = new License();
                    licenseInstance.setLicense(is);
                } catch (Exception e) {
                    licenseInitEnable = false;
                    log.error("license verify failed", e);
                }
            }
        }
    }

    /**
     * ppt转pdf，大文件存在内存溢出
     *
     * @param fis
     * @param //saveImageFunc , Function2<File, String, String> saveImageFunc
     * @return
     */
    @Async("taskExecutor")
    public synchronized String ppt2pdf(InputStream fis, String key, Function<File, String> saveImageFunc) {
        log.info("Conversion from ppt to PDF start,key:{} ", key);
        String pdfFile = key + ".pdf";
        File pdfFileObj = new File(pdfFile);
        long startTime = System.currentTimeMillis();
        try (FileOutputStream os = new FileOutputStream(pdfFile)) {
            Presentation pres = new Presentation(fis);
            pres.save(os, com.aspose.slides.SaveFormat.Pdf);
            log.info("Conversion from ppt to PDF completed. PDF file saved at:{} ", pdfFile);
            long endTime = System.currentTimeMillis();
            log.info("Conversion took " + (endTime - startTime) + " milliseconds.");
            if (saveImageFunc != null) {
                saveImageFunc.apply(pdfFileObj);
            }
            return pdfFile;
        } catch (Exception e) {
            log.error("Failed to convert ppt to PDF", e);
            return null;
        } finally {
            if (pdfFileObj != null) {
                pdfFileObj.delete();
            }
        }
    }

    /**
     * ppt转图片
     *
     * @param fis
     * @param key
     * @return
     */
    @Async("taskExecutor")
    public synchronized List<String> ppt2Pic(InputStream fis, String key, Function<Object[], String> saveImageFunc, Function<Object[], Boolean> saveCountFunc) {
        loadLicence();
        long startTime = System.currentTimeMillis();
        List<String> imageFiles = new ArrayList<>();
        try {
            log.info("Conversion from ppt to images start,key:{} ", key);
            Presentation pres = new Presentation(fis);
            ISlideCollection iSlides = pres.getSlides();
            if (saveCountFunc != null) {
                Long count = (long) iSlides.size();
                Object[] args = {key, count};
                saveCountFunc.apply(args);
            }
            for (int i = 0; i < iSlides.size(); i++) {
                File imageFile = null;
                try {
                    // 获取ppt单页图
                    BufferedImage img = iSlides.get_Item(i).getThumbnail(2, 2);
                    String imageFileName = key + (i + 1) + ".png";
                    log.info("Conversion from ppt to images ,{}", imageFileName);
                    imageFile = new File(imageFileName);
                    // 文件压缩保存
                    filePicUtil.compressPic(imageFileName, img);
                    // 原样保存
//                    ImageIO.write(img, "png", imageFile);
                    imageFiles.add(imageFileName);
                    if (saveImageFunc != null) {
                        Object[] args = {key, imageFile};
                        imageFiles.add(saveImageFunc.apply(args));
                    }
                } catch (Exception e) {
                    log.error("transferToPdf error", e);
                    throw e;
                } finally {
                    if (imageFile != null) {
                        if (imageFile.exists()) {
                            imageFile.delete();
                        }
                    }
                }
            }
            log.info("Conversion from ppt to images completed.");
            return imageFiles;
        } catch (Exception e) {
            log.error("Failed to convert ppt to images", e);
            return imageFiles;
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Conversion took " + (endTime - startTime) + " milliseconds.");
            try {
                Files.deleteIfExists(Paths.get(key + ".ppt"));
            } catch (Exception e1) {
                log.error("删除{}文件失败", Paths.get(key + ".ppt"), e1);
            }
        }
    }
}
