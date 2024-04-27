package org.example.productCategory.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
更新产品分类的入参结构
*/
@NaslStructure
public class UpdateProductCategoryDto {

    public String corpid;
    public String userId;
    public String name;
    public Long dataId;

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

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\"corpid\":" +"\""+ corpid+"\""  +
                ",\n \"userId\":" +"\""+ userId +"\"" +
                ",\n \"name\":" + "\""+ name +"\""+
                ",\n \"dataId:\"" +"\"" + dataId +"\""+
                "\n}";
    }
}
