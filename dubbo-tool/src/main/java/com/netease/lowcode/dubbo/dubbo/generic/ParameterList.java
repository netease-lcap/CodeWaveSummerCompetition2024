package com.netease.lowcode.dubbo.dubbo.generic;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class ParameterList {
    /**
     * 参数列表
     */
    public List<Parameter> parameterParameter;

    public List<Parameter> getParameterParameter() {
        return parameterParameter;
    }

    public void setParameterParameter(List<Parameter> parameterParameter) {
        this.parameterParameter = parameterParameter;
    }
}
