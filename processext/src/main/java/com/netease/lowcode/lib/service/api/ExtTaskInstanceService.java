package com.netease.lowcode.lib.service.api;

import com.netease.codewave.process.api.domain.CWHistoricTaskInstance;

import java.util.List;

/**
 * TaskInstanceService
 *
 * @author baojz
 */
public interface ExtTaskInstanceService {
    /**
     * 移除候选处理人
     *
     * @param operator              操作者
     * @param removedCandidateUsers 被添加的候选人
     * @param comment               审批意见
     * @param taskInstance          任务实例
     */
    void removeTaskHandler(String operator, List<String> removedCandidateUsers, String comment, CWHistoricTaskInstance taskInstance);

    /**
     * 更新任务候选人，需要确保任务未结束
     *
     * @param updateCandidateUsers 即将被更新的用户组列表
     * @param operator             操作者
     * @param comment              审批意见
     * @param taskInstance         任务实例
     */

    void updateCandidateUsers(List<String> updateCandidateUsers, String operator, String comment, CWHistoricTaskInstance taskInstance);

    /**
     * 新增候选处理人
     *
     * @param operator            操作者
     * @param addedCandidateUsers 被添加的候选人
     * @param comment             审批意见
     * @param taskInstance        任务实例
     */
    void addTaskHandler(String operator, List<String> addedCandidateUsers, String comment, CWHistoricTaskInstance taskInstance);

}
