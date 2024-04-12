package com.moocsk.lowcode.baidu.translate.model;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * 翻译结果
 */
@NaslStructure
public class Translate {  
    /**
     * 原文
     */
    public String src;

    /**
     * 译文
     */
    public String dst;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    @Override
    public String toString() {
        return "Translate [src=" + src + ", dst=" + dst + "]";
    }

}
