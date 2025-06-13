package com.netease.lowcode.lib.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProcInstStatusEnum {
    Approved("Approved", "审批结束"),
    Approving("Approving", "审批中"),
    ;
    public final String code;
    public final String desc;

    ProcInstStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonValue
    public String getJsonValue() {
        return this.code;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
