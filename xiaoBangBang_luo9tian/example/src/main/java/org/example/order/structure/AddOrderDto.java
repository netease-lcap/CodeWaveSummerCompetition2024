package org.example.order.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
增加合同订单的入参结构
*/
@NaslStructure
public class AddOrderDto {
    public String corpid;    //公司Id
    public String userId;    //操作人Id
    public Long formId;  //表单id
    public String dataList;//表单数据

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getDataList() {
        return dataList;
    }

    public void setDataList(String dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "AddOrderDto{" +
                "corpid='" + corpid + '\'' +
                ", userId='" + userId + '\'' +
                ", formId=" + formId +
                ", dataList='" + dataList + '\'' +
                '}';
    }
}
