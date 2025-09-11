package com.netease.lowcode.lib.logic;

import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.lib.logic.impl.ExtTaskInstanceSearchLogicService;
import com.netease.lowcode.lib.logic.structure.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZonedDateTime;

@Service
public class ExtTaskInstanceSearchLogicApi {
    @Resource
    private ExtTaskInstanceSearchLogicService extTaskInstanceSearchLogicService;

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
    @NaslLogic
    public MyPendingTaskPage getMyPendingTasksByCustomUser(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                                           ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                                           Long page, Long size, String search, String overrideLogicReq) {
        return extTaskInstanceSearchLogicService.getMyPendingTasksByCustomUser(procDefKey, procInstStartTimeAfter,
                procInstStartTimeBefore, procInstInitiator, page, size, search, overrideLogicReq);
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
    @NaslLogic
    public MyCompletedTaskPage getMyCompletedTasksByCustomUser(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                                               ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                                               Long page, Long size, String search, String overrideLogicReq) {
        return extTaskInstanceSearchLogicService.getMyCompletedTasksByCustomUser(procDefKey, procInstStartTimeAfter, procInstStartTimeBefore,
                procInstInitiator, page, size, search, overrideLogicReq);
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
    @NaslLogic
    public MyInitiatedTaskPage getMyInitiatedTasksByCustomUser(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                                               ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                                               Long page, Long size, String search, String overrideLogicReq) {
        return extTaskInstanceSearchLogicService.getMyInitiatedTasksByCustomUser(procDefKey, procInstStartTimeAfter,
                procInstStartTimeBefore, procInstInitiator, page, size, search, overrideLogicReq);
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
    @NaslLogic
    public MyCCTaskPage getMyCCTasksByCustomUser(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                                 ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                                 Boolean viewed, Long page, Long size,
                                                 String search, String overrideLogicReq) {
        return extTaskInstanceSearchLogicService.getMyCCTasksByCustomUser(procDefKey, procInstStartTimeAfter,
                procInstStartTimeBefore, procInstInitiator, viewed, page, size, search, overrideLogicReq);
    }

    /**
     * 分页获取任务实例（管理员）
     *
     * @param taskId
     * @param procDefKey
     * @param procInstId
     * @param taskDefName     节点名称
     * @param candidateUser   参与人名称
     * @param finished        任务是否已完成
     * @param page
     * @param size
     * @param startTimeBefore
     * @param startTimeAfter
     * @return
     */
    @NaslLogic
    public TaskInstPage pageRunTaskInstance(String taskId, String procDefKey, String procInstId, String taskDefName, String candidateUser, Boolean finished,
                                            Long page, Long size, ZonedDateTime startTimeBefore, ZonedDateTime startTimeAfter) {
        return extTaskInstanceSearchLogicService.pageRunTaskInstance(taskId, procDefKey, procInstId, taskDefName, candidateUser, finished,
                page, size, startTimeBefore, startTimeAfter);
    }
}
