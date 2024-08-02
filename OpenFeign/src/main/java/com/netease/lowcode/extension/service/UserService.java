package com.netease.lowcode.extension.service;

import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.extension.feign.ExtensionUserClient;
import com.netease.lowcode.extension.feign.ExtensionUserClientWithFallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static ExtensionUserClient extensionUserClient;
    private static ExtensionUserClientWithFallback extensionUserClientWithFallback;

    /**
     * 普通接口调用
     * 当服务不可用时直接异常
     *
     * @return
     */
    @NaslLogic
    public static String hello() {
        return extensionUserClient.hello();
    }

    /**
     * 开启熔断器的接口调用
     * 当调用服务不可用时将触发熔断降级
     *
     * @return
     */
    @NaslLogic
    public static String helloWithFallback() {
        return extensionUserClientWithFallback.hello();
    }

    @Autowired
    public void setUserClient(ExtensionUserClient extensionUserClient) {
        UserService.extensionUserClient = extensionUserClient;
    }
    @Autowired
    public void setExtensionUserClientWithFallback(ExtensionUserClientWithFallback extensionUserClientWithFallback){
        UserService.extensionUserClientWithFallback = extensionUserClientWithFallback;
    }
}
