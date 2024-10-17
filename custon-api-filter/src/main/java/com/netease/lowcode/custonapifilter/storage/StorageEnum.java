package com.netease.lowcode.custonapifilter.storage;

public enum StorageEnum {
    REDIS("redis"),
    LOCAL("local"),
    DB("db"),
    ;
    public final String type;

    StorageEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
