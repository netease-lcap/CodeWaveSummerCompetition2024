package com.netease.lowcode.freemarker.dto;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.Map;

@NaslStructure
public class CreateDocxRequest {
    /**
     * 模板url,key=模板名称,value=模板url地址
     */
    public Map<String,String> templateUrl;

    /**
     * 导出文件名 *.docx
     */
    public String outFileName;

    /**
     * 原模板文件url
     */
    public String templateDocxFileUrl;

    /**
     * 文本数据json
     */
    public String jsonData;

    /**
     * 图片 key = 图片名称, value = 图片url
     */
    public Map<String, String> imageMap;
    /**
     * 如果为true，表示图片传入的是base64编码
     */
    public Boolean base64 = false;
}
