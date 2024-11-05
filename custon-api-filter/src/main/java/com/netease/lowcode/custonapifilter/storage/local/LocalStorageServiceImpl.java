package com.netease.lowcode.custonapifilter.storage.local;

import com.netease.lowcode.custonapifilter.storage.StorageEnum;
import com.netease.lowcode.custonapifilter.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * redis存储实现类
 */
@Component
public class LocalStorageServiceImpl implements StorageService {
    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public void put(String key, long ttl) {
        map.put(key, key);
        scheduler.schedule(() -> evict(key), ttl, TimeUnit.MILLISECONDS);
    }

    private void evict(String key) {
        map.remove(key);
    }

    @Override
    public boolean checkAndAddIfAbsent(String key, Long timeout) {
        boolean nonceExist = containsKey(key);
        if (nonceExist) {
            return false;
        } else {
            put(key, timeout);
        }
        return true;
    }

    @Override
    public String type() {
        return StorageEnum.LOCAL.getType();
    }
}
