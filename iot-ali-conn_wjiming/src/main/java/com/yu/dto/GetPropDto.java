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

    /**
     * 排序 0倒序 1正序
     */
    public Long asc = 0L;
    /**
     * 设备名称
     */
    public String deviceName;
    /**
     * 结束时间
     */
    @Validate(required = true)
    public Long endTime;

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
     * 分页数量
     */
    public Long pageSize = 10L;
    /**
     * 产品key
     */
    public String productKey;
    /**
     * 开始时间
     */
    @Validate(required = true)
    public Long startTime;
}
