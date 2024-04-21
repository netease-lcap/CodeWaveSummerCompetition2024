package com.codewave.converter;

import com.codewave.converter.api.CurrencyAmountInWordsConverterApi;
import org.junit.Assert;
import org.junit.Test;

public class TestConverter {

    @Test
    public void testRmbToChinese(){
        Assert.assertEquals("零元",CurrencyAmountInWordsConverterApi.convertRMBToChinese("0"));
        Assert.assertEquals("零元",CurrencyAmountInWordsConverterApi.convertRMBToChinese("0.00001"));
        Assert.assertEquals("零元",CurrencyAmountInWordsConverterApi.convertRMBToChinese("0.00"));
        Assert.assertEquals("零元",CurrencyAmountInWordsConverterApi.convertRMBToChinese("0.0"));
        Assert. assertEquals("壹佰元",CurrencyAmountInWordsConverterApi.convertRMBToChinese("100.00"));
        Assert.assertEquals("壹佰元",CurrencyAmountInWordsConverterApi.convertRMBToChinese("100"));
        Assert.assertEquals("壹佰元",CurrencyAmountInWordsConverterApi.convertRMBToChinese("100.0000"));
        Assert.assertEquals("壹佰零壹元零壹分",CurrencyAmountInWordsConverterApi.convertRMBToChinese("101.01"));
        Assert.assertEquals("壹佰元零壹分",CurrencyAmountInWordsConverterApi.convertRMBToChinese("100.01"));
        Assert.assertEquals("壹仟零壹元零壹分",CurrencyAmountInWordsConverterApi.convertRMBToChinese("1001.01"));
        Assert.assertEquals("壹元零壹分",CurrencyAmountInWordsConverterApi.convertRMBToChinese("1.01"));
        Assert.assertEquals("壹分",CurrencyAmountInWordsConverterApi.convertRMBToChinese("0.01"));
        Assert.assertEquals("壹拾亿元",CurrencyAmountInWordsConverterApi.convertRMBToChinese("1000000000.00"));
        Assert.assertEquals("零元",CurrencyAmountInWordsConverterApi.convertRMBToChinese("-0"));
        Assert.assertEquals("零元",CurrencyAmountInWordsConverterApi.convertRMBToChinese("-0.00001"));
        Assert.assertEquals("零元",CurrencyAmountInWordsConverterApi.convertRMBToChinese("-0.00"));
        Assert.assertEquals("零元",CurrencyAmountInWordsConverterApi.convertRMBToChinese("-0.0"));
        Assert.assertEquals("负壹佰元",CurrencyAmountInWordsConverterApi.convertRMBToChinese("-100.00"));

        Assert.assertEquals("",CurrencyAmountInWordsConverterApi.convertRMBToChinese("123456232323323237890.12"));
        Assert.assertEquals("",CurrencyAmountInWordsConverterApi.convertRMBToChinese("12345623232fds sdf"));
        Assert.assertEquals("",CurrencyAmountInWordsConverterApi.convertRMBToChinese(null));
        Assert.assertEquals("",CurrencyAmountInWordsConverterApi.convertRMBToChinese("."));
        Assert.assertEquals("贰角叁分",CurrencyAmountInWordsConverterApi.convertRMBToChinese(".23"));

    }

    @Test
    public void testUsdToChinese(){
        Assert.assertEquals("DOLLARS SEVENTEEN THOUSAND,TWENTY-SIX AND CENTS EIGHT",CurrencyAmountInWordsConverterApi.convertUSDToWords("17026.08"));
        Assert.assertEquals("DOLLARS ONE BILLION,TWO HUNDRED,THIRTY-FOUR MILLION,FIVE HUNDRED,SIXTY-SEVEN THOUSAND,EIGHT HUNDRED,NINETY AND CENTS TWELVE",CurrencyAmountInWordsConverterApi.convertUSDToWords("1234567890.12"));
        Assert.assertEquals("DOLLARS ZERO AND CENTS ONE",CurrencyAmountInWordsConverterApi.convertUSDToWords("0.01"));
        Assert.assertEquals("DOLLARS FIVE THOUSAND",CurrencyAmountInWordsConverterApi.convertUSDToWords("5000.00"));
        Assert.assertEquals("DOLLARS ZERO AND CENTS NINETY-NINE",CurrencyAmountInWordsConverterApi.convertUSDToWords("0.99"));
        Assert.assertEquals("DOLLARS ONE",CurrencyAmountInWordsConverterApi.convertUSDToWords("1"));
        Assert.assertEquals("DOLLARS TWELVE THOUSAND,THREE HUNDRED,FORTY-FIVE AND CENTS SIXTY-SEVEN",CurrencyAmountInWordsConverterApi.convertUSDToWords("12345.67"));
        Assert.assertEquals("DOLLARS ZERO AND CENTS TWO",CurrencyAmountInWordsConverterApi.convertUSDToWords("0.0156"));
        Assert.assertEquals("DOLLARS ZERO AND CENTS ONE",CurrencyAmountInWordsConverterApi.convertUSDToWords("0.014"));
        Assert.assertEquals("",CurrencyAmountInWordsConverterApi.convertUSDToWords("123456232323323237890.12"));
        Assert.assertEquals("",CurrencyAmountInWordsConverterApi.convertUSDToWords("12345623232fds sdf"));
        Assert.assertEquals("",CurrencyAmountInWordsConverterApi.convertUSDToWords(null));
        Assert.assertEquals("",CurrencyAmountInWordsConverterApi.convertUSDToWords("."));
        Assert.assertEquals("DOLLARS ZERO AND CENTS TWENTY-THREE",CurrencyAmountInWordsConverterApi.convertUSDToWords(".23"));

    }
}
