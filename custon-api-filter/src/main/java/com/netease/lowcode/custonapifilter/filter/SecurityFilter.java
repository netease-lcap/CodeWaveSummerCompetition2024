package com.netease.lowcode.custonapifilter.filter;

import com.netease.lowcode.custonapifilter.config.Constants;
import com.netease.lowcode.custonapifilter.sign.CheckService;
import com.netease.lowcode.custonapifilter.sign.dto.ReReadableHttpServletRequestWrapper;
import com.netease.lowcode.custonapifilter.sign.dto.RequestHeader;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Order(-200)
public class SecurityFilter extends CommonsRequestLoggingFilter {
    public static final String LOGIC_IDENTIFIER_SEPARATOR = ":";
    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Autowired
    private CheckService checkService;
    @Autowired
    private CustomFilterConfig customFilterConfig;

    private List<String> apiBlackList() {
        //后端依赖库逻辑
        List<String> otherApis = new ArrayList<>();
        if (!StringUtils.isEmpty(customFilterConfig.getFilterUrlList())) {
            try {
                otherApis = Arrays.asList(customFilterConfig.getFilterUrlList().split(","));
            } catch (Exception e) {
                log.warn("filterUrlList配置错误,{}", customFilterConfig.getFilterUrlList());
            }
        }
        return otherApis;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String requestURI = request.getRequestURI();
            String method = request.getMethod();
            String logicIdentifier = requestURI + LOGIC_IDENTIFIER_SEPARATOR + method;
            //过滤黑名单
            boolean isFilter = false;
            if ("black".equals(customFilterConfig.getFilterType())) {
                for (String api : apiBlackList()) {
                    if (logicIdentifier.startsWith(api)) {
                        isFilter = true;
                        break;
                    }
                }
            } else if ("white".equals(customFilterConfig.getFilterType())) {
                isFilter = true;
                for (String api : apiBlackList()) {
                    if (logicIdentifier.startsWith(api)) {
                        isFilter = false;
                        break;
                    }
                }
            }
            if (!isFilter) {
                filterChain.doFilter(request, response);
                return;
            }
            ReReadableHttpServletRequestWrapper requestWrapper = new ReReadableHttpServletRequestWrapper(request);
            String body = requestWrapper.getBody();
            RequestHeader requestHeader = new RequestHeader(requestWrapper.getHeader(Constants.LIB_SIGN_HEADER_NAME), requestWrapper.getHeader(Constants.LIB_TIMESTAMP_HEADER_NAME), requestWrapper.getHeader(Constants.LIB_NONCE_HEADER_NAME), body);
            if (!checkService.check(requestHeader)) {
                response.setContentType("application/json");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(checkService + "校验请求拦截");
                return;
            }
            filterChain.doFilter(requestWrapper, response);
        } catch (Exception e) {
            log.error("SecurityFilter error", e);
            response.setContentType("application/json");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("SecurityFilter error. 校验请求拦截");
        }
    }
}
