package com.netease.lowcode.lib.process.domain;

public enum ExtProcessRecordAction {
    LAUNCH("launch", "启动"),
    APPROVE("approve", "同意"),
    REJECT("reject", "拒绝"),
    SUBMIT("submit", "提交"),
    REASSIGN("reassign", "转派"),
    REVERT("revert", "回退"),
    WITHDRAW("withdraw", "撤回"),
    TERMINATE("terminate", "终止"),
    ADDSIGN("addSign", "加签"),
    CC("cc", "抄送"),
    END("end", "结束"),
    UPDATE_TASK_HANDLER("updateTaskHandler", "修改任务审批人"),
    ADD_TASK_HANDLER("addTaskHandler", "添加任务审批人"),
    REMOVE_TASK_HANDLER("removeTaskHandler", "移除任务审批人"),
    END_PROCESS("endProcess", "结束流程"),
    ;

    private String value;
    private String desc;

    ExtProcessRecordAction(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String value() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
