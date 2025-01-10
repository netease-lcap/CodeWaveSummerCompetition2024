package com.netease.lowcode.pdf.extension.spring;

public class FontCache {

    private String[][] fullName;
    private String fontName;
    private String[][] familyName;
    private String[][] familyName2;

    public String[][] getFullName() {
        return fullName;
    }

    public void setFullName(String[][] fullName) {
        this.fullName = fullName;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String[][] getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String[][] familyName) {
        this.familyName = familyName;
    }

    public String[][] getFamilyName2() {
        return familyName2;
    }

    public void setFamilyName2(String[][] familyName2) {
        this.familyName2 = familyName2;
    }
}
