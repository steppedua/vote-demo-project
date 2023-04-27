package com.steppedua.loadbalancer.service;


import com.steppedua.loadbalancer.config.LoadBalancerConfigurationProperties;
import com.steppedua.loadbalancer.exception.RestTemplateException;
import com.steppedua.loadbalancer.model.VoteSaveRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class VoteSaveTask {
    private final RestTemplate restTemplate;
    private final LoadBalancerConfigurationProperties loadBalancerConfigurationProperties;
    private final AtomicInteger serverCounter;
    private final VoteSaveRequestDto voteSaveRequestDto;

    public UUID voteSaveTask() {
        final var serverNumber = serverCounter.incrementAndGet() % loadBalancerConfigurationProperties.getServerQuantity();

        final var serverIp = loadBalancerConfigurationProperties.getServersIp().get(serverNumber);
        log.debug("Parameter serverIp {}", serverIp);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<VoteSaveRequestDto> requesEntity = new HttpEntity<>(voteSaveRequestDto, headers);
        log.debug("Parameter requesEntity {}", requesEntity);

        final var uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(loadBalancerConfigurationProperties.getServerPath())
                .port(serverIp)
                .path("/api/v1/vote/")
                .build()
                .toUri();
        log.debug("Parameter uri {}", uri);

        //TODO заменить на feign client
        try {
            final var uuidResponseEntity = restTemplate.postForEntity(uri, requesEntity, UUID.class);
            log.debug("Parameter uuidResponseEntity {} from serverIp {}", uuidResponseEntity, serverIp);

            return uuidResponseEntity.getBody();
        } catch (RuntimeException e) {
            log.error("Exception in voteSaveTask method with message: {}", e.getMessage());
            throw new RestTemplateException(e.getMessage(), e);
        }
    }
}
