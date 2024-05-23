package com.netease.lowcode.extensions.model;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class ComplexFillRequest {
    public String fileName;
    public String templateUrl;

    /**
     * 必须是json数据
     */
    public String jsonData;
    public List<String> listJsonData;

}
