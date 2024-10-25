package com.netease.lowcode.custonapifilter.storage.redis;

import com.netease.lowcode.custonapifilter.storage.StorageEnum;
import com.netease.lowcode.custonapifilter.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * redis存储实现类
 */
@Component
public class RedisStorageServiceImpl implements StorageService {
    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Resource
    private RedisConnector redisConnector;

    @Override
    public boolean checkAndAddIfAbsent(String key, Long timeout) {
        try {
            String nonceExist = redisConnector.getValue(key);
            if (nonceExist != null) {
                log.info("redis key:{} 已存在", key);
                return false;
            } else {
                redisConnector.setValueTimeOut(key, key, timeout);
            }
        } catch (Exception e) {
            log.error("redis连接失败", e);
        }
        return true;
    }

    @Override
    public String type() {
        return StorageEnum.REDIS.getType();
    }
}
