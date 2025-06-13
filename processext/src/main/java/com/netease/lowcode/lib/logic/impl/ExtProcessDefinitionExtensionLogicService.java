package com.netease.lowcode.lib.logic.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.codewave.process.api.domain.CWHistoricTaskInstance;
import com.netease.codewave.process.api.domain.graph.CWCompleteInfoModel;
import com.netease.codewave.process.api.domain.graph.CWElementModel;
import com.netease.codewave.process.api.domain.graph.CWProcInstGraphModel;
import com.netease.codewave.process.api.service.ProcessGraphService;
import com.netease.codewave.process.api.service.TaskInstanceService;
import com.netease.codewave.process.service.ProcessUserIdentityService;
import com.netease.codewave.process.util.ProcessConstant;
import com.netease.lowcode.lib.annotation.ReconfirmAuthorization;
import com.netease.lowcode.lib.exception.ProcessExtCommonException;
import com.netease.lowcode.lib.logic.structure.graph.CompleteInfo;
import com.netease.lowcode.lib.logic.structure.graph.Element;
import com.netease.lowcode.lib.util.ElementSort;
import com.netease.lowcode.lib.util.ProcessUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.*;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExtProcessDefinitionExtensionLogicService {

    //参数使用LCAP_EXTENSION_LOGGER后日志会显示在平台日志功能中
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    private static final String TASK_ASSIGNEE_SERVICE_NAME = "%sVersion%s%sAssigneeService";
    @Autowired
    public ApplicationContext applicationContext;
    @Resource
    private RepositoryService repositoryService;

    @Resource
    private ProcessGraphService processGraphService;
    @Resource
    private TaskInstanceService taskInstanceService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private ProcessUserIdentityService processUserIdentityService;
    @Resource
    private HistoryService historyService;

    @PostConstruct
    public void init() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
        } catch (Exception e) {
            log.error("init config error", e);
        }
    }

    /**
     * 获取下一节点
     *
     * @param procDefKey
     * @return
     */
    @ReconfirmAuthorization
    public Map<String, String> getNextNodeInfo(String procDefKey, String taskDefName) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(procDefKey)
                .latestVersion()
                .singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        if (bpmnModel != null) {
            return getNextNodeNamesByPreviousNodeName(bpmnModel, taskDefName, null);
        }
        return null;
    }

    /**
     * 获取下一节点（根据conditionalFlowIds条件流过滤）
     *
     * @param procDefKey
     * @return
     */
    @ReconfirmAuthorization
    public Map<String, String> getNextNodeInfoByConditionalFlowId(String procDefKey, String taskDefName, List<Integer> conditionalFlowIds) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(procDefKey)
                .latestVersion()
                .singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        if (bpmnModel != null) {
            return getNextNodeNamesByPreviousNodeName(bpmnModel, taskDefName, conditionalFlowIds);
        }
        return null;
    }

    public Map<String, String> getNextNodeNamesByPreviousNodeName(BpmnModel bpmnModel, String previousNodeName, List<Integer> conditionalFlowIds) {
        Map<String, FlowElement> flowElementMap = bpmnModel.getProcesses().get(0).getFlowElementMap();
        FlowElement previousFlowElement = flowElementMap.get(previousNodeName);

        if (previousFlowElement == null) {
            throw new IllegalArgumentException("Previous node with name " + previousNodeName + " not found.");
        }
        Map<String, String> nextNodes = new HashMap<>();
        Collection<FlowElement> flowElements = bpmnModel.getProcesses().get(0).getFlowElements();
        // 使用一个队列来进行广度优先搜索
        Queue<String> queue = new LinkedList<>();
        queue.add(previousFlowElement.getId());
        Integer conditionalFlowIdAllow = 0;
        int level = 0;
        while (!queue.isEmpty()) {
            level++;
            //第一层是previousNodeName到第一个网关，不需要判断conditionalFlowId
            //第二层是网关到节点或第二个网关，需要判断conditionalFlowId
            if (level > 1 && !CollectionUtils.isEmpty(conditionalFlowIds) && conditionalFlowIds.size() > level - 2) {
                conditionalFlowIdAllow = conditionalFlowIds.get(level - 2);
            }
            String currentId = queue.poll();
            for (FlowElement flowElement : flowElements) {
                if (flowElement instanceof SequenceFlow) {
                    SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                    if (sequenceFlow.getSourceRef().equals(currentId)) {
                        if (conditionalFlowIdAllow > 0 && sequenceFlow.getId().startsWith("ConditionalFlow")) {
                            String conditionalFlowIdStr = sequenceFlow.getId().replace("ConditionalFlow", "").replace("_DefaultFlow", "");
                            Integer conditionalFlowIdCurrent = Integer.valueOf(conditionalFlowIdStr);
                            if (!conditionalFlowIdCurrent.equals(conditionalFlowIdAllow)) {
                                continue;
                            }
                        }
                        FlowElement targetFlowElement = flowElementMap.get(sequenceFlow.getTargetRef());
                        if (targetFlowElement instanceof UserTask) {
                            if (nextNodes.get(targetFlowElement.getId()) == null) {
                                nextNodes.put(targetFlowElement.getId(), targetFlowElement.getName());
                            }
                        } else if (targetFlowElement instanceof EndEvent) {
                            if (nextNodes.get(targetFlowElement.getId()) == null) {
                                nextNodes.put(targetFlowElement.getId(), "结束");
                            }
                        } else {
                            queue.add(targetFlowElement.getId());
                        }
                    }
                }
            }
            conditionalFlowIdAllow = 0;
        }
        return nextNodes;
    }

}
