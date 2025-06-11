package com.netease.lowcode.lib.logic.impl;

import com.netease.codewave.process.api.domain.CWHistoricTaskInstance;
import com.netease.codewave.process.api.service.TaskInstanceService;
import com.netease.codewave.process.util.ProcessConstant;
import com.netease.lowcode.lib.annotation.ReconfirmAuthorization;
import com.netease.lowcode.lib.exception.ProcessExtCommonException;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

@Service
public class ExtCustomVarsLogicService {
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private TaskInstanceService taskInstanceService;

    private Map<String, Object> getProcessVariable(String procInstId) {
        return (Map<String, Object>) applicationContext.getBean(ProcessEngine.class)
                .getRuntimeService().getVariable(procInstId, ProcessConstant.KEY_PROCESS_VARIABLE);
    }

    private void updateProcessVariableByProcInstId(String procInstId, Map<String, Object> processVariable) {
        RuntimeService runtimeService = applicationContext.getBean(ProcessEngine.class)
                .getRuntimeService();
        runtimeService.setVariable(procInstId, ProcessConstant.KEY_PROCESS_VARIABLE, processVariable);
    }

    @ReconfirmAuthorization
    public <T> T getProcessVariableByProcInstId(String procInstId, String processVariableName) {
        RuntimeService runtimeService = applicationContext.getBean(ProcessEngine.class)
                .getRuntimeService();
        Map<String, Object> processVariable = (Map<String, Object>) runtimeService.getVariable(procInstId, ProcessConstant.KEY_PROCESS_VARIABLE);
        return (T) processVariable.get(processVariableName);
    }

    @ReconfirmAuthorization
    public <T> T getProcessVariableByTaskId(String taskId, String processVariableName) {
        Optional<CWHistoricTaskInstance> optional = this.taskInstanceService.getHistoricTaskInstanceById(taskId);
        if (!optional.isPresent()) {
            log.warn("Task does not exist!");
            throw new ProcessExtCommonException("Task does not exist!");
        } else {
            Object object = this.getProcessVariableByProcInstId(optional.get().getProcessInstanceId(), processVariableName);
            return (T) object;
        }
    }

    @ReconfirmAuthorization
    public <T> Boolean updateProcessVariableByTaskId(String taskId, String processVariableName, T processVariableValue) {
        Optional<CWHistoricTaskInstance> optional = this.taskInstanceService.getHistoricTaskInstanceById(taskId);
        if (!optional.isPresent()) {
            log.warn("Task does not exist!");
            throw new ProcessExtCommonException("Task does not exist!");
        } else {
            Map<String, Object> oldProcessVariable = this.getProcessVariable(optional.get().getProcessInstanceId());
            oldProcessVariable.put(processVariableName, processVariableValue);
            this.updateProcessVariableByProcInstId(optional.get().getProcessInstanceId(), oldProcessVariable);
        }
        return true;
    }


    @ReconfirmAuthorization
    public <T> Boolean updateProcessVariableByProcInstId(String procInstId, String processVariableName, T processVariableValue) {
        Map<String, Object> oldProcessVariable = this.getProcessVariable(procInstId);
        oldProcessVariable.put(processVariableName, processVariableValue);
        this.updateProcessVariableByProcInstId(procInstId, oldProcessVariable);
        return true;
    }
}
