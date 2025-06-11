package com.netease.lowcode.lib.logic.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * 流程实例详情
 */
@NaslStructure
public class ProcInstInfo {
    /**
     * 流程实例ID
     */
    public String procInstId;
    /**
     * 流程实例标题
     */
    public String procInstTitle;
    /**
     * 流程实例发起人
     */
    public ProcessUser procInstInitiator;
    /**
     * 流程实例启动时间
     */
    public ZonedDateTime procInstStartTime;
    /**
     * 流程实例结束时间
     */
    public ZonedDateTime procInstEndTime;
    /**
     * 流程实例状态
     */
    public String procInstStatus;
    /**
     * 流程实例当前节点
     */
    public List<CurrNode> procInstCurrNodes;
    /**
     * 流程定义名称
     */
    public String processDefinitionName;
    /**
     * 流程定义标题
     */
    public String processDefinitionTitle;

    // 此处应该是processDefinitionTitle

    public String getProcInstTitle() {
        return procInstTitle;
    }

    public void setProcInstTitle(String procInstTitle) {
        this.procInstTitle = procInstTitle;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public ProcessUser getProcInstInitiator() {
        return procInstInitiator;
    }

    public void setProcInstInitiator(ProcessUser procInstInitiator) {
        this.procInstInitiator = procInstInitiator;
    }

    public ZonedDateTime getProcInstStartTime() {
        return procInstStartTime;
    }

    public void setProcInstStartTime(ZonedDateTime procInstStartTime) {
        this.procInstStartTime = procInstStartTime;
    }

    public ZonedDateTime getProcInstEndTime() {
        return procInstEndTime;
    }

    public void setProcInstEndTime(ZonedDateTime procInstEndTime) {
        this.procInstEndTime = procInstEndTime;
    }

    public String getProcInstStatus() {
        return procInstStatus;
    }

    public void setProcInstStatus(String procInstStatus) {
        this.procInstStatus = procInstStatus;
    }

    public List<CurrNode> getProcInstCurrNodes() {
        return procInstCurrNodes;
    }

    public void setProcInstCurrNodes(List<CurrNode> procInstCurrNodes) {
        this.procInstCurrNodes = procInstCurrNodes;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getProcessDefinitionTitle() {
        return processDefinitionTitle;
    }

    public void setProcessDefinitionTitle(String processDefinitionTitle) {
        this.processDefinitionTitle = processDefinitionTitle;
    }
}
