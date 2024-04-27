package org.example.productCategory.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*更新产品分类的返回的数据结构*/
@NaslStructure
public class UpdateProductCategoryVo {
    public Integer code;


    public String msg;

    public UpdateProductCategoryResult result;
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


    public UpdateProductCategoryResult getResult() {
        return result;
    }

    public void setResult(UpdateProductCategoryResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UpdateProductCategoryVo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result='" + result + '\'' +
                ", success='" + success + '\'' +
                '}';
    }
}
