package com.netease.lowcode.lib.logic.custom;


import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.lib.logic.impl.ExtProcessDefinitionExtensionLogicService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class ExtProcessDefinitionExtensionLogicApi {

    @Resource
    private ExtProcessDefinitionExtensionLogicService extProcessDefinitionExtensionLogicService;

    /**
     * 获取下一节点
     *
     * @param procDefKey
     * @param taskDefName         节点名称
     * @return
     */
    @NaslLogic
    public Map<String, String> getNextNodeInfo(String procDefKey, String taskDefName) {
        return extProcessDefinitionExtensionLogicService.getNextNodeInfo(procDefKey, taskDefName);
    }

    /**
     * 获取下一节点（根据conditionalFlowIds条件流过滤）
     *
     * @param procDefKey
     * @param taskDefName
     * @param conditionalFlowIds 想要获取的下一节点的分支流id列表
     * @return
     */
    @NaslLogic
    public Map<String, String> getNextNodeInfoByConditionalFlowId(String procDefKey, String taskDefName, List<Integer> conditionalFlowIds) {
        return extProcessDefinitionExtensionLogicService.getNextNodeInfoByConditionalFlowId(procDefKey, taskDefName, conditionalFlowIds);
    }
}
