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
    /**
     * 下一页分页标识
     */
    public String NextToken;
    /**
     * 总数量
     */
    public Integer Total;
    /**
     * 分页数量
     */
    public Integer PageCount;
    public List<DeviceInfo> deviceInfos;
}
