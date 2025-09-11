package com.netease.lowcode.lib.logic.impl;

import com.netease.codewave.process.api.domain.CWHistoricTaskInstance;
import com.netease.codewave.process.api.domain.CWHistoricTaskInstanceQuery;
import com.netease.codewave.process.api.domain.CWPageOf;
import com.netease.codewave.process.api.service.ProcessInstanceService;
import com.netease.codewave.process.api.service.TaskInstanceService;
import com.netease.codewave.process.service.ProcessUserIdentityService;
import com.netease.lowcode.annotation.helper.provider.OverriddenFrameworkHelper;
import com.netease.lowcode.lib.annotation.ReconfirmAuthorization;
import com.netease.lowcode.lib.enums.ProcInstStatusEnum;
import com.netease.lowcode.lib.exception.ProcessExtCommonException;
import com.netease.lowcode.lib.logic.structure.*;
import com.netease.lowcode.lib.service.ExtProcessUserIdentityService;
import com.netease.lowcode.lib.service.ExtTaskInstanceSearchService;
import com.netease.lowcode.lib.util.ProcessUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExtTaskInstanceSearchLogicService {
    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Autowired
    public ApplicationContext applicationContext;
    @Resource
    private ExtTaskInstanceSearchService extTaskInstanceSearchService;
    @Resource
    private ExtProcessUserIdentityService extProcessUserIdentityService;
    @Resource
    private TaskInstanceService taskInstanceService;
    @Resource
    private ProcessUserIdentityService processUserIdentityService;
    @Resource
    private ProcessInstanceService processInstanceService;

    private String getUserName(String overrideLogicReq) {
        String finalUserName;
        if (applicationContext.containsBean("customGetUserNameOverriddenLcap_process_extCustomizeService")) {
            try {
                String userName = (String) OverriddenFrameworkHelper
                        .invokeOverriddenMethod0("customGetUserName", "lcap-process-ext", overrideLogicReq);
                if (StringUtils.isNotBlank(userName)) {
                    finalUserName = userName;
                } else {
                    throw new ProcessExtCommonException(-1, "customGetUserName获取自定义逻辑的username异常：" + userName);
                }
            } catch (Exception e) {
                throw new ProcessExtCommonException(-1, "customGetUserName获取自定义逻辑的username异常：" + e.getMessage());
            }
        } else {
            finalUserName = extProcessUserIdentityService.getCurrentUserName();
        }
        return finalUserName;
    }

    /**
     * 查询我的待办任务列表(getUserName自定义获取用户)
     *
     * @param procDefKey              流程定义Key
     * @param procInstStartTimeAfter  流程实例开始时间后
     * @param procInstStartTimeBefore 流程实例开始时间前
     * @param procInstInitiator       流程实例发起人
     * @param page                    页码
     * @param size                    每页数量
     * @param search                  搜索关键词（流程定义名）
     * @param overrideLogicReq        复写逻辑请求入参
     * @return 我的待办任务列表
     */

    public MyPendingTaskPage getMyPendingTasksByCustomUser(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                                           ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                                           Long page, Long size, String search, String overrideLogicReq) {
        return extTaskInstanceSearchService.getMyPendingTasks(procDefKey, procInstStartTimeAfter, procInstStartTimeBefore,
                procInstInitiator, page, size, search, getUserName(overrideLogicReq));
    }

    /**
     * 查询我的已办任务列表(getUserName自定义获取用户)
     *
     * @param procDefKey              流程定义Key
     * @param procInstStartTimeAfter  流程实例开始时间后
     * @param procInstStartTimeBefore 流程实例开始时间前
     * @param procInstInitiator       流程实例发起人
     * @param page                    页码
     * @param size                    每页数量
     * @param search                  搜索关键词（流程定义名）
     * @param overrideLogicReq        复写逻辑请求入参
     * @return 我的已办任务列表
     */
    public MyCompletedTaskPage getMyCompletedTasksByCustomUser(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                                               ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                                               Long page, Long size, String search, String overrideLogicReq) {
        return  extTaskInstanceSearchService.getMyCompletedTasks(procDefKey, procInstStartTimeAfter, procInstStartTimeBefore,
                procInstInitiator, page, size, search, getUserName(overrideLogicReq));
    }

    /**
     * 查询我的发起任务列表(getUserName自定义获取用户)
     *
     * @param procDefKey              流程定义Key
     * @param procInstStartTimeAfter  流程实例开始时间后
     * @param procInstStartTimeBefore 流程实例开始时间前
     * @param procInstInitiator       流程实例发起人
     * @param page                    页码
     * @param size                    每页数量
     * @param search                  搜索关键词（流程定义名）
     * @param overrideLogicReq        复写逻辑请求入参
     * @return 我的发起任务列表
     */

    public MyInitiatedTaskPage getMyInitiatedTasksByCustomUser(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                                               ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                                               Long page, Long size, String search, String overrideLogicReq) {
        return extTaskInstanceSearchService.getMyInitiatedTasks(procDefKey, procInstStartTimeAfter, procInstStartTimeBefore,
                procInstInitiator, page, size, search, getUserName(overrideLogicReq));
    }

    /**
     * 查询抄送给我的任务(getUserName自定义获取用户)
     *
     * @param procDefKey              流程定义Key
     * @param procInstStartTimeAfter  流程实例开始时间后
     * @param procInstStartTimeBefore 流程实例开始时间前
     * @param procInstInitiator       流程实例发起人
     * @param viewed                  是否已读
     * @param page                    页码
     * @param size                    每页数量
     * @param search                  搜索关键词（流程定义名）
     * @param overrideLogicReq        复写逻辑请求入参
     * @return 抄送我的任务列表
     */

    public MyCCTaskPage getMyCCTasksByCustomUser(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                                 ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                                 Boolean viewed, Long page, Long size,
                                                 String search, String overrideLogicReq) {
        return extTaskInstanceSearchService.getMyCCTasks(procDefKey, procInstStartTimeAfter, procInstStartTimeBefore,
                procInstInitiator, viewed, page, size, search, getUserName(overrideLogicReq));
    }

    /**
     * 查询任务列表
     *
     * @param taskId
     * @param procDefKey
     * @param procInstId
     * @param taskDefName
     * @param candidateUser
     * @param finished
     * @param page
     * @param size
     * @param startTimeBefore
     * @param startTimeAfter
     * @return
     */
    @ReconfirmAuthorization
    public TaskInstPage pageRunTaskInstance(String taskId, String procDefKey, String procInstId, String taskDefName, String candidateUser, Boolean finished,
                                            Long page, Long size, ZonedDateTime startTimeBefore, ZonedDateTime startTimeAfter) {
        page = Optional.ofNullable(page).orElse(1L);
        size = Optional.ofNullable(size).orElse(20L);
        CWHistoricTaskInstanceQuery taskInstanceQuery = new CWHistoricTaskInstanceQuery();
        taskInstanceQuery.setProcessInstanceId(procInstId);
        taskInstanceQuery.setTaskId(taskId);
        taskInstanceQuery.setNodeName(taskDefName);
        taskInstanceQuery.setQueryUser(candidateUser);
        taskInstanceQuery.setProcessDefinitionKey(procDefKey);
        taskInstanceQuery.setTaskWithoutDeleteReason(true);
        taskInstanceQuery.setSort("desc");
        taskInstanceQuery.setPage(page);
        taskInstanceQuery.setSize(size);
        taskInstanceQuery.setOrder("createTime");
        taskInstanceQuery.setFinished(finished);
        taskInstanceQuery.setStartTimeAfter(startTimeBefore);
        taskInstanceQuery.setStartTimeBefore(startTimeAfter);

        CWPageOf<CWHistoricTaskInstance> historicTaskInstancePageOf = taskInstanceService.queryHistoricTaskInstanceList(taskInstanceQuery);

        List<TaskInst> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(historicTaskInstancePageOf.getList())) {
            result = historicTaskInstancePageOf.getList()
                    .stream()
                    .map(this::toTaskInst)
                    .collect(Collectors.toList());
        }
        return TaskInstPage.of(result, historicTaskInstancePageOf.getTotal());
    }

    private TaskInst toTaskInst(CWHistoricTaskInstance taskInstance) {
        if (taskInstance == null) {
            return null;
        }

        TaskInst taskInst = new TaskInst();
        taskInst.setApprovalPolicy(taskInstance.getApprovalPolicy());
        taskInst.setTaskId(taskInstance.getTaskId());
        taskInst.setTitle(taskInstance.getNodeTitle());
        taskInst.setDescription(taskInstance.getNodeDescription());
        taskInst.setTaskTitle(taskInstance.getTaskTitle());
        taskInst.setTaskDescription(taskInstance.getTaskDescription());
        taskInst.setCategory(taskInstance.getCategory());
        taskInst.setFinished(taskInstance.getEndTime() != null);
        taskInst.setCompleteBy(ProcessUserUtil.toProcessUserFront(processUserIdentityService.getUsersByUserName(taskInstance.getAssignee())));
        taskInst.setCreateTime(taskInstance.getCreateTime());
        taskInst.setCompleteTime(taskInstance.getEndTime());
        taskInst.setTaskDefName(taskInstance.getNodeName());
        taskInst.setProcessInstanceId(taskInstance.getProcessInstanceId());
        taskInst.setProcessDefUniqueKey(taskInstance.getProcessDefinitionId().split(":")[0]);
        taskInst.setProcessDefinitionId(taskInstance.getProcessDefinitionId());

        List<ProcessUser> candidateProcessUsers = taskInstance.getCandidateUsers()
                .stream()
                .map(userName -> processUserIdentityService.getUsersByUserName(userName))
                .map(ProcessUserUtil::toProcessUserFront)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        taskInst.setCandidateUsers(candidateProcessUsers);

        processInstanceService.getHistoricProcessInstanceById(taskInstance.getProcessInstanceId())
                .ifPresent(processInstance -> {
                    taskInst.setProcessDefName(processInstance.getProcessDefinitionTitle());
                    taskInst.setProcInstStatus(processInstance.getEndTime() != null ? ProcInstStatusEnum.Approved.getCode() : ProcInstStatusEnum.Approving.getCode());
                });

        return taskInst;
    }
}
