package com.netease.lowcode.dubbo.dubbo.generic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DubboGenericUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Resource
    private List<ApplicationConfig> applicationConfigs;
    @Resource
    private List<RegistryConfig> registryConfigs;

    /**
     * 获取通用服务
     *
     * @param interfaceName 接口名称
     * @param version       版本
     * @return 泛化调用服务
     */
    private GenericService getGenericService(String interfaceName, String version, String referenceUrl) {
        if (CollectionUtils.isEmpty(applicationConfigs) || CollectionUtils.isEmpty(registryConfigs)) {
            LOGGER.warn("Dubbo config may not exist.");
        }
        ReferenceConfig<GenericService> genericService = new ReferenceConfig<>();
        genericService.setRegistries(registryConfigs);
        genericService.setApplication(CollectionUtils.isEmpty(applicationConfigs) ? null : applicationConfigs.get(0));
        genericService.setInterface(interfaceName);
        genericService.setVersion(version);
        genericService.setRetries(0);
        genericService.setGeneric(true);
        genericService.setCheck(false);
        if (!StringUtils.isEmpty(referenceUrl)) {
            genericService.setUrl(referenceUrl);
        }
        return ReferenceConfigCache.getCache().get(genericService);
    }


    /**
     * rpc调用
     *
     * @param interfaceName 接口名称
     * @param version       接口版本
     * @param methodName    方法名称
     * @param parameterList 参数信息
     * @param referenceUrl  dubbo直连地址（可空）
     * @return 接口返回
     */
    @NaslLogic
    public String invoke(String interfaceName, String version, String methodName, ParameterList parameterList, String referenceUrl) {
        GenericService genericService = getGenericService(interfaceName, version, referenceUrl);
        try {
            return OBJECT_MAPPER.writeValueAsString(genericService.$invoke(methodName,
                    parameterList.getParameterParameter().stream().map(Parameter::getType).toArray(String[]::new),
                    parameterList.getParameterParameter().stream().map(Parameter::getArg).toArray()));
        } catch (Exception e) {
            LOGGER.error("An unknown exception occurred", e);
            throw new RuntimeException(e);
        }
    }

}
