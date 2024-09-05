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
    /**
     * 当前页码
     */
    public Long currentPage = 1L;
    /**
     * 下一页的token
     */
    public String nextToken;
    /**
     * 每页数量
     */
    public Long pageSize = 10L;
    /**
     * 产品key
     */
    public String productKey;
}
