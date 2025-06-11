package com.netease.lowcode.lib.logic.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * 任务实例详情
 */
@NaslStructure
public class TaskInst {
    /**
     * 任务ID
     */
    public String taskId;
    /**
     * 节点标题
     */
    public String title;
    /**
     * 节点描述
     */
    public String description;
    /**
     * 任务标题
     */
    public String taskTitle;
    /**
     * 任务描述
     */
    public String taskDescription;
    /**
     * 是否完成
     */
    public Boolean finished;
    /**
     * 任务完成用户
     */
    public ProcessUser completeBy;
    /**
     * 任务创建时间
     */
    public ZonedDateTime createTime;
    /**
     * 任务完成时间
     */
    public ZonedDateTime completeTime;
    /**
     * 任务定义名称
     */
    public String taskDefName;
    /**
     * 分类，任务类型
     */
    public String category;
    /**
     * 流程实例ID
     */
    public String processInstanceId;
    /**
     * 流程定义唯一标识
     */
    public String processDefUniqueKey;
    /**
     * 流程定义ID
     */
    public String processDefinitionId;
    /**
     * 候选用户
     */
    public List<ProcessUser> candidateUsers;
    /**
     * 审批结果
     */
    public Boolean approvalResult;
    /**
     * 审批意见
     */
    public String approvalComment;
    /**
     * 流程定义名称
     */
    public String processDefName;
    /**
     * 流程实例状态
     */
    public String procInstStatus;
    /**
     * 任务审批策略，或签、会签以及依次审批
     */
    public String approvalPolicy;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public ProcessUser getCompleteBy() {
        return completeBy;
    }

    public void setCompleteBy(ProcessUser completeBy) {
        this.completeBy = completeBy;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(ZonedDateTime completeTime) {
        this.completeTime = completeTime;
    }

    public String getTaskDefName() {
        return taskDefName;
    }

    public void setTaskDefName(String taskDefName) {
        this.taskDefName = taskDefName;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDefUniqueKey() {
        return processDefUniqueKey;
    }

    public void setProcessDefUniqueKey(String processDefUniqueKey) {
        this.processDefUniqueKey = processDefUniqueKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public List<ProcessUser> getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(List<ProcessUser> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public Boolean getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(Boolean approvalResult) {
        this.approvalResult = approvalResult;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public void setApprovalComment(String approvalComment) {
        this.approvalComment = approvalComment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProcessDefName() {
        return processDefName;
    }

    public void setProcessDefName(String processDefName) {
        this.processDefName = processDefName;
    }

    public String getProcInstStatus() {
        return procInstStatus;
    }

    public void setProcInstStatus(String procInstStatus) {
        this.procInstStatus = procInstStatus;
    }

    public String getApprovalPolicy() {
        return approvalPolicy;
    }

    public void setApprovalPolicy(String approvalPolicy) {
        this.approvalPolicy = approvalPolicy;
    }
}
