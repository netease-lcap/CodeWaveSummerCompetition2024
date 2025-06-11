package com.netease.lowcode.lib.logic.event;

import com.netease.codewave.process.api.domain.event.TaskOperationEvent;

import java.util.List;

/**
 * 修改任务审批人事件
 */
public class UpdateTaskHandlerEvent extends TaskOperationEvent {

    /**
     * 被修改的用户
     */
    private List<String> updatedCandidateUsers;

    public UpdateTaskHandlerEvent() {
        super("UpdateTaskHandlerEvent");
    }

    public List<String> getUpdatedCandidateUsers() {
        return updatedCandidateUsers;
    }

    public void setUpdatedCandidateUsers(List<String> updatedCandidateUsers) {
        this.updatedCandidateUsers = updatedCandidateUsers;
    }
}
