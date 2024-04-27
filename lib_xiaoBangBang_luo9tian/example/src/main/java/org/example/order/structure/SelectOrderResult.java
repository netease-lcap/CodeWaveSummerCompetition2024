package org.example.order.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
查询合同订单的结果类
*/
@NaslStructure
public class SelectOrderResult {
    public String list;  //合同订单列表
    public Integer totalCount;//总数据量
    public Integer totalPage;	//总页码数

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "SelectOrderResult{" +
                "list='" + list + '\'' +
                ", totalCount=" + totalCount +
                ", totalPage=" + totalPage +
                '}';
    }
}
