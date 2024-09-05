package com.netease.lowcode.dubbo.dubbo.generic;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.Map;

@NaslStructure
public class Parameter {
    /**
     * 参数类型
     */
    public String type;
    /**
     * 参数值
     */
    public Map<String, String> arg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getArg() {
        return arg;
    }

    public void setArg(Map<String, String> arg) {
        this.arg = arg;
    }
}
