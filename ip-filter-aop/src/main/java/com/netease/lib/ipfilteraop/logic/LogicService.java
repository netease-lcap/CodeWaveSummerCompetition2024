package com.netease.lib.ipfilteraop.logic;

import com.netease.lowcode.core.annotation.NaslLogic;

public class LogicService {
    /**
     * 获取用户ID
     *
     * @return
     */
    @NaslLogic(override = true)
    public static String getUserId() {
        //IDE 可复写其逻辑
        return null;
    }

}
