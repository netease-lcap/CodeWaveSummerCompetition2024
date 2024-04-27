package org.example.productCategory.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
添加产品分类的结果类
*/
@NaslStructure
public class AddProductCategoryResult {
    public Long dataId;

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    @Override
    public String toString() {
        return "AddProductCategoryResult{" +
                ", dataId=" + dataId +
                '}';
    }
}
