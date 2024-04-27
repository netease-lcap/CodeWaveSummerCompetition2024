package org.example.product.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*更新产品的返回的数据结构*/
@NaslStructure
public class UpdateProductVo {
    public Integer code;


    public String msg;

    public UpdateProductResult result;
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


    public UpdateProductResult getResult() {
        return result;
    }

    public void setResult(UpdateProductResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UpdateProductVo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result='" + result + '\'' +
                ", success='" + success + '\'' +
                '}';
    }
}
