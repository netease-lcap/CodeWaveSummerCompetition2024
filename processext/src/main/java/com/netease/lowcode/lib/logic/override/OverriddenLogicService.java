package com.netease.lowcode.lib.logic.override;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.springframework.stereotype.Component;

@Component
public class OverriddenLogicService {
    /**
     * 判断当前用户是否有权操作logicName对应逻辑
     * logicName视为资源点，对齐进行二次鉴权
     *
     * @param logicName 示例：
     *                  updateTaskHandler
     *                  launchProcessCustomName
     *                  getNextNodeInfo
     *                  getNextNodeInfoByConditionalFlowId
     * @return
     */
    @NaslLogic(override = true)
    public Boolean reconfirmAuthorization(String logicName) {
        return true;
    }

    /**
     * 获取流程/任务信息时，自定义获取用户名，支持复写
     *
     * @param req
     * @return
     */
    @NaslLogic(override = true)
    public String customGetUserName(String req) {
        return null;
    }

    /**
     * 扩展依赖库监听事件，支持复写
     *
     * @param event 事件类型
     *              ProcessStartedEvent：流程启动事件
     *              ProcessEndedEvent：流程结束事件
     *              ProcessTerminatedEvent：流程终止事件
     *              TaskCreatedEvent：任务创建事件
     *              TaskClosedEvent：任务关闭事件
     *              TaskCancelledEvent：任务取消事件
     *              TaskNodeStartEvent：任务节点开始事件
     *              TaskNodeEndEvent：任务节点结束事件
     *              TaskOperationEvent：任务操作事件
     *              TaskApprovedEvent：任务审批通过事件
     *              TaskRejectedEvent：任务拒绝通过事件
     *              TaskReassignedEvent：任务转派事件
     *              TaskAddSignEvent：任务加签事件
     *              TaskSubmittedEvent：任务提交事件
     *              TaskRevertedEvent：任务回退事件
     *              TaskWithdrawnEvent：任务撤回事件
     * @param json  ProcessEvent事件内容字符串
     * @return
     */
    @NaslLogic(override = true)
    public String customProcessOnEvent(String event, String json) {
        return "";
    }
}
