package com.netease.lowcode.lib.logic.impl;

import com.netease.codewave.process.api.domain.CWHistoricProcessInstance;
import com.netease.codewave.process.api.domain.CWHistoricTaskInstance;
import com.netease.codewave.process.api.domain.CWProcessRecord;
import com.netease.codewave.process.api.domain.CWTaskOperationPermission;
import com.netease.codewave.process.api.service.TaskDefinitionService;
import com.netease.codewave.process.api.service.TaskInstanceService;
import com.netease.codewave.process.util.ProcessUtil;
import com.netease.lowcode.lib.exception.ProcessExtCommonException;
import com.netease.lowcode.lib.service.api.impl.ExtProcessRecordServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class ExtProcessHandlingLogicBaseService {
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Resource
    private TaskInstanceService taskInstanceService;

    @Resource
    private TaskDefinitionService taskDefinitionService;
    @Resource
    private ExtProcessRecordServiceImpl extProcessRecordService;

    protected void addTaskRecord(CWHistoricTaskInstance taskInstance, String userName, String action, Map<String, Object> variable,
                                 String comment, Boolean isShow, Boolean isCheckIdeOperationPermission) {
        addTaskRecord(taskInstance, userName, action, variable, comment, ZonedDateTime.now(), isShow, isCheckIdeOperationPermission);
    }

    protected void addProcessRecord(CWHistoricProcessInstance processInstance, String userName, String action, Map<String, Object> variable,
                                 String comment, Boolean isShow, Boolean isCheckIdeOperationPermission) {
        addProcessRecord(processInstance, userName, action, variable, comment, ZonedDateTime.now(), isShow, isCheckIdeOperationPermission);
    }

    /**
     * 添加审批记录
     *
     * @param taskInstance
     * @param userName
     * @param action
     * @param variable
     * @param comment
     * @param recordCreatedTime
     * @param isShow                        是否在已办任务和审批记录中展示
     * @param isCheckIdeOperationPermission 若ide中可配置节点的操作权限，则传true。否则传false
     */
    private void addTaskRecord(CWHistoricTaskInstance taskInstance, String userName, String action, Map<String, Object> variable,
                               String comment, ZonedDateTime recordCreatedTime, Boolean isShow, Boolean isCheckIdeOperationPermission) {
        if (taskInstance == null) {
            log.error("The taskInstance cannot be empty!");
            throw new ProcessExtCommonException("The taskId cannot be empty!");
        }
        if (recordCreatedTime == null) {
            log.error("The recordCreatedTime cannot be empty!");
            throw new ProcessExtCommonException("The recordCreatedTime cannot be empty!");
        }
        CWTaskOperationPermission operationPermission = new CWTaskOperationPermission();

        if (isCheckIdeOperationPermission) {
            Optional<CWTaskOperationPermission> optional = taskDefinitionService.getOperationPermission(taskInstance.getProcessDefinitionId(), taskInstance.getNodeName(), action);

            if (!optional.isPresent()) {
                log.error("The operation permission does not exist!");
                throw new ProcessExtCommonException("The operation permission does not exist!");
            }

            operationPermission = optional.get();
        } else {
            operationPermission.setName(action);
        }
        String snapshotData = ProcessUtil.writeValueAsString(variable);
        CWProcessRecord processRecord = new CWProcessRecord();
        processRecord.setProcessInstanceId(taskInstance.getProcessInstanceId());
        processRecord.setTaskId(taskInstance.getTaskId());
        processRecord.setUserName(userName);
        processRecord.setRecordCreatedTime(recordCreatedTime);
        processRecord.setAction(action);

        processRecord.setNodeOperationComment(comment);
        processRecord.setNodeOperation(operationPermission.getName());
        processRecord.setNodeOperationDisplayText(operationPermission.getDisplayText());

        processRecord.setNodeName(taskInstance.getNodeName());
        processRecord.setNodeTitle(taskInstance.getNodeTitle());
        processRecord.setSnapshotData(snapshotData);

        extProcessRecordService.addProcessRecord(processRecord, isShow);
    }

    /**
     * 添加流程操作记录
     *
     * @param processInstance
     * @param userName
     * @param action
     * @param variable
     * @param comment
     * @param recordCreatedTime
     * @param isShow                        是否在已办任务和审批记录中展示
     * @param isCheckIdeOperationPermission 若ide中可配置节点的操作权限，则传true。否则传false
     */
    private void addProcessRecord(CWHistoricProcessInstance processInstance, String userName, String action, Map<String, Object> variable,
                                  String comment, ZonedDateTime recordCreatedTime, Boolean isShow, Boolean isCheckIdeOperationPermission) {
        if (processInstance == null) {
            log.error("The processInstance cannot be empty!");
            throw new ProcessExtCommonException("The processInstanceId cannot be empty!");
        }
        if (recordCreatedTime == null) {
            log.error("The recordCreatedTime cannot be empty!");
            throw new ProcessExtCommonException("The recordCreatedTime cannot be empty!");
        }

        String snapshotData = ProcessUtil.writeValueAsString(variable);
        CWProcessRecord processRecord = new CWProcessRecord();
        processRecord.setProcessInstanceId(processInstance.getId());
        processRecord.setUserName(userName);
        processRecord.setRecordCreatedTime(recordCreatedTime);
        processRecord.setAction(action);
        processRecord.setSnapshotData(snapshotData);

        extProcessRecordService.addProcessRecord(processRecord, isShow);
    }

}
