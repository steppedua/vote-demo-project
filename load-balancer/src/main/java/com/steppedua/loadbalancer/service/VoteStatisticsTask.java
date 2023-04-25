package com.steppedua.loadbalancer.service;

import com.steppedua.loadbalancer.config.LoadBalancerConfigurationProperties;
import com.steppedua.loadbalancer.exception.RestTemplateException;
import com.steppedua.loadbalancer.model.VoteStatisticsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

import static com.steppedua.loadbalancer.util.LoadBalancerServerUtil.INITIAL_SERVER_NUMBER;

@Slf4j
@RequiredArgsConstructor
public class VoteStatisticsTask {
    private final RestTemplate restTemplate;
    private final LoadBalancerConfigurationProperties loadBalancerConfigurationProperties;
    private final AtomicInteger serverCounter;

    public VoteStatisticsResponseDto voteStatisticsTask() {
        if (serverCounter.get() == loadBalancerConfigurationProperties.getServerQuantity()) {
            serverCounter.set(INITIAL_SERVER_NUMBER);
        } else {
            serverCounter.incrementAndGet();
        }

        final var serverIp = loadBalancerConfigurationProperties.getServersIp().get(serverCounter.get());
        log.debug("Parameter serverIp {}", serverIp);

        //TODO заменить на feign client
        final var uri = URI.create("http://" + loadBalancerConfigurationProperties.getServerPath() + ":" + serverIp + "/api/v1/vote/statistics");
        log.debug("Parameter uri {}", uri);

        try {
            return restTemplate.getForObject(uri, VoteStatisticsResponseDto.class);
        } catch (RuntimeException e) {
            log.error("Exception in voteStatisticsTask method with message: {}", e.getMessage());
            throw new RestTemplateException(e.getMessage(), e);
        }
    }
}
