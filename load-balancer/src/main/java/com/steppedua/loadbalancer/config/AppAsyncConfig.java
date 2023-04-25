package com.steppedua.loadbalancer.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AppAsyncConfig implements AsyncConfigurer {
    //TODO Сколько надо?
    private static final int CORE_POOL_SIZE_THREADS = Runtime.getRuntime().availableProcessors() / 2;
    //TODO Сколько надо?
    private static final int MAX_POOL_SIZE_THREADS = Runtime.getRuntime().availableProcessors();
    //TODO Сколько надо?
    private static final int QUEUE_CAPACITY = 100;
    private static final String THREAD_NAME_PREFIX = "LoadBalancerExecutor-";

    @Override
    @Bean(name = "threadPoolTaskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE_THREADS);
        executor.setMaxPoolSize(MAX_POOL_SIZE_THREADS);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
