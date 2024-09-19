package com.netease.lib.tasks.scheduled;

import java.util.concurrent.ScheduledFuture;

/**
 * @Author baojz
 * @Date: 2024/05/06
 * @description 任务持有者
 */
public class ScheduledTaskHolder {
    /**
     * 逻辑名称
     */
    private String logicName;
    /**
     * 逻辑请求参数
     */
    private String request;
    /**
     * result of scheduling
     */
    private ScheduledFuture<?> scheduledFuture;

    private String cronExpression;

    /**
     * 任务id
     */
    private String taskId;
    /**
     * 是否暂停,0:否,1:是
     */
    private String isPaused;
    public String getIsPaused() {
        return isPaused;
    }

    public void setIsPaused(String isPaused) {
        this.isPaused = isPaused;
    }
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getLogicName() {
        return logicName;
    }

    public void setLogicName(String logicName) {
        this.logicName = logicName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public boolean terminate() {
        return scheduledFuture.isCancelled();
    }
}