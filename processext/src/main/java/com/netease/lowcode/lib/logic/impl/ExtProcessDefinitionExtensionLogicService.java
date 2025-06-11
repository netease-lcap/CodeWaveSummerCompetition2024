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

    /**
     * 获取流程定义列表信息
     * 未来节点的审批人信息只能根据当前变量获取。若后续节点对变量有变化，当前节点无法获取到未来信息。
     *
     * @param taskId 任务ID
     * @return 流程信息
     */
    @ReconfirmAuthorization
    public List<Element> getProcInstGraphByTaskId(String taskId) {
        Optional<CWHistoricTaskInstance> optional = this.taskInstanceService.getHistoricTaskInstanceById(taskId);
        if (!optional.isPresent()) {
            log.warn("Task does not exist!");
            throw new ProcessExtCommonException("Task does not exist!");
        } else {
            return this.getProcInstGraphByProcInstId(optional.get().getProcessInstanceId());
        }
    }

    /**
     * 获取流程定义列表信息
     * 未来节点的审批人信息只能根据当前变量获取。若后续节点对变量有变化，当前节点无法获取到未来信息。
     *
     * @param procInstId
     * @return 流程信息
     */
    @ReconfirmAuthorization
    public List<Element> getProcInstGraphByProcInstId(String procInstId) {
        try {
            CWProcInstGraphModel cwProcInstGraphModel = processGraphService.getProcInstGraphModelByProcessInstanceId(procInstId);
            //未完成节点获取编辑态设置的审批人信息
            cwProcInstGraphModel.getElements().stream()
                    .filter(element -> !element.getType().equals(com.netease.lowcode.lib.util.ProcessConstant.ELEMENT_EVENT_END))
                    .filter(element -> !element.getType().equals(com.netease.lowcode.lib.util.ProcessConstant.ELEMENT_EVENT_Start))
                    .filter(element -> !element.getType().equals(com.netease.lowcode.lib.util.ProcessConstant.ELEMENT_EVENT_InclusiveGateway))
                    .filter(element -> !element.getType().equals(com.netease.lowcode.lib.util.ProcessConstant.ELEMENT_EVENT_ExclusiveGateway))
                    .filter(element -> !element.isCompleted())
                    .filter(element -> element.getCompleteInfos() == null || element.getCompleteInfos().isEmpty())
                    .forEach(element -> {
                        List<CWCompleteInfoModel> getCompleteInfos = new ArrayList<>();
                        CWCompleteInfoModel cwCompleteInfoModel = new CWCompleteInfoModel();
                        cwCompleteInfoModel.setCompleted(false);
                        cwCompleteInfoModel.setCandidates(getAssigneeList(procInstId, element.getName()));
                        getCompleteInfos.add(cwCompleteInfoModel);
                        element.setCompleteInfos(getCompleteInfos);
                    });
            //固定end节点最后
            CWElementModel end = cwProcInstGraphModel.getElements().stream().filter(element -> element.getType().equals(com.netease.lowcode.lib.util.ProcessConstant.ELEMENT_EVENT_END)).findFirst().get();
            cwProcInstGraphModel.getElements().remove(end);
            List<CWElementModel> cwElementModelList = ElementSort.sortElements(cwProcInstGraphModel.getElements(), cwProcInstGraphModel.getFlows());
            cwElementModelList.add(end);
            cwElementModelList = cwElementModelList.stream().filter(Objects::nonNull).collect(Collectors.toList());
            return toElement(cwElementModelList);
        } catch (Exception e) {
            log.error("Process graph sort error!", e);
            throw e;
        }
    }

    private List<Element> toElement(List<CWElementModel> cwElementModelList) {
        List<Element> elements = new ArrayList<>();
        for (CWElementModel cwElementModel : cwElementModelList) {
            Element element = new Element();
            element.setName(cwElementModel.getName());
            element.setTitle(cwElementModel.getTitle());
            element.setType(cwElementModel.getType());
            element.setCurrent(cwElementModel.isCurrent());
            element.setCompleted(cwElementModel.isCompleted());
            if (cwElementModel.getCompleteInfos() != null && !cwElementModel.getCompleteInfos().isEmpty()) {
                List<CompleteInfo> completeInfos = cwElementModel.getCompleteInfos().stream().map(cwCompleteInfoModel -> {
                    CompleteInfo completeInfo = new CompleteInfo();
                    completeInfo.setCompleteTime(cwCompleteInfoModel.getCompleteTime());
                    completeInfo.setCompleted(cwCompleteInfoModel.isCompleted());
                    if (StringUtils.isNoneBlank(cwCompleteInfoModel.getAssignee())) {
                        com.netease.codewave.process.open.domain.structure.ProcessUser processUserAssignee = processUserIdentityService.getUsersByUserName(cwCompleteInfoModel.getAssignee());
                        completeInfo.setAssignee(ProcessUserUtil.toProcessUserFront(processUserAssignee));
                    }
                    if (com.netease.lowcode.core.util.CollectionUtils.isNotEmpty(cwCompleteInfoModel.getCandidates())) {
                        completeInfo.setCandidates(cwCompleteInfoModel.getCandidates().stream()
                                .map(processUserIdentityService::getUsersByUserName)
                                .map(ProcessUserUtil::toProcessUserFront).collect(Collectors.toList()));
                    }
                    completeInfo.setAddSignTag(cwCompleteInfoModel.getAddSignTag());
                    return completeInfo;
                }).collect(Collectors.toList());
                element.setCompleteInfos(completeInfos);
            }
            elements.add(element);
        }
        return elements;
    }

    private List<String> getAssigneeList(String procInstId, String currentUserTaskNodeKey) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(getProcessDefinitionId(procInstId));
        if (bpmnModel != null) {
            String version = bpmnModel.getMainProcess().getAttributeValue(ProcessConstant.EXTENSION_NAMESPACE, ProcessConstant.KEY_NASL_VERSION);
            try {
                String taskAssigneeServiceName = String.format(TASK_ASSIGNEE_SERVICE_NAME, bpmnModel.getMainProcess().getId(), version, currentUserTaskNodeKey);
                Object service = applicationContext.getBean(taskAssigneeServiceName);
                if (service == null) {
                    return null;
                }
                Method method = service.getClass().getMethod("getAssigneeList", Map.class);
                Map<String, Object> variables = runtimeService.getVariables(procInstId);
                return (List<String>) method.invoke(service, variables);
            } catch (Exception e) {
                log.error("getAssigneeList error", e);
            }
        }
        return null;
    }

    public String getProcessDefinitionId(String processInstanceId) {
        // 根据流程实例ID查询流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (historicProcessInstance != null) {
            return historicProcessInstance.getProcessDefinitionId();
        }
        return null;
    }
}
