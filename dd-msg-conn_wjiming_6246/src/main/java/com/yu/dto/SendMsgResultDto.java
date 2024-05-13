// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.yu.dto;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.ToString;

/**
 * 发送工作通知的返回结果
 */
@NaslStructure
@ToString(callSuper = true)
public class SendMsgResultDto extends BaseResultDto {
    /**
     * 任务id
     */
    public Long task_id;
    /**
     * 请求id
     */
    public String request_id;


}
