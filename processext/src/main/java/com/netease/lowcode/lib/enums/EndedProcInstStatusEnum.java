package com.netease.lowcode.lib.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EndedProcInstStatusEnum {
    ORIGINAL_ENDED("ORIGINAL_ENDED", 0, "流程结束（流程原生）"),
    ENDED("ENDED", 1, "流程结束"),
    CANCELLED("CANCELLED", 2, "流程取消"),
    DELETED("DELETED", 3, "流程删除"),
    ;
    public final String code;
    public final Integer endType;
    public final String desc;

    EndedProcInstStatusEnum(String code, Integer endType, String desc) {
        this.code = code;
        this.endType = endType;
        this.desc = desc;
    }

    /**
     * 判断endType是否存在于EndedProcInstStatusEnum
     *
     * @param endType
     * @return true/false
     */
    public static Boolean isExist(Integer endType) {
        for (EndedProcInstStatusEnum endedProcInstStatusEnum : EndedProcInstStatusEnum.values()) {
            if (endedProcInstStatusEnum.endType.equals(endType)) {
                return true;
            }
        }
        return false;
    }

    public Integer getEndType() {
        return endType;
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
