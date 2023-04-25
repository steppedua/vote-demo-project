package com.steppedua.loadbalancer.service;


import com.steppedua.loadbalancer.config.LoadBalancerConfigurationProperties;
import com.steppedua.loadbalancer.model.VoteSaveRequestDto;
import com.steppedua.loadbalancer.model.VoteStatisticsResponseDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.steppedua.loadbalancer.util.LoadBalancerServerUtil.INITIAL_SERVER_NUMBER;

@Slf4j
@Service
public class LoadBalancerServiceImpl implements LoadBalancerService {
    private final LoadBalancerConfigurationProperties loadBalancerConfigurationProperties;
    private final RestTemplate restTemplate;
    private final AtomicInteger serverCounter;

    public LoadBalancerServiceImpl(LoadBalancerConfigurationProperties loadBalancerConfigurationProperties, RestTemplate restTemplate) {
        this.loadBalancerConfigurationProperties = loadBalancerConfigurationProperties;
        this.restTemplate = restTemplate;
        serverCounter = new AtomicInteger(INITIAL_SERVER_NUMBER);
    }

    //TODO а нужен ли @Transactional?
    @Override
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<UUID> voteSave(@NonNull final VoteSaveRequestDto voteSaveRequestDto) {
        log.debug("Start voteSave method, input parameters {}", voteSaveRequestDto);

        final var uuidResponse = CompletableFuture.supplyAsync(() -> {
                    final var voteSaveTask = new VoteSaveTask(restTemplate, loadBalancerConfigurationProperties, serverCounter, voteSaveRequestDto);
                    return voteSaveTask.voteSaveTask();
                }
        );
        // TODO .exceptionally(); - нужна ли обработка исключений?

        log.debug("End voteSave method");
        return uuidResponse;
    }

    //TODO а нужен ли @Transactional?
    @Override
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<VoteStatisticsResponseDto> getVoteStatistics() {
        log.debug("Start getVoteStatistics voteSave method");

        final var voteStatisticsResponse = CompletableFuture.supplyAsync(() -> {
                    final var voteStatisticsTask = new VoteStatisticsTask(restTemplate, loadBalancerConfigurationProperties, serverCounter);
                    return voteStatisticsTask.voteStatisticsTask();
                }
        );

        log.debug("End getVoteStatistics voteSave method");
        return voteStatisticsResponse;
    }
}
