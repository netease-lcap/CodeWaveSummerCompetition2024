package com.netease.lib.tasks.scheduled;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @Author baojz
 * @Date: 2024/05/06
 * @description 任务调度配置类
 */
@Configuration
public class TaskSchedulerConfig {


    /**
     * 线程池任务调度器
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(3);
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
        return threadPoolTaskScheduler;
    }

    /**
     * 自定义任务注册器
     */
    @Bean
    public ScheduledTaskRegistrar taskRegistrar(ThreadPoolTaskScheduler scheduler) {
        ScheduledTaskRegistrar taskRegistrar = new ScheduledTaskRegistrar(scheduler);
        //可以初始化一个监控任务
        return taskRegistrar;
    }
}