package com.netease.lowcode.lib.service;

import com.netease.codewave.process.api.domain.*;
import com.netease.codewave.process.api.domain.custom.CustomCCTaskQuery;
import com.netease.codewave.process.api.domain.custom.CustomTask;
import com.netease.codewave.process.api.domain.custom.CustomTaskQuery;
import com.netease.codewave.process.api.service.ProcessInstanceService;
import com.netease.codewave.process.api.service.TaskInstanceService;
import com.netease.codewave.process.api.service.custom.CustomTaskQueryService;
import com.netease.codewave.process.util.ProcessConstant;
import com.netease.lowcode.lib.logic.impl.ExtProcessInstanceSearchLogicService;
import com.netease.lowcode.lib.logic.structure.*;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExtTaskInstanceSearchService {
    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Autowired
    public ApplicationContext applicationContext;
    @Resource
    private CustomTaskQueryService customTaskQueryService;
    @Resource
    private TaskInstanceService taskInstanceService;
    @Resource
    private ExtProcessUserIdentityService extProcessUserIdentityService;
    @Resource
    private ExtProcessInstanceSearchLogicService extProcessInstanceSearchLogicService;
    @Resource
    private ProcessInstanceService processInstanceService;

    /**
     * 查询我的待办任务列表
     *
     * @param procDefKey              流程定义Key
     * @param procInstStartTimeAfter  流程实例开始时间后
     * @param procInstStartTimeBefore 流程实例开始时间前
     * @param procInstInitiator       流程实例发起人
     * @param page                    页码
     * @param size                    每页数量
     * @param search                  搜索关键词（流程定义名）
     * @param userName                用户名称
     * @return 我的待办任务列表
     */
    public MyPendingTaskPage getMyPendingTasks(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                               ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                               Long page, Long size, String search, String userName) {
        page = Optional.ofNullable(page).orElse(1L);
        size = Optional.ofNullable(size).orElse(20L);
        CustomTaskQuery query = new CustomTaskQuery();
        query.userName = userName;
        query.offset = (page.intValue() - 1) * size.intValue();
        query.size = size;
        query.sort = "desc";
        query.finished = false;
        query.procDefKey = procDefKey;
        if (StringUtils.isNotBlank(search)) {
            query.search = search + "%";
        }
        query.procInstStartTimeAfter = procInstStartTimeAfter;
        query.procInstStartTimeBefore = procInstStartTimeBefore;
        query.procInstInitiator = procInstInitiator;
        Long count = customTaskQueryService.countCustomRunTask(query);
        if (count == 0) {
            return MyPendingTaskPage.empty();
        }
        List<CustomTask> customTasks = customTaskQueryService.selectCustomRunTaskList(query);
        List<MyPendingTask> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(customTasks)) {
            List<MyPendingTask> pItems = customTasks.stream().map(task -> {
                MyPendingTask myPendingTask = new MyPendingTask();
                Optional<CWRunTaskInstance> optional = taskInstanceService.getRunTaskInstanceById(task.getTaskId());
                optional.ifPresent(runTaskInstance -> {
                    List<CurrNode> currentNodeList = new LinkedList<>();
                    List<ProcessUser> currNodeParticipants = runTaskInstance.getCandidateUsers().stream()
                            .map(user -> extProcessUserIdentityService.getUsersByUserName(user))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    currentNodeList.add(new CurrNode(runTaskInstance.getNodeTitle(), currNodeParticipants));
                    myPendingTask.setProcInstCurrNodes(currentNodeList);
                });

                myPendingTask.setTaskId(task.getTaskId());
                myPendingTask.setTaskTitle(task.getTaskTitle());
                myPendingTask.setProcDefTitle(task.getProcDefTitle());
                myPendingTask.setProcInstTitle(task.getProcInstTitle());
                myPendingTask.setProcInstInitiator(extProcessUserIdentityService.getUsersByUserName(task.getProcInstInitiator()));
                myPendingTask.setProcInstStartTime(task.getProcInstStartTime());
                return myPendingTask;
            }).collect(Collectors.toList());
            result.addAll(pItems);
        }
        return MyPendingTaskPage.of(result, count);
    }

    /**
     * 查询我的已办任务列表
     *
     * @param procDefKey              流程定义Key
     * @param procInstStartTimeAfter  流程实例开始时间后
     * @param procInstStartTimeBefore 流程实例开始时间前
     * @param procInstInitiator       流程实例发起人
     * @param page                    页码
     * @param size                    每页数量
     * @param search                  搜索关键词（流程定义名）
     * @param userName                用户名称
     * @return 我的已办任务列表
     */
    public MyCompletedTaskPage getMyCompletedTasks(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                                   ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                                   Long page, Long size, String search, String userName) {
        page = Optional.ofNullable(page).orElse(1L);
        size = Optional.ofNullable(size).orElse(20L);
        CustomTaskQuery query = new CustomTaskQuery();
        query.userName = userName;
        query.offset = (page.intValue() - 1) * size.intValue();
        query.size = size;
        query.sort = "desc";
        query.finished = true;
        query.procDefKey = procDefKey;
        if (StringUtils.isNotBlank(search)) {
            query.search = search + "%";
        }
        query.procInstStartTimeAfter = procInstStartTimeAfter;
        query.procInstStartTimeBefore = procInstStartTimeBefore;
        query.procInstInitiator = procInstInitiator;
        Long count = customTaskQueryService.countCustomTask(query);
        if (count == 0) {
            return MyCompletedTaskPage.empty();
        }
        List<CustomTask> customTasks = customTaskQueryService.selectCustomTaskList(query);
        List<MyCompletedTask> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(customTasks)) {
            List<MyCompletedTask> pItems = customTasks.stream().map(task -> {
                MyCompletedTask myCompletedTask = new MyCompletedTask();
                List<CurrNode> currentNodeList = getCurrentNodeList(task.getProcessInstanceId());
                myCompletedTask.setTaskId(task.getTaskId());
                myCompletedTask.setTaskTitle(task.getTaskTitle());
                Optional<CWHistoricTaskInstance> optional = taskInstanceService.getHistoricTaskInstanceById(task.getTaskId());
                optional.ifPresent(cwHistoricTaskInstance -> myCompletedTask.setNodeTitle(cwHistoricTaskInstance.getNodeTitle()));
                myCompletedTask.setProcInstTitle(task.getProcInstTitle());
                myCompletedTask.setProcDefTitle(task.getProcDefTitle());
                myCompletedTask.setProcInstInitiator(extProcessUserIdentityService.getUsersByUserName(task.getProcInstInitiator()));
                myCompletedTask.setProcInstStartTime(task.getProcInstStartTime());
                myCompletedTask.setProcInstCurrNodes(currentNodeList);
                return myCompletedTask;
            }).collect(Collectors.toList());
            result.addAll(pItems);
        }
        return MyCompletedTaskPage.of(result, count);
    }

    /**
     * 查询我的发起任务列表
     *
     * @param procDefKey              流程定义Key
     * @param procInstStartTimeAfter  流程实例开始时间后
     * @param procInstStartTimeBefore 流程实例开始时间前
     * @param procInstInitiator       流程实例发起人
     * @param page                    页码
     * @param size                    每页数量
     * @param search                  搜索关键词（流程定义名）
     * @param userName                用户名称
     * @return 我的发起任务列表
     */
    public MyInitiatedTaskPage getMyInitiatedTasks(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                                   ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                                   Long page, Long size, String search, String userName) {
        page = Optional.ofNullable(page).orElse(1L);
        size = Optional.ofNullable(size).orElse(20L);

        String currentUserName = userName;

        CWHistoricProcessInstanceQuery query = new CWHistoricProcessInstanceQuery();
        query.setProcessDefinitionKey(procDefKey);
        query.setStartBy(currentUserName);
        query.setStartTimeAfter(procInstStartTimeAfter);
        query.setStartTimeBefore(procInstStartTimeBefore);
        query.setProcessDefinitionTitle(search);
        query.setPage(page);
        query.setSize(size);
        query.setSort("desc");

        CWPageOf<CWHistoricProcessInstance> historicProcessInstancePageOf = processInstanceService.queryHistoricProcessInstanceList(query);

        List<MyInitiatedTask> result = new ArrayList<>();
        historicProcessInstancePageOf.getList().forEach(historicProcessInstance -> {

            CWHistoricTaskInstanceQuery taskInstanceQuery = new CWHistoricTaskInstanceQuery();

            taskInstanceQuery.setQueryUser(currentUserName);
            taskInstanceQuery.setProcessInstanceId(historicProcessInstance.getId());
            taskInstanceQuery.setCategory(ProcessConstant.TASK_CATEGORY_INITIATE_TASK);
            taskInstanceQuery.setTaskWithoutDeleteReason(true);
            taskInstanceQuery.setSort("desc");
            taskInstanceQuery.setPage(1L);
            taskInstanceQuery.setSize(1L);
            taskInstanceQuery.setOrder("createTime");

            // 获取"InitiateTask"类型的最近被创建的任务，只返回1条即可
            CWPageOf<CWHistoricTaskInstance> historicTaskInstancePageOf = taskInstanceService.queryHistoricTaskInstanceList(taskInstanceQuery);

            if (CollectionUtil.isNotEmpty(historicTaskInstancePageOf.getList())) {
                CWHistoricTaskInstance historicTaskInstance = historicTaskInstancePageOf.getList().get(0);
                List<CurrNode> currentNodeList = getCurrentNodeList(historicProcessInstance.getId());

                MyInitiatedTask myInitiatedTask = new MyInitiatedTask();
                myInitiatedTask.setTaskId(historicTaskInstance.getTaskId());
                myInitiatedTask.setTaskTitle(historicTaskInstance.getTaskTitle());
                myInitiatedTask.setProcInstTitle(historicProcessInstance.getTitle());
                myInitiatedTask.setProcDefTitle(historicProcessInstance.getProcessDefinitionTitle());
                myInitiatedTask.setProcInstInitiator(extProcessUserIdentityService.getUsersByUserName(historicProcessInstance.getStartBy()));
                myInitiatedTask.setProcInstStartTime(historicProcessInstance.getStartTime());
                myInitiatedTask.setProcInstCurrNodes(currentNodeList);
                result.add(myInitiatedTask);
            }
        });
        return MyInitiatedTaskPage.of(new ArrayList<>(result), historicProcessInstancePageOf.getTotal());
    }

    /**
     * 查询抄送给我的任务
     *
     * @param procDefKey              流程定义Key
     * @param procInstStartTimeAfter  流程实例开始时间后
     * @param procInstStartTimeBefore 流程实例开始时间前
     * @param procInstInitiator       流程实例发起人
     * @param viewed                  是否已读
     * @param page                    页码
     * @param size                    每页数量
     * @param search                  搜索关键词（流程定义名）
     * @param userName                用户名称
     * @return 抄送我的任务列表
     */
    public MyCCTaskPage getMyCCTasks(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                     ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                     Boolean viewed, Long page, Long size,
                                     String search, String userName) {
        page = Optional.ofNullable(page).orElse(1L);
        size = Optional.ofNullable(size).orElse(20L);
        CustomCCTaskQuery query = new CustomCCTaskQuery();
        query.userName = userName;
        query.offset = (page.intValue() - 1) * size.intValue();
        query.size = size;
        query.sort = "desc";
        query.viewed = viewed;
        query.variableName = ProcessConstant.KEY_VARIABLE_CC_TASK_VIEWED_PREFIX + query.userName;
        query.procDefKey = procDefKey;
        if (StringUtils.isNotBlank(search)) {
            query.search = search + "%";
        }
        query.procInstStartTimeAfter = procInstStartTimeAfter;
        query.procInstStartTimeBefore = procInstStartTimeBefore;
        query.procInstInitiator = procInstInitiator;
        Long count = customTaskQueryService.countCCTask(query);
        if (count == 0) {
            return MyCCTaskPage.empty();
        }
        List<CustomTask> customTasks = customTaskQueryService.selectCCTaskList(query);
        List<MyCCTask> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(customTasks)) {
            List<MyCCTask> pItems = customTasks.stream().map(task -> {
                MyCCTask myCCTask = new MyCCTask();
                List<CurrNode> currentNodeList = getCurrentNodeList(task.getProcessInstanceId());
                myCCTask.setTaskId(task.getTaskId());
                myCCTask.setTaskTitle(task.getTaskTitle());
                myCCTask.setProcInstTitle(task.getProcInstTitle());
                myCCTask.setProcDefTitle(task.getProcDefTitle());
                myCCTask.setProcInstInitiator(extProcessUserIdentityService.getUsersByUserName(task.getProcInstInitiator()));
                myCCTask.setProcInstStartTime(task.getProcInstStartTime());
                myCCTask.setProcInstCurrNodes(currentNodeList);
                return myCCTask;
            }).collect(Collectors.toList());
            result.addAll(pItems);
        }
        return MyCCTaskPage.of(new ArrayList<>(result), count);
    }

    public List<CurrNode> getCurrentNodeList(String processInstanceId) {
        return extProcessInstanceSearchLogicService.getCurrentNodeList(processInstanceId);
    }
}
