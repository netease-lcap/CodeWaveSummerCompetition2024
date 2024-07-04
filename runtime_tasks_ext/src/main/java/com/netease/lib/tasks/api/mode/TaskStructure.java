package com.netease.lib.tasks.api.mode;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * 任务自定义结构
 */
@NaslStructure
public class TaskStructure {
    /**
     * 逻辑名称
     */
    public String logicName;
    /**
     * cron表达式/任务启动时间YYYYMMDDhhmmss
     */
    public String cron;
    /**
     * 逻辑请求参数
     */
    public String request;
    /**
     * 任务id
     */
    public String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    /**
     * 是否暂停,0:否,1:是
     */
    public String isPaused;
    public String getIsPaused() {
        return isPaused;
    }

    public void setIsPaused(String isPaused) {
        this.isPaused = isPaused;
    }
    public String getLogicName() {
        return logicName;
    }

    public void setLogicName(String logicName) {
        this.logicName = logicName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
