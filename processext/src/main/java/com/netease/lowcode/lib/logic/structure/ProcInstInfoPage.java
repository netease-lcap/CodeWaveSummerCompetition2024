package com.netease.lowcode.lib.logic.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.ArrayList;
import java.util.List;

@NaslStructure
public class ProcInstInfoPage {
    public ProcInstInfoPage() {
    }

    /**
     * 数据
     */
    public List<ProcInstInfo> list = new ArrayList<>();

    /**
     * 总条数
     */
    public Long total;


    public ProcInstInfoPage(List<ProcInstInfo> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public static ProcInstInfoPage of(List<ProcInstInfo> list, Long total) {
        return new ProcInstInfoPage(list, total);
    }

    public static ProcInstInfoPage empty() {
        return new ProcInstInfoPage(new ArrayList<>(), 0L);
    }

    public List<ProcInstInfo> getList() {
        return list;
    }

    public void setList(List<ProcInstInfo> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
