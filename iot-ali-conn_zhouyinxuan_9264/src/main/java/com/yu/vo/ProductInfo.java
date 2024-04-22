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
    public String authType;
    public Integer dataFormat;
    public String description;
    public Integer deviceCount;
    public Long gmtCreate;
    public Integer nodeType;
    public String productKey;
    public String productName;
}
