package com.netease.lowcode.xxljob.config;

import com.netease.lowcode.annotation.context.LogicContext;
import com.netease.lowcode.xxljob.config.helper.JobConfigHelper;
import com.netease.lowcode.xxljob.model.JobHandlerInterfaceModel;
import com.netease.lowcode.xxljob.task.InterfaceJobHandler;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class JobHandlerConfig implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private XxlJobConfig xxlJobConfig;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        // 配置执行器参数
        String adminAddress = xxlJobConfig.getAdminAddress();
        if (StringUtils.isNotEmpty(adminAddress)) {
            executor.setAdminAddresses(adminAddress.replace("\u200B", ""));
        }
        if (StringUtils.isNotEmpty(xxlJobConfig.getAppName())) {
            executor.setAppname(xxlJobConfig.getAppName());
        }
        if (StringUtils.isNotEmpty(xxlJobConfig.getAddress())) {
            executor.setAddress(xxlJobConfig.getAddress());
        }
        if (StringUtils.isNotEmpty(xxlJobConfig.getAccessToken())) {
            executor.setAccessToken(xxlJobConfig.getAccessToken());
        }
        if (StringUtils.isNotEmpty(xxlJobConfig.getIp())) {
            executor.setIp(xxlJobConfig.getIp());
        }
        if (StringUtils.isNotEmpty(xxlJobConfig.getLogPath())) {
            executor.setLogPath(xxlJobConfig.getLogPath());
        }
        if (StringUtils.isNotEmpty(xxlJobConfig.getLogRetentionDays())) {
            executor.setLogRetentionDays(Integer.parseInt(xxlJobConfig.getLogRetentionDays()));
        }
        if (StringUtils.isNotEmpty(xxlJobConfig.getPort())) {
            executor.setPort(Integer.parseInt(xxlJobConfig.getPort()));
        }
        return executor;
    }

    /**
     * 初始化并注册所有JobHandler
     */
//    @PostConstruct
    public void initJobHandlers() {
        try {
            // 获取xxlJob注解和logic的关系
            List<LogicContext> logicContexts = JobConfigHelper.listJobContext();
            if (CollectionUtils.isEmpty(logicContexts)) {
                return;
            }

            List<JobHandlerInterfaceModel> jobModelList = logicContexts.stream()
                    .filter(e -> {
                        Map<String, Object> annotationProperties = e.getAnnotationProperties();
                        if (MapUtils.isEmpty(annotationProperties)) {
                            return false;
                        }

                        String useAnno = String.valueOf(annotationProperties.get("useAnno"));
                        String jobHandler = String.valueOf(annotationProperties.get("inputText"));
                        // 开启了开关，并设置了JobHandler才被判定开启了定时任务
                        if ("true".equals(useAnno) && StringUtils.isNotEmpty(jobHandler)) {
                            return true;
                        }
                        return false;
                    }).map(e -> {
                        Map<String, Object> annotationProperties = e.getAnnotationProperties();
                        JobHandlerInterfaceModel model = new JobHandlerInterfaceModel();
                        model.setJobHandler(String.valueOf(annotationProperties.get("inputText")));
                        model.setLogicName(e.getLogicName());
                        return model;
                    }).collect(Collectors.toList());

            // 为每个配置创建JobHandler并注册
            for (JobHandlerInterfaceModel config : jobModelList) {
                String handlerName = config.getJobHandler();
                // 创建处理器
                InterfaceJobHandler handler = new InterfaceJobHandler(applicationContext, jobModelList) {
                    @Override
                    public String getHandlerName() {
                        return handlerName;
                    }
                };

                XxlJobSpringExecutor.registJobHandler(handlerName, handler);
                log.info("Successfully registered job handler: {}", handlerName);
            }
        } catch (Exception e) {
            log.error("Failed to initialize job handlers", e);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initJobHandlers();
    }
}