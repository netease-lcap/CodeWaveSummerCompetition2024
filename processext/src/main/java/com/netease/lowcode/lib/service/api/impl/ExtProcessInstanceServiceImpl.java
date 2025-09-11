package com.netease.lowcode.lib.service.api.impl;

import com.netease.codewave.process.api.service.ProcessInstanceService;
import com.netease.lowcode.lib.enums.EndedProcInstStatusEnum;
import com.netease.lowcode.lib.service.api.ExtProcessInstanceService;
import com.netease.lowcode.lib.util.ProcessConstant;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ExtProcessInstanceServiceImpl implements ExtProcessInstanceService {
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private HistoryService historyService;
    @Resource
    private TaskService taskService;
    @Resource
    private ProcessInstanceService processInstanceService;

    @Override
    public void endProcessInstance(String processInstanceId, String deleteReason) {
        runtimeService.setVariable(processInstanceId, ProcessConstant.EXT_PROCESS_TERMINATION_STATUS, EndedProcInstStatusEnum.ENDED.getCode());
        processInstanceService.endProcessInstance(processInstanceId, "end terminate process instance");
    }

    @Override
    public void cancelProcessInstance(String processInstanceId, String deleteReason) {
        runtimeService.setVariable(processInstanceId, ProcessConstant.EXT_PROCESS_TERMINATION_STATUS, EndedProcInstStatusEnum.CANCELLED.getCode());
        processInstanceService.endProcessInstance(processInstanceId, "cancel terminate process instance");
    }

    @Override
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        //方案一
        // 级联删除（运行时数据+历史数据）
        runtimeService.deleteProcessInstance(processInstanceId, "delete terminate process instance");
        // 显式清理历史数据（针对6.0+版本）
        historyService.deleteHistoricProcessInstance(processInstanceId);

    }
}
