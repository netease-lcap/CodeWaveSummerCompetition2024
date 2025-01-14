package com.netease.lowcode.lib.officetopdf.api;


import com.netease.lowcode.annotation.helper.provider.OverriddenFrameworkHelper;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.lib.officetopdf.dto.FileTransferConfig;
import com.netease.lowcode.lib.officetopdf.dto.ResultDTO;
import com.netease.lowcode.lib.officetopdf.exception.TransferCommonException;
import com.netease.lowcode.lib.officetopdf.util.FilePicUtil;
import com.netease.lowcode.lib.officetopdf.util.FileUtil;
import com.netease.lowcode.lib.officetopdf.util.office.ApachePPTUtil;
import com.netease.lowcode.lib.officetopdf.util.office.AsposePPTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * 文件转换接口
 */
@Component
public class TransferApi {

    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Resource
    private FileUtil officeBigFileUtils;
    @Resource
    private AsposePPTUtil asposePPTUtil;
    @Resource
    private FileTransferConfig fileTransferConfig;
    @Resource
    private ApachePPTUtil apachePPTUtil;
    @Resource
    private FilePicUtil filePicUtil;

    /**
     * ppt转pdf
     *
     * @param pptUrl
     * @return 文件异步获取key
     */
    @NaslLogic
    public String pptTransfer(String pptUrl) throws TransferCommonException {
        log.info("pptUrl req:{}", pptUrl);
        String pptFile = null;
        String fileNameKey;
        if (StringUtils.isEmpty(pptUrl)) {
            log.info("参数pptUrl为空");
            return null;
        }
        try {
            fileNameKey = FilePicUtil.fileUrlMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equals(pptUrl))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
            if (!StringUtils.isEmpty(fileNameKey)) {
                return fileNameKey;
            }
            //  pptUrl判重，若已存在则直接返回key；
            fileNameKey = (String) OverriddenFrameworkHelper
                    .invokeOverriddenMethod0("isPreviewExist", "office-to-pdf", pptUrl);
            if (!StringUtils.isEmpty(fileNameKey)) {
                return fileNameKey;
            }
        } catch (Exception e) {
            log.error("内存/数据库判重异常", e);
            throw new TransferCommonException(-1, "根据入参文件地址内存/数据库判重异常" + e.getMessage());
        }
        fileNameKey = String.valueOf(System.currentTimeMillis());
        try {
            InputStream inputStream = officeBigFileUtils.downloadFile(pptUrl);
            pptFile = fileNameKey + ".ppt";
            officeBigFileUtils.saveInputStreamToFile(inputStream, pptFile);
            FileInputStream inputStream2 = new FileInputStream(pptFile);
            if ("2".equals(fileTransferConfig.getTransferType())) {
                asposePPTUtil.ppt2Pic(inputStream2, fileNameKey, filePicUtil::saveImage, filePicUtil::saveCountFunc);
            } else {
                apachePPTUtil.transferToPicList(inputStream2, fileNameKey, filePicUtil::saveImage, filePicUtil::saveCountFunc);
            }
            FilePicUtil.fileUrlMap.putIfAbsent(fileNameKey, pptUrl);
            FilePicUtil.picCountMap.putIfAbsent(fileNameKey, 0L);
            return fileNameKey;
        } catch (Exception e) {
            FilePicUtil.picCountMap.remove(fileNameKey);
            FilePicUtil.picUrlListMap.remove(fileNameKey);
            FilePicUtil.fileUrlMap.remove(fileNameKey);
            try {
                Files.deleteIfExists(Paths.get(pptFile));
            } catch (Exception e1) {
                log.error("删除{}文件失败", Paths.get(pptFile), e1);
            }
            log.error("ppt转pdf失败", e);
            throw new TransferCommonException(-1, "ppt转pdf失败:" + e.getMessage());
        } catch (Error e) {
            log.error("ppt转pdf失败", e);
            throw new TransferCommonException(-1, "ppt转pdf失败:" + e.getMessage());
        }
    }


    @NaslLogic
    public ResultDTO getTransferResult(String key) throws TransferCommonException {
        try {
            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setKey(key);
            if (StringUtils.isEmpty(key)) {
                resultDTO.setStatus(2);
                return resultDTO;
            }
            Long count = FilePicUtil.picCountMap.get(key);
            List<String> picList = FilePicUtil.picUrlListMap.get(key);
            if (count == null) {
                ResultDTO resultDTODb = (ResultDTO) OverriddenFrameworkHelper
                        .invokeOverriddenMethod0("getPreviewInfo", "office-to-pdf", key);
                if (resultDTODb != null) {
                    return resultDTODb;
                }
                resultDTO.setStatus(2);
                return resultDTO;
            }
            if (count != 0L && picList != null && count == picList.size()) {
                resultDTO.setStatus(0);
            } else {
                resultDTO.setStatus(1);
            }
            resultDTO.setRealCount(count);
            resultDTO.setPageUrls(picList);
            resultDTO.setFileUrl(FilePicUtil.fileUrlMap.get(key));
            if (resultDTO.getStatus() == 0) {
                FilePicUtil.picCountMap.remove(key);
                FilePicUtil.picUrlListMap.remove(key);
                FilePicUtil.fileUrlMap.remove(key);
                OverriddenFrameworkHelper
                        .invokeOverriddenMethod0("updatePreviewInfo", "office-to-pdf", resultDTO);
            }
            return resultDTO;
        } catch (Exception e) {
            log.error("获取pdf结果失败", e);
            throw new TransferCommonException(-1, "获取pdf结果失败:" + e.getMessage());
        }
    }
}
