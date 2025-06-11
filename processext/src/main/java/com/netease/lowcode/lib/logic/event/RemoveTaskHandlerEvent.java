package com.netease.lowcode.lib.logic.event;

import com.netease.codewave.process.api.domain.event.TaskOperationEvent;

import java.util.List;

/**
 * 删除任务审批人事件
 */
public class RemoveTaskHandlerEvent extends TaskOperationEvent {

    /**
     * 被删除的用户
     */
    private List<String> removedCandidateUsers;

    public RemoveTaskHandlerEvent() {
        super("RemoveTaskHandlerEvent");
    }

    public List<String> getRemovedCandidateUsers() {
        return removedCandidateUsers;
    }

    public void setRemovedCandidateUsers(List<String> removedCandidateUsers) {
        this.removedCandidateUsers = removedCandidateUsers;
    }
}
