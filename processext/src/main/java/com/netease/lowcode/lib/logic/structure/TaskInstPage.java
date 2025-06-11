package com.netease.lowcode.lib.logic.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务实例详情
 */
@NaslStructure
public class TaskInstPage {

    /**
     * 数据
     */
    public List<TaskInst> list = new ArrayList<>();
    /**
     * 总条数
     */
    public Long total;

    public TaskInstPage() {
    }

    public TaskInstPage(List<TaskInst> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public static TaskInstPage of(List<TaskInst> list, Long total) {
        return new TaskInstPage(list, total);
    }


    public List<TaskInst> getList() {
        return list;
    }

    public void setList(List<TaskInst> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
