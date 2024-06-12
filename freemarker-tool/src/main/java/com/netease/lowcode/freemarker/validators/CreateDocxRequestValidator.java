package com.netease.lowcode.freemarker.validators;

import com.netease.lowcode.freemarker.dto.CreateDocxRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class CreateDocxRequestValidator {

    public static void validate(CreateDocxRequest request) {

        if(Objects.isNull(request)){
            throw new RuntimeException("请求为空");
        }
        if(StringUtils.isBlank(request.jsonData)){
            throw new RuntimeException("jsonData为空");
        }

        if(StringUtils.isBlank(request.templateDocxFileUrl)){
            throw new RuntimeException("原模板docx文件不能为空");
        }

        if(StringUtils.isBlank(request.outFileName)||!request.outFileName.endsWith(".docx")){
            throw new RuntimeException("导出文件后缀不是 *.docx");
        }
    }

}