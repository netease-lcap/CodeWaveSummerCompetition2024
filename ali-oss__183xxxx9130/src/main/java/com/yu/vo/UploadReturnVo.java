package com.yu.vo;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/24 9:50
 **/
@ToString
@Getter
@Setter
@NaslStructure
public class UploadReturnVo {
    /**
     * eTag
     */
    public String eTag;
    /**
     * 请求id
     */
    public String requestId;
    /**
     * client校验码
     */
    public String clientCRC;
    /**
     * server校验码
     */
    public String serverCRC;
}
