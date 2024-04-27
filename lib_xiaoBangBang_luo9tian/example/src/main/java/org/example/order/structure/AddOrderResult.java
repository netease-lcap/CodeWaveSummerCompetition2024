package org.example.order.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
添加合同订单的结果类
*/
@NaslStructure
public class AddOrderResult {
    public Long dataId;

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    @Override
    public String toString() {
        return "AddOrderResult{" +
                ", dataId=" + dataId +
                '}';
    }
}
