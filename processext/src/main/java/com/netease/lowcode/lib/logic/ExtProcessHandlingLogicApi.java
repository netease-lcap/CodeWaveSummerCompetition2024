package com.netease.lowcode.lib.logic;

import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.lib.logic.impl.ExtProcessHandlingLogicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExtProcessHandlingLogicApi {

    @Resource
    private ExtProcessHandlingLogicService extProcessHandlingLogicService;

    /**
     * 更新任务实例候选处理人（管理员）
     * 基于已生成任务修改，不操作任务的增减
     *
     * @param taskId
     * @param updatedCandidateUsers
     * @param operatorUser
     * @return
     */
    @NaslLogic
    public Boolean updateTaskHandlerAdmin(String taskId, List<String> updatedCandidateUsers, String operatorUser) {
        extProcessHandlingLogicService.updateTaskHandlerAdmin(taskId, updatedCandidateUsers, operatorUser);
        return true;
    }

    /**
     * 更新任务实例候选处理人（当前任务审批人）
     * 基于已生成任务修改，不操作任务的增减
     *
     * @param taskId
     * @param updatedCandidateUsers
     * @return
     */
//    @NaslLogic
    public Boolean updateTaskHandler(String taskId, List<String> updatedCandidateUsers) {
        extProcessHandlingLogicService.updateTaskHandler(taskId, updatedCandidateUsers);
        return true;
    }

    /**
     * 新增任务实例候选处理人
     * 基于已生成任务修改，审批方式不变。不同的审批方式，操作任务增减原则不同：
     * 或签：多个候选人只会产生一个task。新增操作即对当前task新增候选人（候选人间为或的关系）
     * 会签：多个候选人会产生多个task。新增操作即生成新的task，新task关联新候选人（候选人间为与的关系）
     * 依次审批：多个候选人依次生成task。一个task对应一个候选人。新增操作即仅对当前task新增候选人（候选人间为或的关系）
     *
     * @param taskId              任务id
     * @param addedCandidateUsers 被添加的候选处理人
     * @return
     */
//    @NaslLogic
    public Boolean addTaskHandler(String taskId, List<String> addedCandidateUsers) {
        extProcessHandlingLogicService.addTaskHandler(taskId, addedCandidateUsers);
        return true;
    }

    /**
     * 新增任务实例候选处理人（管理员）
     * 基于已生成任务修改，审批方式不变。不同的审批方式，操作任务增减原则不同：
     * 或签：多个候选人只会产生一个task。新增操作即对当前task新增候选人（候选人间为或的关系）
     * 会签：多个候选人会产生多个task。新增操作即生成新的task，新task关联新候选人（候选人间为与的关系）
     * 依次审批：多个候选人依次生成task。一个task对应一个候选人。新增操作即仅对当前task新增候选人（候选人间为或的关系）
     *
     * @param taskId              任务id
     * @param addedCandidateUsers 被添加的候选处理人
     * @param operatorUser        操作人
     * @return
     */
    @NaslLogic
    public Boolean addTaskHandlerAdmin(String taskId, List<String> addedCandidateUsers, String operatorUser) {
        extProcessHandlingLogicService.addTaskHandlerAdmin(taskId, addedCandidateUsers, operatorUser);
        return true;
    }

    /**
     * 减少任务实例候选处理人（管理员）
     * 基于已生成任务修改，审批方式不变。不同的审批方式，操作任务增减原则不同：
     * 或签：多个候选人只会产生一个task。删除操作即对当前task删除候选人（候选人间为或的关系）
     * 会签：多个候选人会产生多个task。删除操作即删除该候选人关联的task（候选人间为与的关系）。注：会签禁止删除所有候选人
     * 依次审批：多个候选人依次生成task。一个task对应一个候选人。删除操作即仅对当前task删除候选人（候选人间为或的关系）
     *
     * @param taskId                任务id
     * @param removedCandidateUsers 被添加的候选处理人
     * @param operatorUser
     * @return
     */
    @NaslLogic
    public Boolean removeTaskHandlerAdmin(String taskId, List<String> removedCandidateUsers, String operatorUser) {
        extProcessHandlingLogicService.removeTaskHandlerAdmin(taskId, removedCandidateUsers, operatorUser);
        return true;
    }

    /**
     * 结束流程实例，并记录流程结束状态
     *
     * @param processInstanceId
     * @param endType           1结束 2取消并留存数据 3取消并删除数据
     * @param operatorUser
     * @return
     */
    @NaslLogic
    public Boolean endProcessWithStatus(String processInstanceId, Integer endType, String operatorUser) {
        extProcessHandlingLogicService.endProcessWithStatus(processInstanceId, endType, operatorUser);
        return true;
    }
}
