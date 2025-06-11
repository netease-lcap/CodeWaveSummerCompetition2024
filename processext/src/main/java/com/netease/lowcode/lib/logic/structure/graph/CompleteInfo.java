package com.netease.lowcode.lib.logic.structure.graph;

import com.netease.lowcode.core.annotation.NaslStructure;
import com.netease.lowcode.lib.logic.structure.ProcessUser;

import java.util.List;

/**
 * @author zhuzaishao
 * @date 2024/12/3 15:39
 * @description
 */
@NaslStructure
public class CompleteInfo {
    public ProcessUser assignee;
    public String completeTime;
    public Boolean completed;
    public List<ProcessUser> candidates;
    public String addSignTag;

    public ProcessUser getAssignee() {
        return assignee;
    }

    public void setAssignee(ProcessUser assignee) {
        this.assignee = assignee;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public List<ProcessUser> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<ProcessUser> candidates) {
        this.candidates = candidates;
    }

    public String getAddSignTag() {
        return addSignTag;
    }

    public void setAddSignTag(String addSignTag) {
        this.addSignTag = addSignTag;
    }
}
