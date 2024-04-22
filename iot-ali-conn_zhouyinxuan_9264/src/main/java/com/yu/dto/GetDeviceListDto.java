package com.yu.dto;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/21 15:14
 **/
@NaslStructure
@ToString
public class GetDeviceListDto {
    public Integer currentPage = 1;
    public String iotInstanceId;
    public String nextToken;
    public Integer pageSize = 10;
    public String productKey;
}
