package com.netease.lowcode.lib.logic.impl;

import com.netease.codewave.nasl.java.definition.error.ProcessError;
import com.netease.codewave.process.api.domain.*;
import com.netease.codewave.process.api.service.ProcessInstanceService;
import com.netease.codewave.process.api.service.ProcessRecordService;
import com.netease.codewave.process.api.service.TaskInstanceService;
import com.netease.codewave.process.service.ProcessUserIdentityService;
import com.netease.lowcode.lib.annotation.ReconfirmAuthorization;
import com.netease.lowcode.lib.enums.EndedProcInstStatusEnum;
import com.netease.lowcode.lib.enums.ProcInstStatusEnum;
import com.netease.lowcode.lib.logic.structure.CurrNode;
import com.netease.lowcode.lib.logic.structure.ProcInstInfo;
import com.netease.lowcode.lib.logic.structure.ProcInstInfoPage;
import com.netease.lowcode.lib.logic.structure.ProcessUser;
import com.netease.lowcode.lib.service.ExtProcessUserIdentityService;
import com.netease.lowcode.lib.service.api.ExtProcessInstanceService;
import com.netease.lowcode.lib.util.ProcessConstant;
import com.netease.lowcode.lib.util.ProcessUserUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.common.engine.impl.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExtProcessInstanceSearchLogicService {
    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    private static final long SIZE_MAX = Integer.MAX_VALUE;
    @Resource
    private ProcessInstanceService processInstanceService;
    @Resource
    private ProcessRecordService processRecordService;
    @Resource
    private TaskInstanceService taskInstanceService;
    @Resource
    private ExtProcessUserIdentityService extProcessUserIdentityService;
    @Resource
    private ProcessUserIdentityService processUserIdentityService;
    @Resource
    private ExtProcessInstanceService extProcessInstanceService;

    /**
     * 获取当前节点列表
     *
     * @param procInstId 流程实例ID
     * @return 当前节点列表
     */
    public List<CurrNode> getCurrentNodeList(String procInstId) {
        CWRunTaskInstanceQuery query = new CWRunTaskInstanceQuery();
        query.setProcessInstanceId(procInstId);
        query.setPage(1L);
        query.setSize(SIZE_MAX);

        CWPageOf<CWRunTaskInstance> pageOf = taskInstanceService.queryRunTaskInstanceList(query);
        if (CollectionUtil.isNotEmpty(pageOf.getList())) {
            return pageOf.getList().stream()
                    .collect(Collectors.groupingBy(CWRunTaskInstance::getNodeName))
                    .values().stream()
                    .map(lowCodeRunTaskInstances -> {
                        CWRunTaskInstance first = lowCodeRunTaskInstances.get(0);
                        List<ProcessUser> participants = lowCodeRunTaskInstances.stream()
                                .map(CWRunTaskInstance::getCandidateUsers)
                                .flatMap(Collection::stream)
                                .distinct()
                                .map(extProcessUserIdentityService::getUsersByUserName)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        return new CurrNode(first.getNodeTitle(), participants, first.getTaskId());
                    }).collect(Collectors.toList());
        } else {
            CurrNode currNode = new CurrNode();
            currNode.setCurrNodeTitle("结束");
            return Collections.singletonList(currNode);
        }
    }

    /**
     * 分页获取流程实例
     *
     * @param procDefKey
     * @param procInstStartTimeAfter
     * @param procInstStartTimeBefore
     * @param procInstInitiator
     * @param page
     * @param size
     * @param search                  精确匹配
     * @param finished                实例是否完成
     * @return
     */
    @ReconfirmAuthorization
    public ProcInstInfoPage pageProcInstInfo(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                             ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                             Long page, Long size, String search, Boolean finished) {
        CWHistoricProcessInstanceQuery query = new CWHistoricProcessInstanceQuery();
        query.setProcessDefinitionKey(procDefKey);
        query.setStartBy(procInstInitiator);
        query.setStartTimeAfter(procInstStartTimeAfter);
        query.setStartTimeBefore(procInstStartTimeBefore);
        query.setProcessDefinitionTitle(search);
        page = Optional.ofNullable(page).orElse(1L);
        size = Optional.ofNullable(size).orElse(20L);
        query.setPage(page);
        query.setSize(size);
        query.setSort("desc");
        query.setFinished(finished);
        CWPageOf<CWHistoricProcessInstance> historicProcessInstancePageOf = processInstanceService.queryHistoricProcessInstanceList(query);
        return ProcInstInfoPage.of(new ArrayList<>(Optional.ofNullable(historicProcessInstancePageOf.getList())
                .orElse(new ArrayList<>())
                .stream()
                .map(this::toProcInstInfo)
                .collect(Collectors.toList())
        ), historicProcessInstancePageOf.getTotal());
    }

    private ProcInstInfo toProcInstInfo(CWHistoricProcessInstance processInstance) {
        if (ObjectUtils.isEmpty(processInstance)) {
            return null;
        }

        ProcInstInfo procInstInfo = new ProcInstInfo();

        List<CurrNode> currentTaskList = getCurrentNodeList(processInstance.getId());

        procInstInfo.setProcessDefinitionName(processInstance.getProcessDefinitionTitle());
        procInstInfo.setProcessDefinitionTitle(processInstance.getProcessDefinitionTitle());
        procInstInfo.setProcInstId(processInstance.getId());
        procInstInfo.setProcInstInitiator(ProcessUserUtil.toProcessUserFront(processUserIdentityService.getUsersByUserName(processInstance.getStartBy())));
        procInstInfo.setProcInstStartTime(processInstance.getStartTime());
        procInstInfo.setProcInstEndTime(processInstance.getEndTime());
        procInstInfo.setProcInstStatus(Boolean.TRUE.equals(processInstance.getFinished()) ? ProcInstStatusEnum.Approved.getCode() : ProcInstStatusEnum.Approving.getCode());
        procInstInfo.setProcInstCurrNodes(currentTaskList);
        procInstInfo.setProcInstTitle(processInstance.getTitle());
        return procInstInfo;
    }

    @ReconfirmAuthorization
    public String getEndedProcessStatus(String processInstanceId) {
        Optional<CWHistoricProcessInstance> optional = processInstanceService.getHistoricProcessInstanceById(processInstanceId);
        if (!optional.isPresent()) {
            logger.warn("Process instance does not exist!");
            throw new ProcessError("Process instance does not exist!");
        }
        CWHistoricProcessInstance historicProcessInstance = optional.get();
        if (historicProcessInstance.getEndTime() == null) {
            logger.warn("Process has not ended!");
            throw new ProcessError("Process has not ended!");
        }
        Optional<Object> optionalVar = processInstanceService.getProcessVariablesById(processInstanceId, ProcessConstant.EXT_PROCESS_TERMINATION_STATUS);
        if (!optionalVar.isPresent()) {
            return EndedProcInstStatusEnum.ORIGINAL_ENDED.getCode();
        }
        return optionalVar.get().toString();


    }
}
