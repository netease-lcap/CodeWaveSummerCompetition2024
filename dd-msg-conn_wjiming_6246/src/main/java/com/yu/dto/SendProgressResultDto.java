package com.yu.dto;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/19 17:15
 **/
@NaslStructure
@ToString
public class SendProgressResultDto {
    /**
     * 发送百分比
     */
    public Long progressInPercent;
    /**
     * 发送状态
     */
    public Long status;
}
