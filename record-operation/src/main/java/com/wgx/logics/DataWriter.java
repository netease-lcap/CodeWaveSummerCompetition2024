package com.wgx.logics;

import com.netease.lowcode.core.annotation.NaslLogic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DataWriter {

    /**
     * 用户覆写，不要修改方法名和参数列表
     *
     * @param log
     * @return
     */
    @NaslLogic(override = true,enhance = false)
    public static Boolean saveLog(String log) {

        return true;
    }

    public static void invoke(Object service,Method saveLog,String log) throws InvocationTargetException, IllegalAccessException {
        if(saveLog == null){
            saveLog(log);
            return;
        }
        saveLog.invoke(service,log);
    }

}
