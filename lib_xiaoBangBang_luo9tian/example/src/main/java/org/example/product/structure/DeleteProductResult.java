package org.example.product.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
删除客户的结果类
*/
@NaslStructure
public class DeleteProductResult {
    public String errorDataMemo;

    public String getErrorDataMemo() {
        return errorDataMemo;
    }

    public void setErrorDataMemo(String errorDataMemo) {
        this.errorDataMemo = errorDataMemo;
    }

    @Override
    public String toString() {
        return "DeleteProductResult{" +
                "errorDataMemo='" + errorDataMemo + '\'' +
                '}';
    }
}
