package com.example.demo.api;


import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestApi {
    private static final Logger log = LoggerFactory.getLogger(TestApi.class);

    /**
     * 示例逻辑：相加
     * @param a
     * @param b
     * @return
     */
    @NaslLogic
    public  static  Integer add(Integer a,Integer b) {
        return a+b;
    }

}
