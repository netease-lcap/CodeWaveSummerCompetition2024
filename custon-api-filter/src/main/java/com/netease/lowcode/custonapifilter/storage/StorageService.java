package com.netease.lowcode.custonapifilter.storage;

public interface StorageService {
    boolean checkAndAddIfAbsent(String key, Long timeout);

    String type();
}
