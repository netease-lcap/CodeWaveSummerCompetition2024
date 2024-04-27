package org.example.customer.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
删除客户的入参结构
*/
@NaslStructure
public class DeleteCustomerDto {

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
