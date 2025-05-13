package com.netease.lowcode.custonapifilter.sign;

import com.netease.lowcode.custonapifilter.sign.dto.RequestHeader;

public interface CheckService {
    /**
     * 安全检查
     *
     * @param request
     * @return
     */
    boolean check(RequestHeader request);
}
