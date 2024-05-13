package com.yu.dto;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/19 17:35
 * 获取工作通知消息的发送结果
 **/
@NaslStructure
@ToString
public class GetSendResultDto extends BaseResultDto {
    /**
     * 请求id
     */
    public String request_id;
    public QuerySendResultDto send_result;

}
