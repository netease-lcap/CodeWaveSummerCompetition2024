package com.netease.lowcode.lib.logic.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.ArrayList;
import java.util.List;

@NaslStructure
public class MyPendingTaskPage {
    public MyPendingTaskPage() {
    }

    /**
     * 数据
     */
    public List<MyPendingTask> list = new ArrayList<>();

    /**
     * 总条数
     */
    public Long total;


    public MyPendingTaskPage(List<MyPendingTask> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public static MyPendingTaskPage of(List<MyPendingTask> list, Long total) {
        return new MyPendingTaskPage(list, total);
    }

    public static MyPendingTaskPage empty() {
        return new MyPendingTaskPage(new ArrayList<>(), 0L);
    }

    public List<MyPendingTask> getList() {
        return list;
    }

    public void setList(List<MyPendingTask> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
