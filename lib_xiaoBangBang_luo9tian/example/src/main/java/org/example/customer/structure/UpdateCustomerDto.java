package org.example.customer.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
更新客户的入参结构
*/
@NaslStructure
public class UpdateCustomerDto {

    public String corpid;
    public String userId;
    public String dataList;
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

    public String getDataList() {
        return dataList;
    }

    public void setDataList(String dataList) {
        this.dataList = dataList;
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
                ",\n \"dataList\":" + "\""+dataList +"\""+
                ",\n \"dataId:\"" +"\"" + dataId +"\""+
                "\n}";
    }
    /*对数据转json和进行空值处理和压缩*/
    /*public String zip(){
        String result="";
        result+="{";
        if(corpid!=null){
            result+="\"corpid\":" +"\""+ corpid+"\",";
        }
        if(userId!=null){
            result+="\"userId\":" +"\""+ userId +"\",";
        }
        if(dataList!=null){
            result+="\"dataList\":" + dataList +",";
        }
        if(formId!=null){
            result+="\"formId\":" +"\"" +formId +"\"";
        }
        if(result.endsWith(",")){
            result=result.substring(0,result.length()-1);
        }
        result+="}";
        return result;
    }*/
}
