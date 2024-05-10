package com.yu.dto;

import com.netease.lowcode.core.annotation.NaslStructure;
import com.yu.annotation.Validate;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/21 17:41
 **/
@NaslStructure
@ToString
public class InvokeServDto {
    /**
     * 输入参数
     */
    @Validate(required = true)
    public String args;
    /**
     * 设备名称
     */
    public String deviceName;
    /**
     * 标识符
     */
    @Validate(required = true)
    public String identifier;
    /**
     * iotId
     */
    public String iotId;
    /**
     * 产品key
     */
    public String productKey;
}
