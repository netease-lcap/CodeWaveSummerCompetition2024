package com.codewave.masking.api;

import org.junit.Assert;
import org.junit.Test;

public class TestDataMaskingApiUse {

    @Test
    public void testDataMaskingApiUse(){
        String maskingChar = "*";

        // 姓名脱敏 脱敏规则一：显示姓名中的第一个字，其它用*号代替。
        Assert.assertEquals("方*",DataMaskingApi.suffixMasking("方钟", 1, maskingChar, -1));
        Assert.assertEquals("方**",DataMaskingApi.suffixMasking("方大钟", 1, maskingChar, -1));
        // 姓名脱敏 脱敏规则二：显示姓名中的第一个和最后一个字，其它用*号代替。
        Assert.assertEquals("方*钟",DataMaskingApi.middleSuffixMasking("方钟", 1, 1,  maskingChar, -1));
        Assert.assertEquals("方*钟",DataMaskingApi.middleSuffixMasking("方大钟", 1, 1,  maskingChar, -1));
        // 证件号码脱敏 对固定位数进行脱敏， 显示前2位和后2位，其它用*号代替。
        Assert.assertEquals("36**************23",DataMaskingApi.middleSuffixMasking("362322199702130023", 2, 2,  maskingChar, -1));
        // 手机号码脱敏 显示前3位+****+后4位。
        Assert.assertEquals("157****6113",DataMaskingApi.middleSuffixMasking("15709626113", 3, 4,  maskingChar, -1));
        // 银行卡卡号脱敏 显示前3位+ *（实际位数）+后4位
        Assert.assertEquals("232********3432",DataMaskingApi.middleSuffixMasking("232323432423432", 3, 4,  maskingChar, -1));
    }

}
