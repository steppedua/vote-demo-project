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

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.steppedua.loadbalancer.util.LoadBalancerServerUtil.INITIAL_SERVER_NUMBER;

@Slf4j
@RequiredArgsConstructor
public class VoteSaveTask {
    private final RestTemplate restTemplate;
    private final LoadBalancerConfigurationProperties loadBalancerConfigurationProperties;
    private final AtomicInteger serverCounter;
    private final VoteSaveRequestDto voteSaveRequestDto;

    public UUID voteSaveTask() {
        if (serverCounter.get() == loadBalancerConfigurationProperties.getServerQuantity()) {
            serverCounter.set(INITIAL_SERVER_NUMBER);
        } else {
            serverCounter.incrementAndGet();
        }

        final var serverIp = loadBalancerConfigurationProperties.getServersIp().get(serverCounter.get());
        log.debug("Parameter serverIp {}", serverIp);

        //TODO заменить на feign client
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<VoteSaveRequestDto> requesEntity = new HttpEntity<>(voteSaveRequestDto, headers);
        log.debug("Parameter requesEntity {}", requesEntity);

        final var uri = URI.create("http://" + loadBalancerConfigurationProperties.getServerPath() + ":" + serverIp + "/api/v1/vote/");
        log.debug("Parameter uri {}", uri);

        try {
            final var uuidResponseEntity = restTemplate.postForEntity(uri, requesEntity, UUID.class);
            log.debug("Parameter uuidResponseEntity {}", uuidResponseEntity);

            return uuidResponseEntity.getBody();
        } catch (RuntimeException e) {
            log.error("Exception in voteSaveTask method with message: {}", e.getMessage());
            throw new RestTemplateException(e.getMessage(), e);
        }
    }
}
