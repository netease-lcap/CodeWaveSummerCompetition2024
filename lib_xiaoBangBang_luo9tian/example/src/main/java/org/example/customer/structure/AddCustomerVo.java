package org.example.customer.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*增加客户的返回的数据结构*/
@NaslStructure
public class AddCustomerVo {
    public Integer code;


    public String msg;

    public AddCustomerResult result;
    public String success;
    public int getCode() {
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

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    public AddCustomerResult getResult() {
        return result;
    }

    public void setResult(AddCustomerResult result) {
        this.result = result;
    }
    @Override
    public String toString() {
        return "AddCustomerVo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result='" + result + '\'' +
                ", success='" + success + '\'' +
                '}';
    }
}
