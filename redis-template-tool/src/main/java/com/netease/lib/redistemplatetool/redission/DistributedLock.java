package com.netease.lib.redistemplatetool.redission;

public interface DistributedLock {

    /**
     * 加锁,获取失败后返回false
     */
    boolean tryLock(String lockKey);

    /**
     * 加锁,获取失败后返回null
     */
    boolean tryLock(String lockKey, long leaseTime);

    /**
     * 加锁，获取失败后等待timeout
     * @param lockKey key
     * @param waitTime 锁等待时间
     * @param leaseTime 锁超时时间
     */
    boolean tryLock(String lockKey, long waitTime, long leaseTime);


    void unLock(String lockKey);


}
