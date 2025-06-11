package com.netease.lowcode.lib.listener;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.netease.codewave.process.api.domain.event.ProcessEvent;
import com.netease.codewave.process.api.domain.event.ProcessEventListener;
import com.netease.codewave.process.api.service.event.ProcessEventListenerManagerService;
import com.netease.lowcode.annotation.helper.provider.OverriddenFrameworkHelper;
import org.flowable.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

@Service
public class ExtProcessListenerService implements ProcessEventListener<ProcessEvent> {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule((Module) new JavaTimeModule());
    }

    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Resource
    public ApplicationContext applicationContext;
    @Resource
    private ProcessEventListenerManagerService processEventListenerManagerService;

    @PostConstruct
    public void register() {
        this.processEventListenerManagerService.registerEventListener(this);
    }

    /**
     * 流程监听事件
     * ProcessStartedEvent：流程启动事件
     * ProcessEndedEvent：流程结束事件
     * ProcessTerminatedEvent：流程终止事件
     * TaskCreatedEvent：任务创建事件
     * TaskClosedEvent：任务关闭事件
     * TaskCancelledEvent：任务取消事件
     * TaskNodeStartEvent：任务节点开始事件
     * TaskNodeEndEvent：任务节点结束事件
     * TaskOperationEvent：任务操作事件
     * TaskApprovedEvent：任务审批通过事件
     * TaskRejectedEvent：任务拒绝通过事件
     * TaskReassignedEvent：任务转派事件
     * TaskAddSignEvent：任务加签事件
     * TaskSubmittedEvent：任务提交事件
     * TaskRevertedEvent：任务回退事件
     * TaskWithdrawnEvent：任务撤回事件
     */
    @Override
    public void onEvent(ProcessEvent processEvent) {
        if (this.applicationContext.containsBean("customProcessOnEventOverriddenLcap_process_extCustomizeService")) {
            Object[] args = {processEvent.getEventType(), writeValueAsString(processEvent)};
            try {
                OverriddenFrameworkHelper.invokeOverriddenMethod0("customProcessOnEvent", "lcap-process-ext", args);
            } catch (Exception e) {
                log.error("onEventOverriddenProcess_extendCustomizeService error", e);
            }
        }
    }

    @Override
    public Class<ProcessEvent> getSupportedEventType() {
        return ProcessEvent.class;
    }

    public String writeValueAsString(Object value) {
        if (value == null) {
            return null;
        }
        try {
            String json = objectMapper.writeValueAsString(value);
            Map<String, Object> map = objectMapper.readValue(json, Map.class);
            String processInstanceId = (String) map.get("procInstId");
            if (processInstanceId != null && !processInstanceId.isEmpty()) {
                //获取实体相关的数据
                Map<String, Object> custom = (Map<String, Object>) applicationContext.getBean(ProcessEngine.class).getRuntimeService().getVariable(processInstanceId, "custom");
                map.put("processData", custom);
                json = objectMapper.writeValueAsString(map);
            }
            return json;
        } catch (Exception e) {
            log.error("Parse data to json error", e);
            return null;
        }
    }
}
