package com.netease.lowcode.lib.logic.impl;

import com.netease.codewave.process.api.domain.CWProcessDefinition;
import com.netease.codewave.process.api.service.ProcessDefinitionService;
import com.netease.codewave.process.open.logic.ProcessHandlingLogicService;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.lib.annotation.ReconfirmAuthorization;
import com.netease.lowcode.lib.exception.ProcessExtCommonException;
import com.netease.lowcode.lib.logic.structure.ProcessUser;
import com.netease.lowcode.lib.service.ExtProcessUserIdentityService;
import com.netease.lowcode.lib.util.ProcessUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ExtProcessInstanceHandlingLogicService {

    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Resource
    private ProcessDefinitionService processDefinitionService;
    @Resource
    private ExtProcessUserIdentityService extProcessUserIdentityService;

    @Resource
    private ProcessHandlingLogicService processHandlingLogicService;

    /**
     * 启动流程
     *
     * @param data                传入数据
     * @param procDefKey          流程定义Key
     * @param processInstanceName 流程实例名称
     * @return 流程实例ID
     */
    @ReconfirmAuthorization
    public <T> String launchProcessCustomName(T data, String procDefKey, String processInstanceName) {
        if (StringUtils.isBlank(procDefKey)) {
            log.warn("The processDefUniqueKey cannot be empty!");
            throw new ProcessExtCommonException("The processDefUniqueKey cannot be empty!");
        }
        if (data == null) {
            log.error("The data cannot be empty!");
            throw new ProcessExtCommonException("The data cannot be empty!");
        }

        ProcessUser startUser = extProcessUserIdentityService.getCurrentUserInfo();

        if (ObjectUtils.isEmpty(startUser)) {
            log.warn("The current user cannot be empty!");
            throw new ProcessExtCommonException("The current user cannot be empty!");
        }
        if (StringUtils.isBlank(processInstanceName)) {
            Optional<CWProcessDefinition> optional = processDefinitionService.getActiveProcessDefinitionByKey(procDefKey);
            if (!optional.isPresent()) {
                throw new ProcessExtCommonException("The processDefUniqueKey does not exist!");
            }
            processInstanceName = optional.get().getTitle() + "-" +
                    (StringUtils.isNotBlank(startUser.getDisplayName()) ? startUser.getDisplayName() : startUser.getUserName())
                    + "-" + LocalDate.now();
        }

        return processDefinitionService.launchProcess(procDefKey, startUser.getUserName(), processInstanceName, ProcessUtil.convertValueToMap(data));
    }

    /**
     * 同意任务
     *
     * @param data    传入数据
     * @param taskId  任务ID
     * @param comment 审批意见
     * @param <T>
     * @return
     */
    @NaslLogic
    public <T> Boolean approveTaskExcludeKey(T data, String taskId, String comment) {
        processHandlingLogicService.approveTask(data, null, taskId, comment);
        return true;
    }

    /**
     * 拒绝任务
     *
     * @param data    传入数据
     * @param taskId  任务ID
     * @param comment 审批意见
     */
    @NaslLogic
    public <T> Boolean rejectTaskExcludeKey(T data, String taskId, String comment) {
        processHandlingLogicService.rejectTask(data, null, taskId, comment);
        return true;
    }

    /**
     * 提交任务
     *
     * @param data    传入数据
     * @param taskId  任务ID
     * @param comment 审批意见
     */
    @NaslLogic
    public <T> Boolean submitTaskExcludeKey(T data, String taskId, String comment) {
        processHandlingLogicService.submitTask(data, null, taskId, comment);
        return true;
    }
}