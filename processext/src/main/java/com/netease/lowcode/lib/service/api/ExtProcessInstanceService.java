package com.netease.lowcode.lib.service.api;

public interface ExtProcessInstanceService {
    /**
     * 终止流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param deleteReason      删除原因
     */
    void endProcessInstance(String processInstanceId, String deleteReason);

    /**
     * 取消流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param deleteReason      删除原因
     */
    void cancelProcessInstance(String processInstanceId, String deleteReason);

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param deleteReason      删除原因
     */
    void deleteProcessInstance(String processInstanceId, String deleteReason);

}
