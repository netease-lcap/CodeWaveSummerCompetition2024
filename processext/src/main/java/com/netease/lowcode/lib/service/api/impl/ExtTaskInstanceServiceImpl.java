package com.netease.lowcode.lib.service.api.impl;

import com.netease.codewave.nasl.java.definition.error.ProcessError;
import com.netease.codewave.process.api.domain.CWHistoricTaskInstance;
import com.netease.codewave.process.api.service.ProcessInstanceService;
import com.netease.codewave.process.api.service.TaskInstanceService;
import com.netease.codewave.process.api.service.event.ProcessEventListenerManagerService;
import com.netease.codewave.process.service.ProcessVariableService;
import com.netease.codewave.process.util.ProcessConstant;
import com.netease.lowcode.lib.logic.event.AddTaskHandlerEvent;
import com.netease.lowcode.lib.logic.event.RemoveTaskHandlerEvent;
import com.netease.lowcode.lib.logic.event.UpdateTaskHandlerEvent;
import com.netease.lowcode.lib.service.api.ExtTaskInstanceService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExtTaskInstanceServiceImpl implements ExtTaskInstanceService {
    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Resource
    private TaskInstanceService taskInstanceService;
    @Resource
    private ProcessInstanceService processInstanceService;
    @Resource
    private TaskService taskService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private ProcessVariableService processVariableService;
    @Resource
    private ProcessEventListenerManagerService processEventListenerManagerService;
    @Resource
    private HistoryService historyService;

    @Override
    public void addTaskHandler(String operator, List<String> addedCandidateUsers, String comment, CWHistoricTaskInstance taskInstance) {
        Authentication.setAuthenticatedUserId(operator);
        String taskId = taskInstance.getTaskId();
        Map<String, Object> processSystemVariables = processInstanceService.getProcessSystemVariablesById(taskInstance.getProcessInstanceId());
        Map<String, Object> nodeVar = (Map<String, Object>) processSystemVariables.getOrDefault(taskInstance.getNodeName(), new HashMap<>());
        List<String> nodeParticipants = (List<String>) nodeVar.get("nodeParticipants");
        // 判断当前节点的参与者是否已包含addedCandidateUsers
        StringBuilder addedCandidateUserString = new StringBuilder();
        addedCandidateUsers.forEach(addedCandidateUser -> {
            if (nodeParticipants.contains(addedCandidateUser)) {
                addedCandidateUserString.append(addedCandidateUser).append(",");
            }
        });
        if (StringUtils.isNoneBlank(addedCandidateUserString)) {
            logger.warn("The current node's participants has included '" + addedCandidateUserString + "', and the addTaskHandler operation is not allowed.");
            throw new ProcessError("The current node's participants has included '" + addedCandidateUserString + "', and the addTaskHandler operation is not allowed.");
        }
        // 将userForAddSign加入到nodeParticipants即可
        nodeParticipants.addAll(addedCandidateUsers);
        nodeVar.put("nodeParticipants", nodeParticipants);
        processSystemVariables.put(taskInstance.getNodeName(), nodeVar);
        processInstanceService.updateProcessSystemVariables(taskInstance.getProcessInstanceId(), processSystemVariables);

        String approvalPolicy = taskInstance.getApprovalPolicy();
        if (StringUtils.isBlank(approvalPolicy) || ProcessConstant.VALUE_TASK_APPROVAL_POLICY_SIMPLE.equals(approvalPolicy)) {
            addedCandidateUsers.forEach(user -> taskService.addCandidateUser(taskId, user));
        } else {
            if (ProcessConstant.VALUE_TASK_APPROVAL_POLICY_SEQUENCE.equals(approvalPolicy)) {
                addedCandidateUsers.forEach(user -> taskService.addCandidateUser(taskId, user));
            } else {
                //会签
                Execution currentExecution = runtimeService.createExecutionQuery().executionId(taskInstance.getExecutionId()).singleResult();
                //获取会签任务未结束的子执行
                List<Execution> childExecutions = runtimeService.createExecutionQuery().parentId(currentExecution.getParentId()).list();
                List<String> existTaskList = new ArrayList<>();
                childExecutions.forEach(childExecution -> {
                    HistoricTaskInstance taskInstanceSub = this.historyService.createHistoricTaskInstanceQuery().executionId(childExecution.getId()).singleResult();
                    if (taskInstanceSub != null && taskInstanceSub.getEndTime() == null) {
                        existTaskList.add(taskInstanceSub.getId());
                    }
                });
                if (existTaskList.size() == 1) {
                    // 若存在只有一个任务，并且没有候选人，首次先给当前活跃任务加上候选人
                    List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForTask(existTaskList.get(0));
                    if (identityLinks.size() == 0) {
                        String addedCandidateUser1 = addedCandidateUsers.get(0);
                        taskService.addCandidateUser(existTaskList.get(0), addedCandidateUser1);
                        addedCandidateUsers.remove(addedCandidateUser1);
                    }
                }
                if (!CollectionUtils.isEmpty(addedCandidateUsers)) {
                    // 若存在多个任务，则直接加任务
                    addedCandidateUsers.forEach(user -> runtimeService.addMultiInstanceExecution(taskInstance.getNodeName(), taskInstance.getProcessInstanceId(), Collections.singletonMap("assignee", user)));
                }
            }
        }

        // 此处发送加签事件
        triggerEventForAddTaskHandler(taskInstance, processSystemVariables, operator, addedCandidateUsers, comment);

        Authentication.setAuthenticatedUserId(null);
    }

    @Override
    public void removeTaskHandler(String operator, List<String> removedCandidateUsers, String comment, CWHistoricTaskInstance taskInstance) {
        Authentication.setAuthenticatedUserId(operator);
        String taskId = taskInstance.getTaskId();
        Map<String, Object> processSystemVariables = processInstanceService.getProcessSystemVariablesById(taskInstance.getProcessInstanceId());
        Map<String, Object> nodeVar = (Map<String, Object>) processSystemVariables.getOrDefault(taskInstance.getNodeName(), new HashMap<>());
        List<String> nodeParticipants = (List<String>) nodeVar.get("nodeParticipants");
        if (!nodeParticipants.containsAll(removedCandidateUsers)) {
            //  当前节点的参与者不包含被删除的候选用户
            removedCandidateUsers.removeAll(nodeParticipants);
            StringBuilder removedCandidateUserString = new StringBuilder();
            removedCandidateUsers.forEach(user -> removedCandidateUserString.append(user).append(","));
            logger.warn("The current node's participants do not include '" + removedCandidateUserString + "', and the removeTaskHandler operation is not allowed.");
            throw new ProcessError("The current node's participants do not include '" + removedCandidateUserString + "', and the removeTaskHandler operation is not allowed.");
        }
        nodeParticipants.removeAll(removedCandidateUsers);
        nodeVar.put("nodeParticipants", nodeParticipants);
        processSystemVariables.put(taskInstance.getNodeName(), nodeVar);
        String approvalPolicy = taskInstance.getApprovalPolicy();
        if (StringUtils.isBlank(approvalPolicy) || ProcessConstant.VALUE_TASK_APPROVAL_POLICY_SIMPLE.equals(approvalPolicy)) {
            processInstanceService.updateProcessSystemVariables(taskInstance.getProcessInstanceId(), processSystemVariables);
            removedCandidateUsers.forEach(user -> taskService.deleteCandidateUser(taskId, user));
        } else {
            if (ProcessConstant.VALUE_TASK_APPROVAL_POLICY_SEQUENCE.equals(approvalPolicy)) {
                processInstanceService.updateProcessSystemVariables(taskInstance.getProcessInstanceId(), processSystemVariables);
                removedCandidateUsers.forEach(user -> taskService.deleteCandidateUser(taskId, user));
            } else {
                Execution currentExecution = runtimeService.createExecutionQuery().executionId(taskInstance.getExecutionId()).singleResult();
                //获取会签所有子执行
                List<Execution> childExecutions = runtimeService.createExecutionQuery().parentId(currentExecution.getParentId()).list();
                StringBuilder completedRemovedCandidateUserString = new StringBuilder();
                Map<String, String> runningExecutionCandidateUserMap = new HashMap<>();
                childExecutions.forEach(childExecution -> {
                    //会签任务和候选人匹配
                    HistoricTaskInstance taskInstanceSub = this.historyService.createHistoricTaskInstanceQuery().executionId(childExecution.getId()).singleResult();
                    if (taskInstanceSub == null) {
                        return;
                    }
                    List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForTask(taskInstanceSub.getId());
                    List<String> subTaskCandidateUsers = identityLinks.stream().filter(identityLink -> "candidate".equals(identityLink.getType()))
                            .map(HistoricIdentityLink::getUserId).filter(Objects::nonNull).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(identityLinks) || CollectionUtils.isEmpty(subTaskCandidateUsers)) {
                        logger.error("custom system error, subTaskCandidateUsers is empty");
                        throw new ProcessError("custom system error, subTaskCandidateUsers is empty");
                    }
                    //会签一个任务只会有一个候选人
                    String subTaskCandidateUser = subTaskCandidateUsers.get(0);
                    if (taskInstanceSub.getEndTime() != null) {
                        if (removedCandidateUsers.contains(subTaskCandidateUser)) {
                            completedRemovedCandidateUserString.append(subTaskCandidateUser).append(",");
                        }
                    }
                    if (taskInstanceSub.getEndTime() == null) {
                        runningExecutionCandidateUserMap.put(subTaskCandidateUser, childExecution.getId());
                    }
                });
                //已完成任务不允许删除
                if (StringUtils.isNoneBlank(completedRemovedCandidateUserString)) {
                    logger.warn(completedRemovedCandidateUserString + " task has ended and removeTaskHandler operations are prohibited!");
                    throw new ProcessError(completedRemovedCandidateUserString + " task has ended and removeTaskHandler operations are prohibited!");
                }
                processInstanceService.updateProcessSystemVariables(taskInstance.getProcessInstanceId(), processSystemVariables);
                //未完成的所有子任务候选人
                Set<String> runningSubTaskCandidateUserSet = runningExecutionCandidateUserMap.keySet();
                //不允许删空
                //删除最后一个时，仅删除任务的候选人，不删除任务
                if (runningSubTaskCandidateUserSet.size() == removedCandidateUsers.size()) {
                    String runningSubTaskCandidateUserFinal = runningSubTaskCandidateUserSet.stream().findFirst().get();
                    String taskIdFinal = historyService.createHistoricTaskInstanceQuery().executionId(runningExecutionCandidateUserMap.get(runningSubTaskCandidateUserFinal)).singleResult().getId();
                    taskService.deleteCandidateUser(taskIdFinal, runningSubTaskCandidateUserFinal);
                    runningExecutionCandidateUserMap.remove(runningSubTaskCandidateUserFinal);
                }
                if (!CollectionUtils.isEmpty(runningExecutionCandidateUserMap)) {
                    // 若存在多个任务，则直接删任务
                    removedCandidateUsers.forEach(user -> runtimeService.deleteMultiInstanceExecution(runningExecutionCandidateUserMap.get(user), false));
                }
            }
        }

        // 此处发送加签事件
        triggerEventForRemoveTaskHandler(taskInstance, processSystemVariables, operator, removedCandidateUsers, comment);

        Authentication.setAuthenticatedUserId(null);

    }

    @Override
    public void updateCandidateUsers(List<String> updateCandidateUsers, String operator, String comment, CWHistoricTaskInstance taskInstance) {
        Authentication.setAuthenticatedUserId(operator);

        String taskId = taskInstance.getTaskId();
        List<String> currentCandidateUsers = taskInstance.getCandidateUsers();
        // 修改流程数据
        Map<String, Object> processSystemVariables = processInstanceService.getProcessSystemVariablesById(taskInstance.getProcessInstanceId());
        Map<String, Object> nodeVar = (Map<String, Object>) processSystemVariables.getOrDefault(taskInstance.getNodeName(), new HashMap<>());
        nodeVar.put("nodeParticipants", updateCandidateUsers);
        processSystemVariables.put(taskInstance.getNodeName(), nodeVar);
        processInstanceService.updateProcessSystemVariables(taskInstance.getProcessInstanceId(), processSystemVariables);

        // 删除旧的candidateUsers, 添加新的candidateUsers
        currentCandidateUsers.forEach(user -> taskService.deleteCandidateUser(taskId, user));
        updateCandidateUsers.forEach(user -> taskService.addCandidateUser(taskId, user));
        triggerEventForUpdateTaskHandler(taskInstance, processSystemVariables, operator, updateCandidateUsers, comment);

        Authentication.setAuthenticatedUserId(null);
        logger.info("The task {} is updated candidate users {}", taskId, updateCandidateUsers);
    }

    public void triggerEventForRemoveTaskHandler(CWHistoricTaskInstance taskInstance, Map<String, Object> systemVariable, String operator, List<String> addedCandidateUsers, String comment) {
        if (ObjectUtils.isEmpty(systemVariable)) {
            return;
        }

        RemoveTaskHandlerEvent event = new RemoveTaskHandlerEvent();

        event.setProcInstId(taskInstance.getProcessInstanceId());
        event.setProcDefId(taskInstance.getProcessDefinitionId());
        event.setProcDefKey(taskInstance.getProcessDefinitionId().split(":")[0]);

        event.setProcInstStartBy((String) systemVariable.get("procInstStartBy"));
        event.setProcInstStartTime((ZonedDateTime) systemVariable.get("procInstStartTime"));

        Object currentNodeVariable = systemVariable.get(taskInstance.getNodeName());

        if (currentNodeVariable != null) {
            Map<String, Object> currentNodeVariableMap = (Map<String, Object>) currentNodeVariable;
            event.setCurrNodeName(taskInstance.getNodeName());
            event.setCurrNodeTitle(taskInstance.getNodeTitle());
            event.setCurrNodeDesc(taskInstance.getNodeDescription());
            event.setCurrNodeParticipants((List<String>) currentNodeVariableMap.get("nodeParticipants"));
            event.setCurrNodeStartTime((ZonedDateTime) currentNodeVariableMap.get("nodeStartTime"));
            event.setCurrTaskId(taskInstance.getTaskId());
            event.setCurrTaskStartTime(taskInstance.getCreateTime());
        }

        event.setOperateUser(operator);
        event.setOperateComment(comment);
        event.setRemovedCandidateUsers(addedCandidateUsers);
        event.setPre(processVariableService.getPreviousNodeVariable(taskInstance.getProcessInstanceId(), taskInstance.getNodeName()));

        processEventListenerManagerService.triggerEvent(event);
    }

    public void triggerEventForAddTaskHandler(CWHistoricTaskInstance taskInstance, Map<String, Object> systemVariable, String operator, List<String> addedCandidateUsers, String comment) {
        if (ObjectUtils.isEmpty(systemVariable)) {
            return;
        }

        AddTaskHandlerEvent event = new AddTaskHandlerEvent();

        event.setProcInstId(taskInstance.getProcessInstanceId());
        event.setProcDefId(taskInstance.getProcessDefinitionId());
        event.setProcDefKey(taskInstance.getProcessDefinitionId().split(":")[0]);

        event.setProcInstStartBy((String) systemVariable.get("procInstStartBy"));
        event.setProcInstStartTime((ZonedDateTime) systemVariable.get("procInstStartTime"));

        Object currentNodeVariable = systemVariable.get(taskInstance.getNodeName());

        if (currentNodeVariable != null) {
            Map<String, Object> currentNodeVariableMap = (Map<String, Object>) currentNodeVariable;
            event.setCurrNodeName(taskInstance.getNodeName());
            event.setCurrNodeTitle(taskInstance.getNodeTitle());
            event.setCurrNodeDesc(taskInstance.getNodeDescription());
            event.setCurrNodeParticipants((List<String>) currentNodeVariableMap.get("nodeParticipants"));
            event.setCurrNodeStartTime((ZonedDateTime) currentNodeVariableMap.get("nodeStartTime"));
            event.setCurrTaskId(taskInstance.getTaskId());
            event.setCurrTaskStartTime(taskInstance.getCreateTime());
        }

        event.setOperateUser(operator);
        event.setOperateComment(comment);
        event.setAddedCandidateUsers(addedCandidateUsers);
        event.setPre(processVariableService.getPreviousNodeVariable(taskInstance.getProcessInstanceId(), taskInstance.getNodeName()));

        processEventListenerManagerService.triggerEvent(event);
    }

    public void triggerEventForUpdateTaskHandler(CWHistoricTaskInstance taskInstance, Map<String, Object> systemVariable, String operator, List<String> updatedCandidateUsers, String comment) {
        if (ObjectUtils.isEmpty(systemVariable)) {
            return;
        }

        UpdateTaskHandlerEvent event = new UpdateTaskHandlerEvent();

        event.setProcInstId(taskInstance.getProcessInstanceId());
        event.setProcDefId(taskInstance.getProcessDefinitionId());
        event.setProcDefKey(taskInstance.getProcessDefinitionId().split(":")[0]);

        event.setProcInstStartBy((String) systemVariable.get("procInstStartBy"));
        event.setProcInstStartTime((ZonedDateTime) systemVariable.get("procInstStartTime"));

        Object currentNodeVariable = systemVariable.get(taskInstance.getNodeName());

        if (currentNodeVariable != null) {
            Map<String, Object> currentNodeVariableMap = (Map<String, Object>) currentNodeVariable;
            event.setCurrNodeName(taskInstance.getNodeName());
            event.setCurrNodeTitle(taskInstance.getNodeTitle());
            event.setCurrNodeDesc(taskInstance.getNodeDescription());
            event.setCurrNodeParticipants((List<String>) currentNodeVariableMap.get("nodeParticipants"));
            event.setCurrNodeStartTime((ZonedDateTime) currentNodeVariableMap.get("nodeStartTime"));
            event.setCurrTaskId(taskInstance.getTaskId());
            event.setCurrTaskStartTime(taskInstance.getCreateTime());
        }

        event.setOperateUser(operator);
        event.setOperateComment(comment);
        event.setUpdatedCandidateUsers(updatedCandidateUsers);
        event.setPre(processVariableService.getPreviousNodeVariable(taskInstance.getProcessInstanceId(), taskInstance.getNodeName()));

        processEventListenerManagerService.triggerEvent(event);
    }
}
