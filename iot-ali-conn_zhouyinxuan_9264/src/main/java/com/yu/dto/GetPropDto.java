package com.yu.dto;

import com.netease.lowcode.core.annotation.NaslStructure;
import com.yu.annotation.Validate;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/21 14:03
 **/
@NaslStructure
@ToString
public class GetPropDto {
    public Integer asc = 0;
    public String deviceName;
    @Validate(required = true)
    public Long endTime;
    @Validate(required = true)
    public String identifier;
    public String iotId;
    public String iotInstanceId;
    public Integer pageSize = 10;
    public String productKey;
    @Validate(required = true)
    public Long startTime;
}
