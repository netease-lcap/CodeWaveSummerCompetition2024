package com.netease.lowcode.lib.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.lib.dto.RequestInfoStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckSafeApi {

    //参数使用LCAP_EXTENSION_LOGGER后日志会显示在平台日志功能中
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    /**
     * 验证请求安全（默认验证文件url是否包含file协议）
     *
     * @param requestInfoStructure
     * @return
     */
    @NaslLogic(override = true)
    public static Boolean checkRequestInfo(RequestInfoStructure requestInfoStructure) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(requestInfoStructure.getBody());
        } catch (JsonProcessingException e) {
            log.error("", e);
            return true;
        }
        JsonNode urlListJsonNode = jsonNode.get("urls");
        if (urlListJsonNode.isArray()) {
            for (JsonNode urlNode : urlListJsonNode) {
                String url = urlNode.asText();
                if (url.startsWith("file://")) {
                    log.error("{},url不能是file协议", url);
                    return false;
                }
            }
        }
        return true;
    }

}
