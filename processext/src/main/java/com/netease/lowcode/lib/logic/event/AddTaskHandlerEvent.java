package com.netease.lowcode.lib.logic.event;

import com.netease.codewave.process.api.domain.event.TaskOperationEvent;

import java.util.List;

/**
 * 新增任务审批人事件
 */
public class AddTaskHandlerEvent extends TaskOperationEvent {

    /**
     * 被新增的用户
     */
    private List<String> addedCandidateUsers;

    public AddTaskHandlerEvent() {
        super("AddTaskHandlerEvent");
    }

    public List<String> getAddedCandidateUsers() {
        return addedCandidateUsers;
    }

    public void setAddedCandidateUsers(List<String> addedCandidateUsers) {
        this.addedCandidateUsers = addedCandidateUsers;
    }
}
