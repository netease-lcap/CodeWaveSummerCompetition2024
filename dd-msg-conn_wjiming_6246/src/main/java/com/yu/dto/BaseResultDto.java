package com.yu.dto;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/19 17:32
 * 基础返回结果类
 **/
@NaslStructure
@ToString
public class BaseResultDto {
    /**
     * 错误码
     */
    public Long errcode;
    /**
     * 错误消息
     */
    public String errmsg;
    /**
     * 细分错误码
     */
    public Long sub_code;
    /**
     * 细分错误消息
     */
    public String sub_msg;


}
