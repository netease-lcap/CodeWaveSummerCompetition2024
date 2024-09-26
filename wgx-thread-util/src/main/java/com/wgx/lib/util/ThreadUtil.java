package com.wgx.lib.util;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtil {

    private static final Logger log = LoggerFactory.getLogger(ThreadUtil.class);

    /**
     * 暂停线程
     *
     * @param millisecond 毫秒
     * @return 操作结果布尔值（是否暂停成功后）
     */
    @NaslLogic
    public static Boolean threadSleep(Long millisecond) {
        if (millisecond == null || millisecond <= 0) {
            log.error("无效的毫秒数值，无法使线程进入休眠状态: {}", millisecond);
            return false;
        }
        try {
            Thread.sleep(millisecond);
            log.info("线程休眠 {} 毫秒", millisecond);
            return true;
        } catch (InterruptedException e) {
            log.error("线程休眠被中断", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 等待线程
     *
     * @param lock 锁对象
     * @return 是否成功唤醒
     */
    @NaslLogic
    public static Boolean waitThread(String lock) {
        if (lock == null) {
            log.error("锁对象不能为空");
            return false;
        }
        synchronized (lock.intern()) {
            try {
                lock.intern().wait();
                log.info("线程成功唤醒");
                return true;
            } catch (InterruptedException e) {
                log.error("等待线程被中断", e);
                Thread.currentThread().interrupt();
                return false;
            }
        }
    }

    /**
     * 带超时的等待线程（避免死锁）
     *
     * @param lock     锁对象
     * @param timeout  超时时间
     * @return 是否成功唤醒
     */
    @NaslLogic
    public static Boolean waitThreadWithTimeout(String lock, Long timeout) {
        if (lock == null) {
            log.error("锁对象不能为空");
            return false;
        }
        if (timeout <= 0) {
            log.error("超时时间必须大于0");
            return false;
        }
        synchronized (lock.intern()) {
            try {
                lock.intern().wait(timeout);
                log.info("线程在超时前被成功唤醒");
                return true;
            } catch (InterruptedException e) {
                log.error("带超时的等待线程被中断", e);
                Thread.currentThread().interrupt();
                return false;
            }
        }
    }

    /**
     * 唤醒等待的线程
     *
     * @param lock 锁对象
     * @return 是否成功唤醒
     */
    @NaslLogic
    public static Boolean notifyThread(String lock) {
        if (lock == null) {
            log.error("锁对象不能为空");
            return false;
        }
        synchronized (lock.intern()) {
            try {
                lock.intern().notify();
                log.info("唤醒了正在等待的线程");
                return true;
            } catch (IllegalMonitorStateException e) {
                log.error("唤醒线程时监控状态不合法", e);
                return false;
            }
        }
    }

    /**
     * 唤醒所有等待的线程
     *
     * @param lock 锁对象
     * @return 是否成功唤醒
     */
    @NaslLogic
    public static Boolean notifyAllThreads(String lock) {
        if (lock == null) {
            log.error("锁对象不能为空");
            return false;
        }
        synchronized (lock.intern()) {
            try {
                lock.intern().notifyAll();
                log.info("唤醒了所有正在等待的线程");
                return true;
            } catch (IllegalMonitorStateException e) {
                log.error("唤醒所有线程时监控状态不合法", e);
                return false;
            }
        }
    }

}