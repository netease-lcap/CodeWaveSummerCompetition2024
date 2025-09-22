package com.netease.lowcode.xxljob.model;

public class JobHandlerInterfaceModel {
    private String logicName;      // 接口名称
    private String jobHandler;     // xxlJob handler

    public String getLogicName() {
        return logicName;
    }

    public void setLogicName(String logicName) {
        this.logicName = logicName;
    }

    public String getJobHandler() {
        return jobHandler;
    }

    public void setJobHandler(String jobHandler) {
        this.jobHandler = jobHandler;
    }
}
