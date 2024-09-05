package com.netease.lowcode.freemarker.dto;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class CreateRequest {

    /**
     * 模板url
     */
    public String templateUrl;
    /**
     * 模板数据
     */
    public String jsonData;

    /**
     * 输出的文件名 带后缀
     */
    public String outFileName;
}
