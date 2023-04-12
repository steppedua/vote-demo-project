package com.steppedua.loadbalancer.service;

import com.steppedua.loadbalancer.config.LoadBalancerConfig;
import com.steppedua.loadbalancer.model.VoteStatisticsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class VoteStatisticsTask implements Callable<VoteStatisticsResponseDto> {

    private static final int VALUE = 1;
    private final RestTemplate restTemplate;
    private final LoadBalancerConfig loadBalancerConfig;
    private final AtomicInteger atomicInteger;

    @Override
    public VoteStatisticsResponseDto call() throws Exception {
        if (atomicInteger.get() == loadBalancerConfig.getServerQuantity()) {
            atomicInteger.set(VALUE);
        } else {
            atomicInteger.incrementAndGet();
        }

        final var serverIp = loadBalancerConfig.getServersIp().get(atomicInteger.get());

        //todo заменить на feign client
        final var url = URI.create(loadBalancerConfig.getServerPath() + serverIp + "/api/v1/vote/statistics");
        return restTemplate.getForObject(url, VoteStatisticsResponseDto.class);
    }
}
