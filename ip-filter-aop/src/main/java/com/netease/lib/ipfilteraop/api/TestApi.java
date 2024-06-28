package com.netease.lib.ipfilteraop.api;


import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TestApi {

    //参数使用LCAP_EXTENSION_LOGGER后日志会显示在平台日志功能中
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

     /**
         * 示例逻辑：相加
         * @param a
         * @param b
         * @return
     */
    @NaslLogic
    public  Integer add(Integer a,Integer b) {
        return a+b;
    }

}
