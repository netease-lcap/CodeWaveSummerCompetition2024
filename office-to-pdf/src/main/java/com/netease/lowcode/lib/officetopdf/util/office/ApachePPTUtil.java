package com.netease.lowcode.lib.officetopdf.util.office;

import com.netease.lowcode.lib.officetopdf.util.FilePicUtil;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

@Component
public class ApachePPTUtil {

    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Resource
    private FilePicUtil filePicUtil;

    /**
     * ppt转pdf
     *
     * @param inputStream
     * @return 每页图片List
     * @throws Exception
     */
    @Async("taskExecutor")
    public synchronized Boolean transferToPicList(InputStream inputStream, String key, Function<Object[], String> saveImageFunc, Function<Object[], Boolean> saveCountFunc) {
        long t1 = System.currentTimeMillis();
        try {
            XMLSlideShow ppt = new XMLSlideShow(inputStream);
            long count = ppt.getSlides().size();
            if (saveCountFunc != null) {
                Object[] args = {key, count};
                saveCountFunc.apply(args);
            }
            int i = 0;
            for (XSLFSlide slide : ppt.getSlides()) {
                File tempFile = null;
                try {
                    BufferedImage img = new BufferedImage((int) ppt.getPageSize().getWidth(), (int) ppt.getPageSize().getHeight(), BufferedImage.TYPE_INT_RGB);
                    slide.draw(img.createGraphics());
                    String imageFileName = key + (i + 1) + ".png";
                    log.info("Conversion from ppt to images ,{}", imageFileName);
                    tempFile = new File(imageFileName);
                    // 文件压缩保存
//                    filePicUtil.compressPic(imageFileName, img);
                    // 原样保存
                    ImageIO.write(img, "png", tempFile);
                    if (saveImageFunc != null) {
                        Object[] args = {key, tempFile};
                        saveImageFunc.apply(args);
                    }
                    i++;
                } catch (Exception e) {
                    log.error("transferToPdf error", e);
                    throw e;
                } finally {
                    if (tempFile != null) {
                        if (tempFile.exists()) {
                            tempFile.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("transferToPdf error", e);
            return false;
        } finally {
            long t2 = System.currentTimeMillis();
            log.info("Pdf 转 图片 共耗时：" + ((t2 - t1) / 1000.0) + "秒");
            try {
                Files.deleteIfExists(Paths.get(key + ".ppt"));
            } catch (Exception e1) {
                log.error("删除{}文件失败", Paths.get(key + ".ppt"), e1);
            }
        }
        return true;
    }
}
