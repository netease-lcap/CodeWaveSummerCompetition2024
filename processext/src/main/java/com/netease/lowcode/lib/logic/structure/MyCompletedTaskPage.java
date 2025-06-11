package com.netease.lowcode.lib.logic.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.ArrayList;
import java.util.List;

@NaslStructure
public class MyCompletedTaskPage {
    /**
     * 数据
     */
    public List<MyCompletedTask> list = new ArrayList<>();
    /**
     * 总条数
     */
    public Long total;

    public MyCompletedTaskPage() {
    }


    public MyCompletedTaskPage(List<MyCompletedTask> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public static MyCompletedTaskPage of(List<MyCompletedTask> list, Long total) {
        return new MyCompletedTaskPage(list, total);
    }

    public static MyCompletedTaskPage empty() {
        return new MyCompletedTaskPage(new ArrayList<>(), 0L);
    }

    public List<MyCompletedTask> getList() {
        return list;
    }

    public void setList(List<MyCompletedTask> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
