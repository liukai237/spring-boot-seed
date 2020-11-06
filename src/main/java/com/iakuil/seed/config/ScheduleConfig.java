package com.iakuil.seed.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 定时任务开关及线程池配置
 * <p>scheduling.enable=true开启定时任务</p>
 */
@Slf4j
@Configuration
@EnableScheduling
@Conditional(ScheduleConfig.SchedulerCondition.class)
public class ScheduleConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(schedulerExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler schedulerExecutor() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("task-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }

    public static class SchedulerCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            boolean turnOn = Boolean.parseBoolean(context.getEnvironment().getProperty("scheduling.enabled"));
            log.info("当前工程定时任务[{}]!", turnOn ? "已开启" : "未开启");
            return turnOn;
        }
    }
}
