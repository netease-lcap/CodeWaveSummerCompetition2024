package com.netease.lowcode.lib.logic.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * 我的已办任务
 */
@NaslStructure
public class MyCompletedTask {
    /**
     * 任务ID
     */
    public String taskId;
    /**
     * 任务标题
     */
    public String taskTitle;
    /**
     * 节点标题
     */
    public String nodeTitle;
    /**
     * 流程实例标题
     */
    public String procInstTitle;
    /**
     * 流程定义标题
     */
    public String procDefTitle;
    /**
     * 流程实例发起人
     */
    public ProcessUser procInstInitiator;
    /**
     * 流程实例启动时间
     */
    public ZonedDateTime procInstStartTime;
    /**
     * 流程实例所处当前节点
     */
    public List<CurrNode> procInstCurrNodes;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcInstTitle() {
        return procInstTitle;
    }

    public void setProcInstTitle(String procInstTitle) {
        this.procInstTitle = procInstTitle;
    }

    public String getProcDefTitle() {
        return procDefTitle;
    }

    public void setProcDefTitle(String procDefTitle) {
        this.procDefTitle = procDefTitle;
    }

    public List<CurrNode> getProcInstCurrNodes() {
        return procInstCurrNodes;
    }

    public void setProcInstCurrNodes(List<CurrNode> procInstCurrNodes) {
        this.procInstCurrNodes = procInstCurrNodes;
    }

    public ZonedDateTime getProcInstStartTime() {
        return procInstStartTime;
    }

    public void setProcInstStartTime(ZonedDateTime procInstStartTime) {
        this.procInstStartTime = procInstStartTime;
    }

    public ProcessUser getProcInstInitiator() {
        return procInstInitiator;
    }

    public void setProcInstInitiator(ProcessUser procInstInitiator) {
        this.procInstInitiator = procInstInitiator;
    }


    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getNodeTitle() {
        return nodeTitle;
    }

    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }
}
