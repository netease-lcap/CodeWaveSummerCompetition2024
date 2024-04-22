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
    @Validate(required = true)
    public String args;
    public String deviceName;
    @Validate(required = true)
    public String identifier;
    public String iotId;
    public String iotInstanceId;
    public String productKey;
}
