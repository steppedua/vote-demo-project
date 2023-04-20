package com.steppedua.loadbalancer.service;


import com.steppedua.loadbalancer.config.LoadBalancerConfig;
import com.steppedua.loadbalancer.model.VoteSaveRequestDto;
import com.steppedua.loadbalancer.model.VoteStatisticsResponseDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class LoadBalancerServiceImpl implements LoadBalancerService {
    private static final int N_THREADS = Runtime.getRuntime().availableProcessors();
    private static final int CAPACITY = Runtime.getRuntime().availableProcessors();
    private static final int INITIAL_SERVER_NUMBER = 1;
    private final ThreadPoolExecutor executor;
    private final LoadBalancerConfig loadBalancerConfig;
    private final RestTemplate restTemplate;
    private final AtomicInteger serverCounter;

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
        serverCounter = new AtomicInteger(INITIAL_SERVER_NUMBER);
    }

    //TODO а нужен ли @Transactional?
    @Override
    public Future<UUID> voteSave(@NonNull final VoteSaveRequestDto voteSaveRequestDto) {
        log.debug("Start voteSave method, input parameters {}", voteSaveRequestDto);
        final var submit = executor.submit(
                new VoteSaveTask(restTemplate, loadBalancerConfig, serverCounter, voteSaveRequestDto)
        );

        log.debug("End voteSave method");
        return submit;
        //TODO подумать где надо сделать shutdown
    }

    //TODO а нужен ли @Transactional?
    @Override
    public Future<VoteStatisticsResponseDto> getVoteStatistics() {
        log.debug("Start getVoteStatistics voteSave method");
        final var submit = executor.submit(
                new VoteStatisticsTask(restTemplate, loadBalancerConfig, serverCounter)
        );

        log.debug("End getVoteStatistics voteSave method");
        return submit;
        //TODO подумать где надо сделать shutdown
    }
}
