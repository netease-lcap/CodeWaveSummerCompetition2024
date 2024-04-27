package org.example.customer.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
查询客户的入参结构
*/
@NaslStructure
public class SelectCustomerDto {

    public String corpid;  //公司id
    public String userId;  //操作人id
    public String condition;  //条件集合
    public Long formId;      //	表单id
    public Integer isPublic;	//是否公海客户
    public Integer del;   //  0:客户列表 1:回收站数据，默认为0
    public Integer page;	//	页码，默认为1
    public Integer pageSize;  //每页数量，默认为20，最大值100

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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
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

    @Override
    public String toString() {
        return "SelectCustomerDto{" +
                "corpid='" + corpid + '\'' +
                ", userId='" + userId + '\'' +
                ", condition=" + condition +
                ", formId=" + formId +
                ", isPublic=" + isPublic +
                ", del=" + del +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
