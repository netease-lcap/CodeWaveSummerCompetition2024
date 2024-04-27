package org.example.order.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
删除合同订单的结果类
*/
@NaslStructure
public class DeleteOrderResult {
    public String errorDataMemo;

    public String getErrorDataMemo() {
        return errorDataMemo;
    }

    public void setErrorDataMemo(String errorDataMemo) {
        this.errorDataMemo = errorDataMemo;
    }

    @Override
    public String toString() {
        return "DeleteOrderResult{" +
                "errorDataMemo='" + errorDataMemo + '\'' +
                '}';
    }
}
