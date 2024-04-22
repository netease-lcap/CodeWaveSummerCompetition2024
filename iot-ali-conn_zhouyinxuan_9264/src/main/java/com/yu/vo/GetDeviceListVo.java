package com.yu.vo;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.ToString;

import java.util.List;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/21 15:19
 **/
@NaslStructure
@ToString
public class GetDeviceListVo {
    public String NextToken;
    public Integer Total;
    public Integer PageCount;
    public List<DeviceInfo> deviceInfos;
}
