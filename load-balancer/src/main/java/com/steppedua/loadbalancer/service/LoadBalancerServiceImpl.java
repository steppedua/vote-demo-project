package com.steppedua.loadbalancer.service;


import com.steppedua.loadbalancer.config.LoadBalancerConfig;
import com.steppedua.loadbalancer.model.VoteSaveRequestDto;
import com.steppedua.loadbalancer.model.VoteStatisticsResponseDto;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoadBalancerServiceImpl implements LoadBalancerService {
    private static final int N_THREADS = Runtime.getRuntime().availableProcessors();
    private static final int CAPACITY = Runtime.getRuntime().availableProcessors();
    private static final int INITIAL_SERVER_NUMBER = 1;
    private final ThreadPoolExecutor executor;
    private final LoadBalancerConfig loadBalancerConfig;
    private final RestTemplate restTemplate;
    private final AtomicInteger atomicInteger;

    public LoadBalancerServiceImpl(LoadBalancerConfig loadBalancerConfig, RestTemplate restTemplate) {
        this.loadBalancerConfig = loadBalancerConfig;

        executor = new ThreadPoolExecutor(
                N_THREADS,
                N_THREADS,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(CAPACITY)
        );
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        this.restTemplate = restTemplate;
        atomicInteger = new AtomicInteger(INITIAL_SERVER_NUMBER);
    }

    @Override
    public UUID voteSave(@NonNull final VoteSaveRequestDto voteSaveRequestDto) {
        final var submit = executor.submit(
                new VoteSaveTask(restTemplate, loadBalancerConfig, atomicInteger, voteSaveRequestDto)
        );

        //TODO подумать, как реализовать без блокировки
        try {
            return submit.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public VoteStatisticsResponseDto getVoteStatistics() {
        final var submit = executor.submit(
                new VoteStatisticsTask(restTemplate, loadBalancerConfig, atomicInteger)
        );

        //TODO подумать, как реализовать без блокировки
        try {
            return submit.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
