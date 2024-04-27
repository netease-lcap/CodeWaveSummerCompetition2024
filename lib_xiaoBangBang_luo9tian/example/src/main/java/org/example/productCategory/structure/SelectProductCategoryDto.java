package org.example.productCategory.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
查询产品分类的入参结构
*/
@NaslStructure
public class SelectProductCategoryDto {

    public String corpid;  //公司id
    public String userId;  //操作人id
    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "SelectProductCategoryDto{" +
                "corpid='" + corpid + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
