package com.example.demo.api;


import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestApi {

    //参数使用LCAP_EXTENSION_LOGGER后日志会显示在平台日志功能中
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");


    /**
     * 暂停线程
     *
     * @param millisecond 毫秒
     */
    @NaslLogic
    public static String threadSleep(Integer millisecond) {
        try {
            Thread.sleep(millisecond);
            return "success sleep" + millisecond + "ms";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "fail";
        }
    }


    /**
     * 等待线程
     *
     * @param obj
     * @return
     */
    @NaslLogic
    public static Boolean wait(Boolean obj) {
        if (obj == null) {
            obj = true;
        }
        synchronized (obj) {
            try {
                obj.wait();
                return obj;
            } catch (InterruptedException e) {
                return null;
            }
        }
    }

    /**
     * notify 唤醒线程
     *
     * @param obj
     * @return
     */
    @NaslLogic
    public static Boolean notify(Boolean obj) {
        if (obj == null) {
            obj = true;
        }
        synchronized (obj) {
            try {
                obj.notify();
            } catch (IllegalMonitorStateException e) {
                e.printStackTrace();
            }
            return obj;
        }
    }

    /**
     * notifyAll 唤醒线程
     *
     * @param obj
     * @return
     */
    @NaslLogic
    public static Boolean notifyAll(Boolean obj) {
        if (obj == null) {
            obj = true;
        }
        synchronized (obj) {
            try {
                obj.notifyAll();
            } catch (IllegalMonitorStateException e) {
                e.printStackTrace();
            }
            return obj;
        }
    }
}
