package com.netease.lowcode.lib.officetopdf.web.filter;

import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.lowcode.lib.officetopdf.dto.ApiReturn;
import com.netease.lowcode.lib.officetopdf.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * 逻辑鉴权过滤器
 *
 * @author sys
 * @since 2.19
 */
public class ExpandTransferLogicAuthFilter implements Filter {
    public static final String LOGIC_IDENTIFIER_SEPARATOR = ":";
    private final Logger log = LoggerFactory.getLogger(ExpandTransferLogicAuthFilter.class);
    /**
     * /expand/transfer/*逻辑鉴权开关，0关闭，1开启
     */
    private String expandLogicAuthFlag;
    /**
     * lcap auth的tokenName
     */
    private String tokenName = "authorization";
    /**
     * lcap auth的secret
     */
    private volatile String secret;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ExpandTransferLogicAuthFilter() {
        System.out.println(1);
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getExpandLogicAuthFlag() {
        return expandLogicAuthFlag;
    }

    public void setExpandLogicAuthFlag(String expandLogicAuthFlag) {
        this.expandLogicAuthFlag = expandLogicAuthFlag;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if ("0".equals(this.getExpandLogicAuthFlag())) {
            log.debug("逻辑鉴权开关关闭");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        // 1. 识别到当前的请求路径
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        String logicIdentifier = requestURI + LOGIC_IDENTIFIER_SEPARATOR + method;
        log.info("当前请求的逻辑标识: {}", logicIdentifier);
        try {
            Map userInfo = getSession(httpRequest);
            if (Objects.isNull(userInfo)) {
                log.warn("当前用户未登录 逻辑{} 鉴权不通过", logicIdentifier);
                handleReturn(httpResponse, "当前用户未登录，逻辑鉴权不通过");
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } catch (Exception e) {
            log.error("逻辑鉴权异常", e);
            handleReturn(httpResponse, "逻辑鉴权异常");
        }
    }

    public Map<String, String> getSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(tokenName)) {
                Map<String, Claim> claimMap = new JwtUtil(secret).decryptToken(cookies[i].getValue());
                Map<String, String> jwtTokenMap = new HashMap<>(claimMap.size());
                Iterator<String> keys = claimMap.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    jwtTokenMap.put(key, claimMap.get(key).asString());
                }
                return jwtTokenMap;
            }
        }
        return null;
    }

    private void handleReturn(HttpServletResponse response, String returnMessage) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ApiReturn<String> unAuthReturn = ApiReturn.of("", HttpStatus.UNAUTHORIZED.value(), returnMessage);
        response.getWriter().write(objectMapper.writeValueAsString(unAuthReturn));
    }
}
