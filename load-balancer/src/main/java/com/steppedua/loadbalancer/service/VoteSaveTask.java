package com.steppedua.loadbalancer.service;


import com.steppedua.loadbalancer.config.LoadBalancerConfig;
import com.steppedua.loadbalancer.model.VoteSaveRequestDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;


public class VoteSaveTask implements Callable<UUID> {
    private static final int VALUE = 1;
    private final RestTemplate restTemplate;
    private final LoadBalancerConfig loadBalancerConfig;
    private final AtomicInteger atomicInteger;
    private final VoteSaveRequestDto voteSaveRequestDto;

    public VoteSaveTask(RestTemplate restTemplate, LoadBalancerConfig loadBalancerConfig, AtomicInteger atomicInteger, VoteSaveRequestDto voteSaveRequestDto) {
        this.restTemplate = restTemplate;
        this.loadBalancerConfig = loadBalancerConfig;
        this.atomicInteger = atomicInteger;
        this.voteSaveRequestDto = voteSaveRequestDto;
    }

    @Override
    public UUID call() throws Exception {

        if (atomicInteger.get() == loadBalancerConfig.getServerQuantity()) {
            atomicInteger.set(VALUE);
        } else {
            atomicInteger.incrementAndGet();
        }

        final var serverIp = loadBalancerConfig.getServersIp().get(atomicInteger.get());
        //todo заменить на feign client
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<VoteSaveRequestDto> entity = new HttpEntity<>(voteSaveRequestDto, headers);

        final var url = URI.create(loadBalancerConfig.getServerPath() + serverIp + "/api/v1/vote/");
        final var uuidResponseEntity = restTemplate.postForEntity(url, entity, UUID.class);
        return uuidResponseEntity.getBody();
    }
}
