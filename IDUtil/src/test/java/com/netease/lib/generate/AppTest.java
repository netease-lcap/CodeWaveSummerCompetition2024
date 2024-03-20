package com.netease.lib.generate;

import junit.framework.TestCase;


public class AppTest 
    extends TestCase
{
    public void testGenerateRandomString() {
        String randomString = IDUtil.generateRandomString(10, true, true);
        System.out.println(randomString);
    }
}
