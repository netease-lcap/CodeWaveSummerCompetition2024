package com.yu.vo;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/21 15:07
 **/
@NaslStructure
@ToString
public class PropertyInfo {
    public String collectTime;
    public String value;

    public PropertyInfo(String time, String value) {
        this.collectTime = time;
        this.value = value;
    }

    public PropertyInfo() {
    }
}
