package com.steppedua.loadbalancer.service;

import com.steppedua.loadbalancer.config.LoadBalancerConfigurationProperties;
import com.steppedua.loadbalancer.exception.RestTemplateException;
import com.steppedua.loadbalancer.model.VoteStatisticsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class VoteStatisticsTask {
    private final RestTemplate restTemplate;
    private final LoadBalancerConfigurationProperties loadBalancerConfigurationProperties;
    private final AtomicInteger serverCounter;

    public VoteStatisticsResponseDto voteStatisticsTask() {
        final var serverNumber = serverCounter.incrementAndGet() % loadBalancerConfigurationProperties.getServerQuantity();

        final var serverIp = loadBalancerConfigurationProperties.getServersIp().get(serverNumber);
        log.debug("Parameter serverIp {}", serverIp);

        final var uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(loadBalancerConfigurationProperties.getServerPath())
                .port(serverIp)
                .path("/api/v1/vote/statistics")
                .build()
                .toUri();
        log.debug("Parameter uri {}", uri);

        //TODO заменить на feign client
        try {
            final var statisticsResponse = restTemplate.getForObject(uri, VoteStatisticsResponseDto.class);
            log.debug("Parameter statisticsResponse {} from serverIp {}", statisticsResponse, serverIp);

            return statisticsResponse;
        } catch (RuntimeException e) {
            log.error("Exception in voteStatisticsTask method with message: {}", e.getMessage());
            throw new RestTemplateException(e.getMessage(), e);
        }
    }
}
