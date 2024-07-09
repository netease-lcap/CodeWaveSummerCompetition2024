package com.netease.lowcode.extension.service;

import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.extension.feign.ExtensionUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static ExtensionUserClient extensionUserClient;

    @NaslLogic
    public static String hello() {
        return extensionUserClient.hello();
    }


    @Autowired
    public void setUserClient(ExtensionUserClient extensionUserClient) {
        UserService.extensionUserClient = extensionUserClient;
    }
}
