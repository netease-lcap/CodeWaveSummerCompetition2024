package com.netease.lowcode.lib.logic.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * 流程用户
 * @author zhuzaishao
 * @date 2024/12/19 10:35
 */
@NaslStructure
public class ProcessUser {
    /**
     * 用户名
     */
    public String userName;
    /**
     * 显示名称
     */
    public String displayName;

    public ProcessUser() {
    }

    public ProcessUser(String userName, String displayName) {
        this.userName = userName;
        this.displayName = displayName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
