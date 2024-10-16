package com.netease.lowcode.custonapifilter.storage.redis;

import com.netease.lowcode.custonapifilter.sign.impl.SignNaslConfiguration;
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
    private static final Logger logger = LoggerFactory.getLogger(RedisStorageServiceImpl.class);
    @Resource
    private RedisConnector redisConnector;

    @Override
    public boolean checkAndAddIfAbsent(String key, Long timeout) {
        try {
            String nonceExist = redisConnector.getValue(key);
            if (nonceExist != null) {
                logger.info("redis key:{} 已存在", key);
                return false;
            } else {
                redisConnector.setValueTimeOut(key, key, timeout);
            }
        } catch (Exception e) {
            logger.error("redis连接失败", e);
        }
        return true;
    }

    @Override
    public String type() {
        return StorageEnum.REDIS.getType();
    }
}
