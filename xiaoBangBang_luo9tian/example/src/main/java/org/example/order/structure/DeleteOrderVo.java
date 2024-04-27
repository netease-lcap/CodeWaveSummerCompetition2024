package org.example.order.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*删除合同订单的返回的数据结构*/
@NaslStructure
public class DeleteOrderVo {
    public Integer code;


    public String msg;

    public DeleteOrderResult result;
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


    public DeleteOrderResult getResult() {
        return result;
    }

    public void setResult(DeleteOrderResult result) {
        this.result = result;
    }
    @Override
    public String toString() {
        return "DeleteOrderVo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result='" + result + '\'' +
                ", success='" + success + '\'' +
                '}';
    }
}
