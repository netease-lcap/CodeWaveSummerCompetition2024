package org.example.productCategory.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/*
查询产品分类的结果类
*/
@NaslStructure
public class SelectProductCategoryResult {
    public String corpid;  //公司id
    public Integer id;//分类id
    public String list;  //产品列表
    public String name; //分类名
    public Integer parenId; //父分类id
    public String router;	//id路由，由根父id逐级到自身id

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParenId() {
        return parenId;
    }

    public void setParenId(Integer parenId) {
        this.parenId = parenId;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    @Override
    public String toString() {
        return "SelectProductCategoryResult{" +
                "corpid='" + corpid + '\'' +
                ", id=" + id +
                ", list='" + list + '\'' +
                ", name='" + name + '\'' +
                ", parenId=" + parenId +
                ", router='" + router + '\'' +
                '}';
    }
}
