package org.example.productCategory.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
增加产品分类的入参结构
*/
@NaslStructure
public class AddProductCategoryDto {
    public String corpid;    //公司Id
    public String userId;    //操作人Id
    public String name;  //分类名称
    public Long parentId;//父分类Id
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "AddProductCategoryDto{" +
                "corpid='" + corpid + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
