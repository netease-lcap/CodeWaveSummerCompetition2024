package com.netease.lib.redistemplatetool.redission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDistributedLock implements DistributedLock {

    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Resource
    private RedissonService redissonService;

    @Override
    public boolean tryLock(String lockKey) {
        return redissonService.tryLock(lockKey);
    }

    @Override
    public boolean tryLock(String lockKey, long leaseTime) {
        return redissonService.tryLock(lockKey, leaseTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean tryLock(String lockKey, long waitTime, long leaseTime) {
        return redissonService.tryLock(lockKey, waitTime, leaseTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public void unLock(String lockKey) {
        redissonService.unLock(lockKey);
    }

}
