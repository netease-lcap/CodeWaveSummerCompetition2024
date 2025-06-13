package com.netease.lowcode.lib.logic.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.ArrayList;
import java.util.List;

@NaslStructure
public class MyCCTaskPage {
    /**
     * 数据
     */
    public List<MyCCTask> list = new ArrayList<>();
    /**
     * 总条数
     */
    public Long total;

    public MyCCTaskPage() {
    }


    public MyCCTaskPage(List<MyCCTask> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public static MyCCTaskPage of(List<MyCCTask> list, Long total) {
        return new MyCCTaskPage(list, total);
    }

    public static MyCCTaskPage empty() {
        return new MyCCTaskPage(new ArrayList<>(), 0L);
    }

    public List<MyCCTask> getList() {
        return list;
    }

    public void setList(List<MyCCTask> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
