package com.netease.lowcode.lib.logic.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

/**
 * 当前节点
 */
@NaslStructure
public class CurrNode {
    /**
     * 当前节点标题
     */
    public String currNodeTitle;
    /**
     * 当前节点参与者
     */
    public List<ProcessUser> currNodeParticipants;
    /**
     * 任务ID
     */
    public String taskId;

    public CurrNode() {
    }

    public CurrNode(String currNodeTitle, List<ProcessUser> currNodeParticipants) {
        this.currNodeTitle = currNodeTitle;
        this.currNodeParticipants = currNodeParticipants;
    }


    public CurrNode(String currNodeTitle, List<ProcessUser> currNodeParticipants, String taskId) {
        this.currNodeTitle = currNodeTitle;
        this.currNodeParticipants = currNodeParticipants;
        this.taskId = taskId;
    }

    public String getCurrNodeTitle() {
        return currNodeTitle;
    }

    public void setCurrNodeTitle(String currNodeTitle) {
        this.currNodeTitle = currNodeTitle;
    }

    public List<ProcessUser> getCurrNodeParticipants() {
        return currNodeParticipants;
    }

    public void setCurrNodeParticipants(List<ProcessUser> currNodeParticipants) {
        this.currNodeParticipants = currNodeParticipants;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
