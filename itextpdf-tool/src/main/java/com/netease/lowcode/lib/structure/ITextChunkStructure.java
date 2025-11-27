package com.netease.lowcode.lib.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class ITextChunkStructure {
    /**
     * 设置字符间距
     */
    public Double characterSpacing;
    /**
     * 添加超链接
     */
    public String anchor;
    /**
     * 设置单词间距（仅对空格有效）
     */
    public Double wordSpacing;
    /**
     * 设置背景色
     */
    public String backgroundColor;

    public Double getCharacterSpacing() {
        return characterSpacing;
    }

    public void setCharacterSpacing(Double characterSpacing) {
        this.characterSpacing = characterSpacing;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public Double getWordSpacing() {
        return wordSpacing;
    }

    public void setWordSpacing(Double wordSpacing) {
        this.wordSpacing = wordSpacing;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
