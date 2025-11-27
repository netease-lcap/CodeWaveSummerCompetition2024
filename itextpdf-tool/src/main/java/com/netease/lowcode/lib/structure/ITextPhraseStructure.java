package com.netease.lowcode.lib.structure;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class ITextPhraseStructure {

    /**
     * 行间距（固定值）
     */
    public Double fixedLeading;
    /**
     * 行间距（倍数）
     */
    public Double multipliedLeading;

    public Double getFixedLeading() {
        return fixedLeading;
    }

    public void setFixedLeading(Double fixedLeading) {
        this.fixedLeading = fixedLeading;
    }

    public Double getMultipliedLeading() {
        return multipliedLeading;
    }

    public void setMultipliedLeading(Double multipliedLeading) {
        this.multipliedLeading = multipliedLeading;
    }
}
