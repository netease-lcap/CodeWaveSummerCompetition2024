package com.netease.lowcode.freemarker.validators;

import com.netease.lowcode.freemarker.dto.CreateRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class CreateRequestValidator {

    public static void validate(CreateRequest request) {

        if (Objects.isNull(request)) {
            throw new RuntimeException("请求为空");
        }

        if (StringUtils.isBlank(request.templateUrl)) {
            throw new RuntimeException("模板url不能为空");
        }

        if (StringUtils.isBlank(request.jsonData)) {
            throw new RuntimeException("jsonData为空");
        }

        if(StringUtils.isBlank(request.outFileName)){
            throw new RuntimeException("输出文件名称outFileName不能为空");
        }

    }
}