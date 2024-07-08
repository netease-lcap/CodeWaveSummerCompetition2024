package com.netease.lib.ipfilteraop.handler;

import com.netease.lib.ipfilteraop.annotation.IpLogicAnnotation;
import com.netease.lib.ipfilteraop.config.LogicIpFilterConfiguration;
import com.netease.lib.ipfilteraop.util.IpUtils;
import com.netease.lowcode.annotation.LCAPAnnotation;
import com.netease.lowcode.annotation.context.LogicContext;
import com.netease.lowcode.annotation.exception.LCAPAnnotationLogicHandlerException;
import com.netease.lowcode.annotation.handler.LCAPAnnotationHandlerAdvise;
import com.netease.lowcode.annotation.handler.LCAPLogicAnnotationHandler;
import com.netease.lowcode.annotation.helper.provider.OverriddenFrameworkHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class IpLogicAnnotationHandler implements LCAPLogicAnnotationHandler<IpLogicAnnotation> {
    private static final Logger log = LoggerFactory.getLogger(IpLogicAnnotationHandler.class);

    private LogicIpFilterConfiguration logicIpFilterConfiguration;

    public IpLogicAnnotationHandler() {
        this.logicIpFilterConfiguration =
                (LogicIpFilterConfiguration) OverriddenFrameworkHelper.getBean("logicIpFilterConfiguration");
    }


    /**
     * 声明了切点位置。
     * 框架层承诺：会在这个方法的返回值所代表的时机，对this.handle方法进行调用，以进行切面处理。
     * handle是一个抽象方法，开发者可以自由实现
     *
     * @return LCAPAnnotationHandlerAdvise 调用this.handle方法的时机，
     * BEFORE代表逻辑翻译出的Service被调用前，AFTER代表被调用后。
     */
    @Override
    public LCAPAnnotationHandlerAdvise[] advises() {
        return new LCAPAnnotationHandlerAdvise[]{LCAPAnnotationHandlerAdvise.BEFORE}; // 这是一个拦截类注解的案例，所以时机是BEFORE
    }

    /**
     * 绑定的注解
     */
    @Override
    public Class<? extends LCAPAnnotation> consume() {
        return IpLogicAnnotation.class; // 由于这是IpLogicAnnotation的处理器，所以此处也必须返回它
    }

    /**
     * 执行顺序，order越小，这个对象的handle在同一advise时机下，相比别的handler越先执行。
     * order相同时不保证执行顺序
     */
    @Override
    public Integer order() {
        return LCAPLogicAnnotationHandler.super.order();
    }

    /**
     * 处理方法
     */
    @Override
    public Object handle(Object[] args, Object result, LogicContext<IpLogicAnnotation> context) throws LCAPAnnotationLogicHandlerException {
        String clientIp = getClientIp();
        log.info("Client IP: {}", clientIp);
//        testOverride();
        String currentTime = getCurrentTime(); // Implement getCurrentTime() method
        if (isIpAllowed(clientIp) && !isIpForbid(clientIp) && !isTimeForbidden(currentTime)) {
            return result;
        } else {
            // 处理未授权访问
            throw new LCAPAnnotationLogicHandlerException("IP地址被禁止的时间段内访问");
        }
    }

    private void testOverride() {
        try {
            String userId = (String) OverriddenFrameworkHelper.invokeOverriddenMethod0(
                    "getUserId", // NaslLogic的方法名
                    "ip-filter-aop", // 本后端依赖库的artifactId
                    null // NaslLogic
            );
            log.info("Overridden NASL Logic, userId={}", userId);
        } catch (Exception e) {
            log.error("fail to invoke Overridden NASL Logic, args={}", "getUserId", e);
        }
    }

    private boolean isTimeForbidden(String currentTime) {
        if (!StringUtils.isEmpty(logicIpFilterConfiguration.getForbidTimeStart())
                && !StringUtils.isEmpty(logicIpFilterConfiguration.getForbidTimeEnd())) {
            LocalTime current = LocalTime.parse(currentTime);
            LocalTime start = LocalTime.parse(logicIpFilterConfiguration.getForbidTimeStart());
            LocalTime end = LocalTime.parse(logicIpFilterConfiguration.getForbidTimeEnd());
            return !current.isBefore(start) && !current.isAfter(end);
        }
        return false;
    }

    private boolean isIpForbid(String ip) {
        if (!StringUtils.isEmpty(logicIpFilterConfiguration.getForbidIp())) {
            String[] forbidIpAddresses = logicIpFilterConfiguration.getForbidIp().split(";"); // 假设 forbid 的格式为 "ip1;ip2;ip3"
            for (String forbiddenIp : forbidIpAddresses) {
                if (forbiddenIp.trim().equals(ip)) {
                    return true; // IP 在黑名单中
                }
            }
            return false; // IP 不在黑名单中
        }
        return false;
    }

    /**
     * 如果clientIp为空，允许所有ip访问
     *
     * @param clientIp
     * @return
     */
    private boolean isIpAllowed(String clientIp) {
        if (!StringUtils.isEmpty(logicIpFilterConfiguration.getAllowedIp())) {
            List<String> allowedIpList = Arrays.asList(logicIpFilterConfiguration.getAllowedIp().split(";"));
            return allowedIpList.contains(clientIp);
        } else {
            return true;
        }
    }

    private String getCurrentTime() {
        LocalTime currentTime = LocalTime.now();
        return currentTime.toString();
    }

    private String getClientIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ipAddress = IpUtils.getIpAddr(request);
        return ipAddress;
    }

    /**
     * 上报方法   // 下文详细说明
     */
    @Override
    public Boolean report(List<LogicContext<IpLogicAnnotation>> logicContextList) {
        return true; // 如果不需要使用"上报"机制，直接return即可
    }
}
