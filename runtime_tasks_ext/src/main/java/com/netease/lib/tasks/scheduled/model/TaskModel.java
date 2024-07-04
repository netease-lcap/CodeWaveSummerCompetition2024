package com.netease.lib.tasks.scheduled.model;

import java.util.function.Function;

/**
 * 任务模型
 */
public class TaskModel {
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 逻辑函数
     */
    private Function<String, String> function;
    /**
     * 逻辑名称
     */
    private String logicName;
    /**
     * cron表达式
     */
    private String cron;
    /**
     * 逻辑请求参数
     */
    private String request;
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


    public String getLogicName() {
        return logicName;
    }

    public void setLogicName(String logicName) {
        this.logicName = logicName;
    }

    public Function<String, String> getFunction() {
        return function;
    }

    public void setFunction(Function<String, String> function) {
        this.function = function;
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
