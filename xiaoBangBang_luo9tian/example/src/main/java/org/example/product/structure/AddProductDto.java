package org.example.product.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
增加产品的入参结构
*/
@NaslStructure
public class AddProductDto {
    public String corpid;    //公司Id
    public String userId;    //操作人Id
    public String dataList;  //表单数据
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

    public String getDataList() {
        return dataList;
    }

    public void setDataList(String dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\"corpid\":" +"\""+ corpid+"\""  +
                ",\n \"userId\":" +"\""+ userId +"\"" +
                ",\n \"dataList\":" + "\""+dataList +"\""+
                "\n}";
    }
}
