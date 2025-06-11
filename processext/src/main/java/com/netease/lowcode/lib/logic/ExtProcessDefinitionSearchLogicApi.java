package com.netease.lowcode.lib.logic;

import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.lib.exception.ProcessExtCommonException;
import com.netease.lowcode.lib.logic.impl.ExtProcessDefinitionExtensionLogicService;
import com.netease.lowcode.lib.logic.structure.graph.Element;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExtProcessDefinitionSearchLogicApi {

    @Resource
    private ExtProcessDefinitionExtensionLogicService extProcessDefinitionExtensionLogicService;

    /**
     * 获取流程定义列表信息
     * 未来节点的审批人信息只能根据当前变量获取。若后续节点对变量有变化，当前节点无法获取到未来信息。
     *
     * @param procInstId 流程实例ID
     * @return 流程信息
     */
    @NaslLogic
    public List<Element> getProcInstGraphByProcInstId(String procInstId)  {
        return extProcessDefinitionExtensionLogicService.getProcInstGraphByProcInstId(procInstId);
    }

    /**
     * 获取流程定义列表信息
     * 未来节点的审批人信息只能根据当前变量获取。若后续节点对变量有变化，当前节点无法获取到未来信息。
     *
     * @param taskId 任务ID
     * @return 流程信息
     */
    @NaslLogic
    public List<Element> getProcInstGraphByTaskId(String taskId)  {
        return extProcessDefinitionExtensionLogicService.getProcInstGraphByTaskId(taskId);
    }
}
