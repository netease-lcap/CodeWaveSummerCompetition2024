package com.yu.vo;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/21 17:59
 **/
@NaslStructure
@ToString
@Getter
@Setter
public class ProductInfo {
    /**
     * 校验类型
     */
    public String authType;
    /**
     * 数据格式
     */
    public Integer dataFormat;
    /**
     * 备注描述
     */
    public String description;
    /**
     * 设备数量
     */
    public Integer deviceCount;
    /**
     * 创建时间
     */
    public Long gmtCreate;
    /**
     * 节点类型
     */
    public Integer nodeType;
    /**
     * 产品标识
     */
    public String productKey;
    /**
     * 产品名称
     */
    public String productName;
}
