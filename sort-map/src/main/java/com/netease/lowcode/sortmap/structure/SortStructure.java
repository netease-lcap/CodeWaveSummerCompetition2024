package com.netease.lowcode.sortmap.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * @author xujianping
 * @date 2023/12/26 11:25 上午
 */
@NaslStructure
public class SortStructure {

    public Integer index;
    public String key;
    public String value;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
