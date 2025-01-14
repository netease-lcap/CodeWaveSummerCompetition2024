package com.netease.lowcode.lib.officetopdf.api;


import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.lib.officetopdf.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 文件相关复写逻辑
 */
@Component
public class FileOverrideApi {

    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    /**
     * 判断是否存在fileUrl对应的记录
     *
     * @param fileUrl
     * @return 文件key
     */
    @NaslLogic(override = true)
    public String isPreviewExist(String fileUrl) {
        return null;
    }

    /**
     * 保存info信息
     *
     * @param resultDTO
     * @return
     */
    @NaslLogic(override = true)
    public Boolean updatePreviewInfo(ResultDTO resultDTO) {
        return true;
    }

    /**
     * 查询key对应的urlList和real count，
     *
     * @param key
     * @return ResultDTO结果
     */
    @NaslLogic(override = true)
    public ResultDTO getPreviewInfo(String key) {
        return null;
    }

}
