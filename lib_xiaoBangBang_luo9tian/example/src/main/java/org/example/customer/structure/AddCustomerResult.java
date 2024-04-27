package org.example.customer.structure;

import com.netease.lowcode.core.annotation.NaslStructure;
/*
添加客户的结果类
*/
@NaslStructure
public class AddCustomerResult {
    public Integer code;
    public String msg;
    public Long dataId;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    @Override
    public String toString() {
        return "AddCustomerResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", dataId=" + dataId +
                '}';
    }
}
