package com.steppedua.loadbalancer.service;

import com.steppedua.loadbalancer.config.LoadBalancerConfig;
import com.steppedua.loadbalancer.model.VoteStatisticsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class VoteStatisticsTask implements Callable<VoteStatisticsResponseDto> {

    private static final int INITIAL_SERVER_NUMBER = 1;
    private final RestTemplate restTemplate;
    private final LoadBalancerConfig loadBalancerConfig;
    private final AtomicInteger serverCounter;

    @Override
    public VoteStatisticsResponseDto call() {
        if (serverCounter.get() == loadBalancerConfig.getServerQuantity()) {
            serverCounter.set(INITIAL_SERVER_NUMBER);
        } else {
            serverCounter.incrementAndGet();
        }

        final var serverIp = loadBalancerConfig.getServersIp().get(serverCounter.get());
        log.debug("Parameter serverIp {}", serverIp);

        //TODO заменить на feign client
        final var uri = URI.create("http://" + loadBalancerConfig.getServerPath() + ":" + serverIp + "/api/v1/vote/statistics");
        log.debug("Parameter uri {}", uri);

        try {
            return restTemplate.getForObject(uri, VoteStatisticsResponseDto.class);
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }
}
