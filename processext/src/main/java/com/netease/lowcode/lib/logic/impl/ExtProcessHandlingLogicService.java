package com.netease.lowcode.lib.logic.impl;

import com.netease.codewave.nasl.java.definition.error.ProcessError;
import com.netease.codewave.process.api.domain.CWHistoricProcessInstance;
import com.netease.codewave.process.api.domain.CWHistoricTaskInstance;
import com.netease.codewave.process.api.service.ProcessInstanceService;
import com.netease.codewave.process.api.service.TaskInstanceService;
import com.netease.codewave.process.service.ProcessUserIdentityService;
import com.netease.codewave.process.util.ProcessConstant;
import com.netease.lowcode.lib.annotation.ReconfirmAuthorization;
import com.netease.lowcode.lib.enums.EndedProcInstStatusEnum;
import com.netease.lowcode.lib.exception.ProcessExtCommonException;
import com.netease.lowcode.lib.process.domain.ExtProcessRecordAction;
import com.netease.lowcode.lib.service.api.ExtProcessInstanceService;
import com.netease.lowcode.lib.service.api.ExtTaskInstanceService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExtProcessHandlingLogicService extends ExtProcessHandlingLogicBaseService {
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Resource
    private TaskInstanceService taskInstanceService;
    @Resource
    private ExtTaskInstanceService extTaskInstanceService;
    @Resource
    private ProcessInstanceService processInstanceService;
    @Resource
    private ProcessUserIdentityService processUserIdentityService;
    @Resource
    private ExtProcessInstanceService extProcessInstanceService;

    /**
     * 更新任务处理人
     *
     * @param taskId
     * @param updatedCandidateUsers
     * @return
     */
    @ReconfirmAuthorization
    public Boolean updateTaskHandler(String taskId, List<String> updatedCandidateUsers) {
        String currentUser = processUserIdentityService.getCurrentUserName();
        return updateTaskHandler(taskId, updatedCandidateUsers, currentUser, false);
    }

    /**
     * 更新任务处理人
     *
     * @param taskId
     * @param updatedCandidateUsers
     * @return
     */
    @ReconfirmAuthorization
    public Boolean updateTaskHandlerAdmin(String taskId, List<String> updatedCandidateUsers, String operator) {
        return updateTaskHandler(taskId, updatedCandidateUsers, operator, true);
    }

    /**
     * 基于已生成任务修改，不操作任务的增减
     *
     * @param taskId
     * @param updatedCandidateUsers
     * @param operator
     * @param isAdmin
     * @return
     */
    private Boolean updateTaskHandler(String taskId, List<String> updatedCandidateUsers, String operator, Boolean isAdmin) {
        if (StringUtils.isBlank(taskId)) {
            log.warn("The taskId cannot be empty!");
            throw new ProcessError("The taskId cannot be empty!");
        }

        if (!CollectionUtils.isEmpty(updatedCandidateUsers)) {
            updatedCandidateUsers = updatedCandidateUsers.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(updatedCandidateUsers)) {
            log.warn("updatedCandidateUsers can not be empty!");
            throw new ProcessExtCommonException("updatedCandidateUsers can not be empty!");
        }
        Optional<CWHistoricTaskInstance> optional = taskInstanceService.getHistoricTaskInstanceById(taskId);
        if (!optional.isPresent()) {
            log.warn("Task does not exist!");
            throw new ProcessExtCommonException("Task does not exist!");
        }

        CWHistoricTaskInstance taskInstance = optional.get();

        if (taskInstance.getCategory() != null && !taskInstance.getCategory().equals(ProcessConstant.TASK_CATEGORY_APPROVAL_TASK)) {
            log.warn("The type of task is not an approval task and updateTaskHandler operation is not supported.");
            throw new ProcessError("The type of task is not an approval task and updateTaskHandler operation is not supported.");
        }

        if (taskInstance.getEndTime() != null) {
            log.warn("The task has ended and updateTaskHandler operations are prohibited!");
            throw new ProcessError("The task has ended and updateTaskHandler operations are prohibited!");
        }

        if (!isAdmin) {
            // 判断经办人是否有权限操作该任务
            if (CollectionUtil.isEmpty(taskInstance.getCandidateUsers()) && StringUtils.isEmpty(taskInstance.getAssignee())) {
                log.warn("There is no person in charge of this task, and other people have no rights to operate it.");
                throw new ProcessExtCommonException("There is no person in charge of this task, and other people have no rights to operate it.");
            }
            if (!taskInstance.getCandidateUsers().contains(operator) && !StringUtils.equals(taskInstance.getAssignee(), operator)) {
                log.warn("This task does not belong to the operator and updateTaskHandler operation is not allowed.");
                throw new ProcessExtCommonException("This task does not belong to the operator and updateTaskHandler operation is not allowed.");
            }
        }
        StringBuilder userStr = new StringBuilder();
        updatedCandidateUsers.forEach(user -> userStr.append(user).append(","));
        String comment = "change to " + userStr;

        //添加审批记录，但是不在审批记录中展示
        addTaskRecord(taskInstance, operator, ExtProcessRecordAction.UPDATE_TASK_HANDLER.value(), null, comment, false, false);

        extTaskInstanceService.updateCandidateUsers(updatedCandidateUsers, operator, comment, taskInstance);

        return true;
    }

    /**
     * 新增候选处理人
     *
     * @param taskId              任务id
     * @param addedCandidateUsers 被添加的候选处理人
     * @return
     */
    @ReconfirmAuthorization
    public Boolean addTaskHandler(String taskId, List<String> addedCandidateUsers) {
        String currentUser = processUserIdentityService.getCurrentUserName();
        return addTaskHandler(taskId, addedCandidateUsers, currentUser, false);
    }

    /**
     * 新增候选处理人
     *
     * @param taskId              任务id
     * @param addedCandidateUsers 被添加的候选处理人
     * @param operator            操作人
     * @return
     */
    @ReconfirmAuthorization
    public Boolean addTaskHandlerAdmin(String taskId, List<String> addedCandidateUsers, String operator) {
        return addTaskHandler(taskId, addedCandidateUsers, operator, true);
    }

    /**
     * 基于已生成任务修改，审批方式不变。不同的审批方式，操作任务增减原则不同：
     * 或签：多个候选人只会产生一个task。新增操作即对当前task新增候选人（候选人间为或的关系）
     * 会签：多个候选人会产生多个task。新增操作即生成新的task，新task关联新候选人（候选人间为与的关系）
     * 依次审批：多个候选人依次生成task。一个task对应一个候选人。新增操作即仅对当前task新增候选人（候选人间为或的关系）
     *
     * @param taskId
     * @param addedCandidateUsers
     * @param operator
     * @param isAdmin
     * @return
     */
    private Boolean addTaskHandler(String taskId, List<String> addedCandidateUsers, String operator, Boolean isAdmin) {
        if (StringUtils.isBlank(taskId)) {
            log.warn("The taskId cannot be empty!");
            throw new ProcessError("The taskId cannot be empty!");
        }

        if (!CollectionUtils.isEmpty(addedCandidateUsers)) {
            addedCandidateUsers = addedCandidateUsers.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(addedCandidateUsers)) {
            log.warn("addedCandidateUsers can not be empty!");
            throw new ProcessExtCommonException("addedCandidateUsers can not be empty!");
        }

        if (StringUtils.isBlank(operator)) {
            log.warn("The operator cannot be empty!");
            throw new ProcessError("The operator cannot be empty!");
        }

        Optional<CWHistoricTaskInstance> optional = taskInstanceService.getHistoricTaskInstanceById(taskId);
        if (!optional.isPresent()) {
            log.warn("Task is not exist!");
            throw new ProcessError("Task is not exist!");
        }

        CWHistoricTaskInstance taskInstance = optional.get();

        if (taskInstance.getCategory() != null && !taskInstance.getCategory().equals(ProcessConstant.TASK_CATEGORY_APPROVAL_TASK)) {
            log.warn("The type of task is not an approval task and addTaskHandler operation is not supported.");
            throw new ProcessError("The type of task is not an approval task and addTaskHandler operation is not supported.");
        }

        if (taskInstance.getEndTime() != null) {
            log.warn("The task has ended and addTaskHandler operations are prohibited!");
            throw new ProcessError("The task has ended and addTaskHandler operations are prohibited!");
        }
        if (!isAdmin) {
            // 判断经办人是否有权限操作该任务
            if (CollectionUtil.isEmpty(taskInstance.getCandidateUsers()) && StringUtils.isEmpty(taskInstance.getAssignee())) {
                log.warn("There is no person in charge of this task, and other people have no rights to operate it.");
                throw new ProcessError("There is no person in charge of this task, and other people have no rights to operate it.");
            }
            if (!taskInstance.getCandidateUsers().contains(operator) && !StringUtils.equals(taskInstance.getAssignee(), operator)) {
                log.warn("This task is not belong to the operator and addTaskHandler operation is not allowed.");
                throw new ProcessError("This task is not belong to the operator and addTaskHandler operation is not allowed.");
            }
        }

        StringBuilder userStr = new StringBuilder();
        addedCandidateUsers.forEach(user -> userStr.append(user).append(","));
        String comment = "handlers add : " + userStr;
        // 添加审批记录
        addTaskRecord(taskInstance, operator, ExtProcessRecordAction.ADD_TASK_HANDLER.value(), null, comment, false, false);

        extTaskInstanceService.addTaskHandler(operator, addedCandidateUsers, comment, taskInstance);

        return true;
    }

    /**
     * 删除候选处理人
     *
     * @param taskId                任务id
     * @param removedCandidateUsers 被添加的候选处理人
     * @param operator
     * @return
     */
    @ReconfirmAuthorization
    public Boolean removeTaskHandlerAdmin(String taskId, List<String> removedCandidateUsers, String operator) {
        return removeTaskHandler(taskId, removedCandidateUsers, operator, true);
    }

    /**
     * 结束流程实例，并记录流程结束状态
     *
     * @param processInstanceId
     * @param endType           1结束 2取消并留存数据 3取消并删除数据
     * @param operator
     * @return
     */
    @ReconfirmAuthorization
    public Boolean endProcessWithStatus(String processInstanceId, Integer endType, String operator) {
        Optional<CWHistoricProcessInstance> optional = processInstanceService.getHistoricProcessInstanceById(processInstanceId);
        if (!optional.isPresent()) {
            log.warn("Process instance does not exist!");
            throw new ProcessError("Process instance does not exist!");
        }
        CWHistoricProcessInstance historicProcessInstance = optional.get();
        if (historicProcessInstance.getEndTime() != null) {
            log.warn("Process has ended!");
            throw new ProcessError("Process has ended!");
        }
        if (!EndedProcInstStatusEnum.isExist(endType)) {
            log.warn("EndType error!");
            throw new ProcessError("EndType error!");
        }
        // 添加操作记录 operator
        addProcessRecord(historicProcessInstance, operator, ExtProcessRecordAction.END_PROCESS.value(), null, null, false, false);

        if (EndedProcInstStatusEnum.ENDED.getEndType().equals(endType)) {
            extProcessInstanceService.endProcessInstance(processInstanceId, "terminate process instance");
        } else if (EndedProcInstStatusEnum.CANCELLED.getEndType().equals(endType)) {
            extProcessInstanceService.cancelProcessInstance(processInstanceId, "cancel process instance");
        } else if (EndedProcInstStatusEnum.DELETED.getEndType().equals(endType)) {
            extProcessInstanceService.deleteProcessInstance(processInstanceId, "delete process instance");
        }
        return true;
    }

    /**
     * 基于已生成任务修改，审批方式不变。不同的审批方式，操作任务增减原则不同：
     * 或签：多个候选人只会产生一个task。删除操作即对当前task删除候选人（候选人间为或的关系）
     * 会签：多个候选人会产生多个task。删除操作即删除该候选人关联的task（候选人间为与的关系）
     * 依次审批：多个候选人依次生成task。一个task对应一个候选人。删除操作即仅对当前task删除候选人（候选人间为或的关系）
     *
     * @param taskId
     * @param removedCandidateUsers
     * @param operator
     * @param isAdmin
     * @return
     */
    private Boolean removeTaskHandler(String taskId, List<String> removedCandidateUsers, String operator, Boolean isAdmin) {
        if (StringUtils.isBlank(taskId)) {
            log.warn("The taskId cannot be empty!");
            throw new ProcessError("The taskId cannot be empty!");
        }

        if (!CollectionUtils.isEmpty(removedCandidateUsers)) {
            removedCandidateUsers = removedCandidateUsers.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(removedCandidateUsers)) {
            log.warn("removedCandidateUsers can not be empty!");
            throw new ProcessExtCommonException("removedCandidateUsers can not be empty!");
        }

        if (StringUtils.isBlank(operator)) {
            log.warn("The operator cannot be empty!");
            throw new ProcessError("The operator cannot be empty!");
        }

        Optional<CWHistoricTaskInstance> optional = taskInstanceService.getHistoricTaskInstanceById(taskId);
        if (!optional.isPresent()) {
            log.warn("Task is not exist!");
            throw new ProcessError("Task is not exist!");
        }

        CWHistoricTaskInstance taskInstance = optional.get();

        if (taskInstance.getCategory() != null && !taskInstance.getCategory().equals(ProcessConstant.TASK_CATEGORY_APPROVAL_TASK)) {
            log.warn("The type of task is not an approval task and removeTaskHandler operation is not supported.");
            throw new ProcessError("The type of task is not an approval task and removeTaskHandler operation is not supported.");
        }

        if (taskInstance.getEndTime() != null) {
            log.warn("The task has ended and removeTaskHandler operations are prohibited!");
            throw new ProcessError("The task has ended and removeTaskHandler operations are prohibited!");
        }

        if (!isAdmin) {
            // 判断经办人是否有权限操作该任务
            if (CollectionUtil.isEmpty(taskInstance.getCandidateUsers()) && StringUtils.isEmpty(taskInstance.getAssignee())) {
                log.warn("There is no person in charge of this task, and other people have no rights to operate it.");
                throw new ProcessError("There is no person in charge of this task, and other people have no rights to operate it.");
            }
            if (!taskInstance.getCandidateUsers().contains(operator) && !StringUtils.equals(taskInstance.getAssignee(), operator)) {
                log.warn("This task is not belong to the operator and removeTaskHandler operation is not allowed.");
                throw new ProcessError("This task is not belong to the operator and removeTaskHandler operation is not allowed.");
            }
        }

        StringBuilder userStr = new StringBuilder();
        removedCandidateUsers.forEach(user -> userStr.append(user).append(","));
        String comment = "handlers remove : " + userStr;
        // 添加审批记录
        addTaskRecord(taskInstance, operator, ExtProcessRecordAction.REMOVE_TASK_HANDLER.value(), null, comment, false, false);

        extTaskInstanceService.removeTaskHandler(operator, removedCandidateUsers, comment, taskInstance);

        return true;
    }
}
