package com.netease.lowcode.lib.logic.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.ArrayList;
import java.util.List;

@NaslStructure
public class MyInitiatedTaskPage {
    /**
     * 数据
     */
    public List<MyInitiatedTask> list = new ArrayList<>();
    /**
     * 总条数
     */
    public Long total;

    public MyInitiatedTaskPage() {
    }


    public MyInitiatedTaskPage(List<MyInitiatedTask> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public static MyInitiatedTaskPage of(List<MyInitiatedTask> list, Long total) {
        return new MyInitiatedTaskPage(list, total);
    }

    public static MyInitiatedTaskPage empty() {
        return new MyInitiatedTaskPage(new ArrayList<>(), 0L);
    }

    public List<MyInitiatedTask> getList() {
        return list;
    }

    public void setList(List<MyInitiatedTask> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
