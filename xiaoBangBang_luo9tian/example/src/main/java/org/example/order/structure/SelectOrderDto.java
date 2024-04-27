package org.example.order.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
查询合同订单的入参结构
*/
@NaslStructure
public class SelectOrderDto {
    public Long formId;	//表单id
    public String conditions;	//条件集合
    public Integer page;//页码，默认为1
    public Integer pageSize;	///每页数量，默认为20，最大值100
    public String corpid;  //公司id
    public String userId;  //操作人id

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

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
        return "SelectOrderDto{" +
                "formId=" + formId +
                ", conditions='" + conditions + '\'' +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", corpid='" + corpid + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
