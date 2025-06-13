package com.netease.http.web.api;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.springframework.stereotype.Component;

@Component
public class HttpExtOverriddenLogicService {
    /**
     * 自定义校验逻辑
     *
     * @return
     */
    @NaslLogic(override = true)
    public Boolean customCheck() {
        return true;
    }

}
