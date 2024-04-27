package org.example.order.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
更新合同订单的入参结构
*/
@NaslStructure
public class UpdateOrderDto {

    public String corpid;
    public String userId;
    public String dataList;
    public Long dataId;

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

    public String getDataList() {
        return dataList;
    }

    public void setDataList(String dataList) {
        this.dataList = dataList;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    @Override
    public String toString() {
        return "UpdateOrderDto{" +
                "corpid='" + corpid + '\'' +
                ", userId='" + userId + '\'' +
                ", dataList='" + dataList + '\'' +
                ", dataId=" + dataId +
                '}';
    }
}
