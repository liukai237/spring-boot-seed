package com.yodinfo.seed.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步线程池配置
 * 可直接注入xxxExecutor，或者通过@Async("xxxExecutor")引用
 */
@Slf4j
@EnableAsync
@Configuration
public class ExecutorConfig implements AsyncConfigurer {

    /**
     * 阻塞因子
     */
    private static final double BLOCKING_COEFFICIENT = 0.9;

    /**
     * CPU核心数
     */
    private static final int CPU_PROCESSORS_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 默认连接池
     * 用于CPU密集型任务
     */
    @Bean
    @Override
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CPU_PROCESSORS_COUNT + 1);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.initialize();
        return executor;
    }

    /**
     * 耗时任务连接池
     * 用于IO密集型任务
     */
    @Bean
    public ThreadPoolTaskExecutor timeConsumingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CPU_PROCESSORS_COUNT);
        executor.setMaxPoolSize((int) (CPU_PROCESSORS_COUNT / (1 - BLOCKING_COEFFICIENT)));
        executor.setQueueCapacity(200);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return ((throwable, method, objects) -> log.error("Occurring an exception during async task [{}] invocation!\n{}", method.getName(), throwable.getMessage()));
    }
}