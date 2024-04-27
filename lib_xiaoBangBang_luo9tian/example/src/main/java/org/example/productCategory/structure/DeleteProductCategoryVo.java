package org.example.productCategory.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*删除产品分类的返回的数据结构*/
@NaslStructure
public class DeleteProductCategoryVo {
    public Integer code;


    public String msg;

    public DeleteProductCategoryResult result;
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


    public DeleteProductCategoryResult getResult() {
        return result;
    }

    public void setResult(DeleteProductCategoryResult result) {
        this.result = result;
    }
    @Override
    public String toString() {
        return "DeleteProductCategoryVo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result='" + result + '\'' +
                ", success='" + success + '\'' +
                '}';
    }
}
