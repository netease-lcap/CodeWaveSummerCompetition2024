package org.example.productCategory.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
删除产品分类的入参结构
*/
@NaslStructure
public class DeleteProductCategoryDto {

    public String corpid;
    public String userId;
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
                ",\n \"dataId:\"" +"\"" + dataId +"\""+
                "\n}";
    }
}
