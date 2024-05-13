package com.netease.lowcode.extension.excel.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExcelParseResult<T> {
    public List<T> data;
    public List<Map<String, String>> unParsedData;
    public List<ExcelParseError> errors;
    public Boolean success;
}
