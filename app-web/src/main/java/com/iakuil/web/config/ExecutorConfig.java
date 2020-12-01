package com.iakuil.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池及异步配置
 * <p>可直接注入xxxExecutor，或者通过@Async("xxxExecutor")引用</p>
 */
@Slf4j
@EnableAsync
@Configuration
public class ExecutorConfig implements AsyncConfigurer {

    /**
     * 阻塞因子
     */
    private static final double BLOCKING_FACTOR = 0.9;

    /**
     * CPU核心数
     * 极个别情况，比如虚拟机，该方法获取的CPU核心数不准确会造成启动失败。
     */
    private static final int CPU_PROCESSORS_COUNT = Runtime.getRuntime().availableProcessors();


    /**
     * 默认连接池
     * 用于CPU密集型任务
     */
    @Bean
    public ThreadPoolTaskExecutor defaultTaskExecutor() {
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
        executor.setMaxPoolSize((int) (CPU_PROCESSORS_COUNT / (1 - BLOCKING_FACTOR)));
        executor.setQueueCapacity(200);
        executor.initialize();
        return executor;
    }

    @Override
    public TaskExecutor getAsyncExecutor() {
        return defaultTaskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return ((throwable, method, objects) -> log.error("Occurring an exception during async task [{}] invocation!\n{}", method.getName(), throwable.getMessage()));
    }
}