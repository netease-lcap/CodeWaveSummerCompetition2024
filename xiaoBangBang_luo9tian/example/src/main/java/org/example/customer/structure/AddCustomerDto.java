package org.example.customer.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
增加客户的入参结构
*/
@NaslStructure
public class AddCustomerDto {

    /*{
        "corpid": "lsj",
            "userId": "xbbTest001",
            "formId": 19274,
            "dataList": {
        "text_1": "apiTest.001",
                "text_17": 310836,
                "text_2": "new",
                "subForm_1": [
        {
            "text_1": "工作",
                "text_2": "1320000000"
        }
    ],
        "address_1": {
            "city": "杭州市",
                    "address": "西兴街道丹枫路126号",
                    "district": "滨江区",
                    "province": "浙江省",
                    "location": {
                "lon": 120.22302,
                        "lat": 30.21842
            }
        },
        "text_4": "潜在客户",
                "text_18": "发现需求",
                "ownerId": [
        "xbbTest001"
    ],
        "text_3": "最终客户",
                "text_5": "企业客户",
                "text_6": "大型",
                "text_7": "服务业",
                "num_1": 5,
                "text_9": "网络推广",
                "text_10": "434",
                "text_11": "34242342",
                "text_8": "中国",
                "text_13": "test",
                "text_16": "xbbTest002"
    }
    }*/
    public String corpid;
    public String userId;
    public String dataList;
    public Long formId;

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

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\"corpid\":" +"\""+ corpid+"\""  +
                ",\n \"userId\":" +"\""+ userId +"\"" +
                ",\n \"dataList\":" + "\""+dataList +"\""+
                ",\n \"formId:\"" +"\"" +formId +"\""+
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
