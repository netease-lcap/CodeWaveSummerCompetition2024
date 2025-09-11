package com.netease.lowcode.lib.service.api.impl;

import com.netease.codewave.process.api.domain.CWProcessRecord;
import com.netease.codewave.process.api.service.TaskInstanceService;
import com.netease.codewave.process.command.AddProcessRecordCmd;
import com.netease.codewave.process.service.api.ProcessRecordServiceImpl;
import com.netease.codewave.process.util.ProcessUtil;
import com.netease.lowcode.lib.process.command.ExtAddProcessRecordCmd;
import org.flowable.engine.ManagementService;
import org.flowable.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ExtProcessRecordServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(ProcessRecordServiceImpl.class);
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_NODE_OPERATION = "nodeOperation";
    private static final String KEY_NODE_OPERATION_DISPLAY_TEXT = "nodeOperationDisplayText";
    private static final String KEY_NODE_NAME = "nodeName";
    private static final String KEY_NODE_TITLE = "nodeTitle";
    @Resource
    private TaskService taskService;
    @Resource
    private ManagementService managementService;
    @Resource
    private TaskInstanceService taskInstanceService;

    public void addProcessRecord(CWProcessRecord processRecord, Boolean isShow) {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            Map<String, String> extendRecordDataMap = new HashMap<>();

            extendRecordDataMap.put(KEY_COMMENT, processRecord.getNodeOperationComment());
            extendRecordDataMap.put(KEY_NODE_OPERATION, processRecord.getNodeOperation());
            extendRecordDataMap.put(KEY_NODE_OPERATION_DISPLAY_TEXT, processRecord.getNodeOperationDisplayText());
            extendRecordDataMap.put(KEY_NODE_NAME, processRecord.getNodeName());
            extendRecordDataMap.put(KEY_NODE_TITLE, processRecord.getNodeTitle());
            if (isShow) {
                managementService.executeCommand(
                        new AddProcessRecordCmd(
                                processRecord.getProcessInstanceId(),
                                processRecord.getTaskId(),
                                processRecord.getUserName(),
                                ProcessUtil.ZonedDateTimeToDate(processRecord.getRecordCreatedTime()),
                                processRecord.getAction(),
                                ProcessUtil.writeValueAsString(extendRecordDataMap),
                                processRecord.getSnapshotData()));
            } else {
                managementService.executeCommand(
                        new ExtAddProcessRecordCmd(
                                processRecord.getProcessInstanceId(),
                                processRecord.getTaskId(),
                                processRecord.getUserName(),
                                ProcessUtil.ZonedDateTimeToDate(processRecord.getRecordCreatedTime()),
                                processRecord.getAction(),
                                ProcessUtil.writeValueAsString(extendRecordDataMap),
                                processRecord.getSnapshotData()));
            }
            logger.info("add process record ,processInstanceId: {}, taskId:{}, nodeName:{}, nodeTitle:{}, userName: {}, action:{}, "
                    , processRecord.getProcessInstanceId(), processRecord.getTaskId(), processRecord.getUserName(), processRecord.getNodeTitle(), processRecord.getUserName(), processRecord.getAction());
            return true;
        });
        try {
            future.get();
        } catch (Exception e) {
            logger.error("Failed to save process record. event:{}", processRecord, e);
        }
    }

}
