package com.netease.lowcode.extension.excel;

public class ExcelParseException extends RuntimeException {
    public ExcelParseException(String msg) {
        super(msg);
    }

    public ExcelParseException(String msg, Throwable e) {
        super(msg, e);
    }
}
