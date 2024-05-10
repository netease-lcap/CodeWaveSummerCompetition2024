package com.yu.vo;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.ToString;

import java.util.List;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/21 17:52
 **/
@NaslStructure
@ToString
public class GetProdVo {
    /**
     * 分页数量
     */
    public Integer pageCount;
    /**
     * 数据数量
     */
    public Integer total;
    public List<ProductInfo> productInfos;
}
