package com.netease.lowcode.lib.logic;

import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.lib.logic.impl.ExtCustomVarsLogicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ExtCustomVarsLogicApi {
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Resource
    private ExtCustomVarsLogicService extCustomVarsLogicService;

    /**
     * 获取流程自定义变量
     *
     * @param taskId
     * @param processVariableName
     * @param <T>
     * @return
     */
    @NaslLogic
    public <T> T getProcessVariableByTaskId(String taskId, String processVariableName) {
        return extCustomVarsLogicService.getProcessVariableByTaskId(taskId, processVariableName);
    }

    /**
     * 获取流程自定义变量
     *
     * @param procInstId
     * @param processVariableName
     * @param <T>
     * @return
     */
    @NaslLogic
    public <T> T getProcessVariableByProcInstId(String procInstId, String processVariableName) {
        return extCustomVarsLogicService.getProcessVariableByProcInstId(procInstId, processVariableName);
    }

    /**
     * 更新流程自定义变量
     *
     * @param taskId
     * @param processVariableName
     * @param processVariableValue 类型须与流程定义中的自定义流程变量类型一致
     * @param <T>
     * @return
     */
    @NaslLogic
    public <T> Boolean updateProcessVariableByTaskId(String taskId, String processVariableName, T processVariableValue) {
        return extCustomVarsLogicService.updateProcessVariableByTaskId(taskId, processVariableName, processVariableValue);
    }

    /**
     * 更新流程自定义变量
     *
     * @param procInstId
     * @param processVariableName
     * @param processVariableValue 类型须与流程定义中的自定义流程变量类型一致
     * @param <T>
     * @return
     */
    @NaslLogic
    public <T> Boolean updateProcessVariableByProcInstId(String procInstId, String processVariableName, T processVariableValue) {
        return extCustomVarsLogicService.updateProcessVariableByProcInstId(procInstId, processVariableName, processVariableValue);
    }
}
