package com.netease.lowcode.lib.logic;

import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.lib.logic.impl.ExtProcessInstanceHandlingLogicService;
import com.netease.lowcode.lib.logic.impl.ExtProcessInstanceSearchLogicService;
import com.netease.lowcode.lib.logic.structure.ProcInstInfoPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZonedDateTime;

@Service
public class ExtProcessInstanceLogicApi {

    @Resource
    private ExtProcessInstanceHandlingLogicService extProcessInstanceHandlingLogicService;
    @Resource
    private ExtProcessInstanceSearchLogicService extProcessInstanceSearchLogicService;

    /**
     * 启动流程
     *
     * @param data                传入数据
     * @param procDefKey          流程定义Key
     * @param processInstanceName 流程实例名称
     * @return 流程实例ID
     */
    @NaslLogic
    public <T> String launchProcessCustomName(T data, String procDefKey, String processInstanceName) {
        return extProcessInstanceHandlingLogicService.launchProcessCustomName(data, procDefKey, processInstanceName);
    }

    /**
     * 分页获取流程实例（管理员）
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
    @NaslLogic
    public ProcInstInfoPage pageProcInstInfo(String procDefKey, ZonedDateTime procInstStartTimeAfter,
                                             ZonedDateTime procInstStartTimeBefore, String procInstInitiator,
                                             Long page, Long size, String search, Boolean finished) {
        return extProcessInstanceSearchLogicService.pageProcInstInfo(procDefKey, procInstStartTimeAfter, procInstStartTimeBefore, procInstInitiator, page, size, search, finished);
    }

    /**
     * 获取结束的流程的状态
     *
     * @param procInstId
     * @return 流程状态
     * 流程结束前-不支持使用本接口：
     * 暂存和审批中（平台原生）
     * 流程结束后：
     * 平台原生结束（平台原生）ORIGINAL_ENDED、
     * 结束ENDED、取消CANCELLED（扩展）
     * 删除DELETED-报错流程实例不存在
     */
    @NaslLogic
    public String getEndedProcessStatus(String procInstId) {
        return extProcessInstanceSearchLogicService.getEndedProcessStatus(procInstId);
    }
}
