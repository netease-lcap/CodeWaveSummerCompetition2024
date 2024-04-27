package org.example.productCategory.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
删除客户分类的结果类
*/
@NaslStructure
public class DeleteProductCategoryResult {
    public String errorDataMemo;

    public String getErrorDataMemo() {
        return errorDataMemo;
    }

    public void setErrorDataMemo(String errorDataMemo) {
        this.errorDataMemo = errorDataMemo;
    }

    @Override
    public String toString() {
        return "DeleteProductCategoryResult{" +
                "errorDataMemo='" + errorDataMemo + '\'' +
                '}';
    }
}
