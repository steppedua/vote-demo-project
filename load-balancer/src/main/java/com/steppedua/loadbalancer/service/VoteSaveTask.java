package com.steppedua.loadbalancer.service;


import com.steppedua.loadbalancer.config.LoadBalancerConfig;
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
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class VoteSaveTask implements Callable<UUID> {
    private static final int INITIAL_SERVER_NUMBER = 1;
    private final RestTemplate restTemplate;
    private final LoadBalancerConfig loadBalancerConfig;
    private final AtomicInteger serverCounter;
    private final VoteSaveRequestDto voteSaveRequestDto;

    @Override
    public UUID call() {
        if (serverCounter.get() == loadBalancerConfig.getServerQuantity()) {
            serverCounter.set(INITIAL_SERVER_NUMBER);
        } else {
            serverCounter.incrementAndGet();
        }

        final var serverIp = loadBalancerConfig.getServersIp().get(serverCounter.get());
        log.debug("Parameter serverIp {}", serverIp);

        //TODO заменить на feign client
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<VoteSaveRequestDto> requesEntity = new HttpEntity<>(voteSaveRequestDto, headers);
        log.debug("Parameter requesEntity {}", requesEntity);

        final var uri = URI.create("http://" + loadBalancerConfig.getServerPath() + ":" + serverIp + "/api/v1/vote/");
        log.debug("Parameter uri {}", uri);

        try {
            final var uuidResponseEntity = restTemplate.postForEntity(uri, requesEntity, UUID.class);
            log.debug("Parameter uuidResponseEntity {}", uuidResponseEntity);

            return uuidResponseEntity.getBody();
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }
}
