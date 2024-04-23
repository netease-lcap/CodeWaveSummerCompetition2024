package com.codewave.masking.api;

import org.junit.Assert;
import org.junit.Test;

public class TestDataMaskingApi {

    @Test
    public void testPrefixDataMasking(){
        String maskingChar = "*";
        Assert.assertEquals("*中",DataMaskingApi.prefixMasking("方中", 1, maskingChar, 1));
        Assert.assertEquals("*新",DataMaskingApi.prefixMasking("方中新", 1, maskingChar, 1));
        Assert.assertEquals("*新",DataMaskingApi.prefixMasking("方中新", 1, maskingChar, 1));
        Assert.assertEquals("*",DataMaskingApi.prefixMasking("方中", 0, maskingChar, 1));
        Assert.assertEquals("*",DataMaskingApi.prefixMasking("方中", 0, maskingChar, 1));
        Assert.assertEquals("*方中",DataMaskingApi.prefixMasking("方中", 2, maskingChar, 1));
        // 异常情况 （保留位数超过长度）
        Assert.assertNull(DataMaskingApi.prefixMasking("方中", 3, maskingChar, -1));
        // 异常情况 （待加密字符串为空）
        Assert.assertNull(DataMaskingApi.prefixMasking(null, 1, maskingChar, -1));
        // 异常情况 （待加密字符串为空）
        Assert.assertNull(DataMaskingApi.prefixMasking("", 1, maskingChar, -1));
    }

    @Test
    public void testPrefixDataMaskingLength(){
        String maskingChar = "*";
        Assert.assertEquals("*中",DataMaskingApi.prefixMasking("方中", 1, maskingChar, 1));
        Assert.assertEquals("**新",DataMaskingApi.prefixMasking("方中新", 1, maskingChar, 2));
        Assert.assertEquals("*新",DataMaskingApi.prefixMasking("方中新", 1, maskingChar, 1));
        Assert.assertEquals("****新",DataMaskingApi.prefixMasking("方中新", 1, maskingChar, 4));
        Assert.assertEquals("新",DataMaskingApi.prefixMasking("方中新", 1, maskingChar, 0));
        Assert.assertEquals("**新",DataMaskingApi.prefixMasking("方中新", 1, maskingChar, -1));
    }

    @Test
    public void testSuffixDataMasking(){
        String maskingChar = "*";
        Assert.assertEquals("方*",DataMaskingApi.suffixMasking("方中", 1, maskingChar, 1));
        Assert.assertEquals("方**",DataMaskingApi.suffixMasking("方中新", 1, maskingChar, -1));
        Assert.assertEquals("方*",DataMaskingApi.suffixMasking("方中新", 1, maskingChar, 1));

        Assert.assertEquals("**",DataMaskingApi.suffixMasking("方中", 0, maskingChar, -1));
        Assert.assertEquals("*",DataMaskingApi.suffixMasking("方中", 0, maskingChar, 1));

        Assert.assertEquals("方中",DataMaskingApi.suffixMasking("方中", 2, maskingChar, 0));
        Assert.assertEquals("方中",DataMaskingApi.suffixMasking("方中", 2, maskingChar, 0));

        // 异常情况 （保留位数超过长度）
        Assert.assertNull(DataMaskingApi.suffixMasking("方中", 3, maskingChar, -1));
        // 异常情况 （待加密字符串为空）
        Assert.assertNull(DataMaskingApi.suffixMasking(null, 1, maskingChar, -1));
        // 异常情况 （待加密字符串为空）
        Assert.assertNull(DataMaskingApi.suffixMasking("", 1, maskingChar, -1));
    }

    @Test
    public void testSuffixDataMaskingLength(){
        String maskingChar = "*";
        Assert.assertEquals("方*",DataMaskingApi.suffixMasking("方中", 1, maskingChar, 1));
        Assert.assertEquals("方**",DataMaskingApi.suffixMasking("方中新", 1, maskingChar, 2));
        Assert.assertEquals("方****",DataMaskingApi.suffixMasking("方中新", 1, maskingChar, 4));
        Assert.assertEquals("方中****",DataMaskingApi.suffixMasking("方中新", 2, maskingChar, 4));
        Assert.assertEquals("方中",DataMaskingApi.suffixMasking("方中新", 2, maskingChar, 0));

    }

    @Test
    public void testMiddleSuffixMasking(){
        String maskingChar = "-";
        Assert.assertEquals("code---ve",DataMaskingApi.middleSuffixMasking("codewave", 4, 2, maskingChar, 3));
        Assert.assertEquals("code--ve",DataMaskingApi.middleSuffixMasking("codewave", 4, 2, maskingChar, -1));
        Assert.assertEquals("codeve",DataMaskingApi.middleSuffixMasking("codewave", 4, 2, maskingChar, 0));
        Assert.assertEquals("code-ve",DataMaskingApi.middleSuffixMasking("codewave", 4, 2, maskingChar, 1));
        Assert.assertEquals("cod---ewave",DataMaskingApi.middleSuffixMasking("codewave", 4, 5, maskingChar, 3));
        Assert.assertEquals("cod-ewave",DataMaskingApi.middleSuffixMasking("codewave", 4, 5, maskingChar, -1));
        Assert.assertEquals("cod---ewave",DataMaskingApi.middleSuffixMasking("codewave", 4, 5, maskingChar, 3));
        Assert.assertEquals("123-456789",DataMaskingApi.middleSuffixMasking("123456789", 4, 6, maskingChar, -1));
        Assert.assertEquals("123-456789",DataMaskingApi.middleSuffixMasking("123456789", 3, 6, maskingChar, -1));

    }


    @Test
    public void testSimple(){
        Assert.assertEquals("*wave", DataMaskingApi.simplePrefixMasking("codewave", 4));
        Assert.assertEquals("code*", DataMaskingApi.simpleSuffixMasking("codewave", 4));
        Assert.assertEquals("co*wave", DataMaskingApi.simpleMiddleMasking("codewave", 2,4));
    }
}
