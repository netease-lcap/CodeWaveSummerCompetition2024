package com.netease.lowcode.custonapifilter.sign;

import javax.servlet.http.HttpServletRequest;

public interface CheckService {
    /**
     * 安全检查
     *
     * @param request
     * @return
     */
    boolean check(HttpServletRequest request);
}
