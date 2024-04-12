package com.moocsk.lowcode.youdao.translate.model;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * 翻译结果
 */
@NaslStructure
public class Translate {
    /**
     * 原文
     */
    public String query;

    /**
     * 译文
     */
    public String translation;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Override
    public String toString() {
        return "{ query: " + query + ", translation: " + translation + " }";
    }

}
