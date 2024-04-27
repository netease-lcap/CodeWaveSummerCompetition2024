package org.example.customer.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*更新客户的返回的数据结构*/
@NaslStructure
public class UpdateCustomerVo {
    public Integer code;


    public String msg;

    public UpdateCustomerResult result;
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


    public UpdateCustomerResult getResult() {
        return result;
    }

    public void setResult(UpdateCustomerResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UpdateCustomerVo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result='" + result + '\'' +
                ", success='" + success + '\'' +
                '}';
    }
}
