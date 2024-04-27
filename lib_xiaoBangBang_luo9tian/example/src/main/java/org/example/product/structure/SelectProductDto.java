package org.example.product.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
查询产品的入参结构
*/
@NaslStructure
public class SelectProductDto {

    public String corpid;  //公司id
    public String userId;  //操作人id
    public String condition;  //条件集合
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
        return "SelectProductDto{" +
                "corpid='" + corpid + '\'' +
                ", userId='" + userId + '\'' +
                ", condition=" + condition +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
