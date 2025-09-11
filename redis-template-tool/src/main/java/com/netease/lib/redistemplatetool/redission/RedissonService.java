package com.netease.lib.redistemplatetool.redission;

import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Configuration("libraryRedissonService")
public class RedissonService {

    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Resource
    private RedissonClient redissonClient;

    /**
     * 立即尝试获取锁，类似 ReentrantLock.tryLock()
     */
    public boolean tryLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean acquired = lock.tryLock();
            if (acquired) {
                log.info("tryLock success, key:{}", lockKey);
            } else {
                log.debug("ryLock failed, key:{}", lockKey);
            }
            return acquired;
        } catch (Exception e) {
            log.error("tryLock error, key:{}", lockKey, e);
            return false;
        }
    }

    /**
     * 带超时的tryLock，一次调用搞定
     */
    public boolean tryLock(String lockKey, long timeout, TimeUnit unit) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean acquired = lock.tryLock(timeout, unit);
            if (acquired) {
                log.info("tryLock success with timeout, key:{}, timeout:{}{}",
                        lockKey, timeout, unit.name().toLowerCase());
            } else {
                log.debug("tryLock timeout, key:{}, timeout:{}{}",
                        lockKey, timeout, unit.name().toLowerCase());
            }
            return acquired;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("tryLock interrupted, key:{}", lockKey);
            return false;
        } catch (Exception e) {
            log.error("tryLock with timeout error, key:{}", lockKey, e);
            return false;
        }
    }

    /**
     * 带超时的tryLock
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean acquired = lock.tryLock(waitTime, leaseTime, unit);
            if (acquired) {
                log.info("tryLock success, key:{}, waitTime:{}{}, leaseTime:{}{}",
                        lockKey, waitTime, unit.name().toLowerCase(),
                        leaseTime, unit.name().toLowerCase());
            } else {
                log.debug("tryLock failed, key:{}, waitTime:{}{}",
                        lockKey, waitTime, unit.name().toLowerCase());
            }
            return acquired;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("tryLock interrupted, key:{}", lockKey);
            return false;
        } catch (Exception e) {
            log.error("tryLock error, key:{}", lockKey, e);
            return false;
        }
    }

    /**
     * 手动释放锁
     */
    public void unLock(String lockKey) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            lock.unlock();
            log.info("Manual unlock success, key:{}", lockKey);
        } catch (IllegalMonitorStateException e) {
            // unlock() 内部检查发现锁不属于当前线程
            log.warn("Lock not held by current thread, key:{}", lockKey);
        } catch (Exception e) {
            log.error("Failed to unlock, key:{}, error:{}", lockKey, e.getMessage(), e);
        }
    }


}
