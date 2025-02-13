package com.netease.lowcode.lib.filter;


import com.netease.lowcode.lib.api.CheckSafeApi;
import com.netease.lowcode.lib.dto.ReReadableHttpServletRequestWrapper;
import com.netease.lowcode.lib.dto.RequestInfoStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
@Order(-200)
public class UrlSafeFilter extends CommonsRequestLoggingFilter {
    public static final String LOGIC_IDENTIFIER_SEPARATOR = ":";
    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Autowired
    private CustomUrlConfig customUrlConfig;

    private List<String> apiBlackList() {
        //后端依赖库逻辑
        List<String> otherApis = new ArrayList<>();
        if (!StringUtils.isEmpty(customUrlConfig.getFilterUrlList())) {
            try {
                otherApis = Arrays.asList(customUrlConfig.getFilterUrlList().split(","));
            } catch (Exception e) {
                log.warn("filterUrlList配置错误,{}", customUrlConfig.getFilterUrlList());
            }
        }
        return otherApis;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String logicIdentifier = requestURI + LOGIC_IDENTIFIER_SEPARATOR + method;
        //过滤黑名单
        boolean isFilter = false;
        for (String api : apiBlackList()) {
            if (logicIdentifier.startsWith(api)) {
                isFilter = true;
                break;
            }
        }
        if (!isFilter) {
            filterChain.doFilter(request, response);
            return;
        }
        ReReadableHttpServletRequestWrapper requestWrapper = new ReReadableHttpServletRequestWrapper(request);
        ;
        Enumeration<String> headersKey = requestWrapper.getHeaderNames();
        Map<String, String> headersMap = new HashMap();
        if (headersKey.hasMoreElements()) {
            String key = headersKey.nextElement();
            headersMap.put(key, requestWrapper.getHeader(key));
        }
        RequestInfoStructure requestInfoStructure = new RequestInfoStructure(requestWrapper.getBody(), headersMap);
        if (!CheckSafeApi.checkRequestInfo(requestInfoStructure)) {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("CheckSafeApi.checkRequestInfo校验请求拦截");
            return;
        }
        filterChain.doFilter(requestWrapper, response);
    }
}
