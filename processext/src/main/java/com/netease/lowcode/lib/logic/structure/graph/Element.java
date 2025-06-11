package com.netease.lowcode.lib.logic.structure.graph;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

/**
 * @author zhuzaishao
 * @date 2024/12/3 15:40
 * @description
 */

@NaslStructure
public class Element {
    /**
     * 是否当前活跃节点
     */
    public Boolean current;
    /**
     * 任务是否完成
     */
    public Boolean completed;
    public String name;
    public String title;
    public String type;
    public List<CompleteInfo> completeInfos;

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CompleteInfo> getCompleteInfos() {
        return completeInfos;
    }

    public void setCompleteInfos(List<CompleteInfo> completeInfos) {
        this.completeInfos = completeInfos;
    }

}
