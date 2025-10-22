package com.netease.lib.tasks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncExecutorConfig {

    @Resource(name = "libraryThreadPoolConfig")
    private ThreadPoolConfig threadPoolConfig;

    @Bean(name = "libraryCommonTaskExecutor")
    public Executor commonTaskExecutor() {
        // Spring 默认配置是核心线程数大小为1，最大线程容量大小不受限制，队列容量也不受限制。
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(Integer.parseInt(threadPoolConfig.getCorePoolSize()));
        // 最大线程数
        executor.setMaxPoolSize(Integer.parseInt(threadPoolConfig.getMaxPoolSize()));
        // 队列大小
        executor.setQueueCapacity(Integer.parseInt(threadPoolConfig.getQueueCapacity()));
        //核心线程等待销毁时间
        executor.setKeepAliveSeconds(Integer.parseInt(threadPoolConfig.getKeepAliveSeconds()));
        // 当最大池已满时，此策略保证不会丢失任务请求，但是可能会影响应用程序整体性能。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadNamePrefix("common-pool");
        // 传递request
        executor.setTaskDecorator(runnable -> {
            RequestAttributes context = RequestContextHolder.getRequestAttributes();
            return () -> {
                try {
                    if (context != null) {
                        RequestContextHolder.setRequestAttributes(context);
                    }
                    runnable.run();
                } finally {
                    if (context != null) {
                        RequestContextHolder.resetRequestAttributes();
                    }
                }
            };
        });
        executor.initialize();
        return executor;
    }
}