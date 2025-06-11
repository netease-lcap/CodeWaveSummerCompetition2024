package com.netease.lowcode.lib.util;

public class ProcessUserUtil {

    public static com.netease.lowcode.lib.logic.structure.ProcessUser toProcessUserFront(com.netease.codewave.process.open.domain.structure.ProcessUser processUser) {
        if (processUser == null) {
            return null;
        }
        com.netease.lowcode.lib.logic.structure.ProcessUser processUserFront = new com.netease.lowcode.lib.logic.structure.ProcessUser();
        processUserFront.setUserName(processUser.getUserName());
        processUserFront.setDisplayName(processUser.getDisplayName());
        return processUserFront;
    }
}
