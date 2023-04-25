package com.steppedua.loadbalancer.service;

import com.steppedua.loadbalancer.model.VoteSaveRequestDto;
import com.steppedua.loadbalancer.model.VoteStatisticsResponseDto;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface LoadBalancerService {
    CompletableFuture<UUID> voteSave(VoteSaveRequestDto voteSaveRequestDto);

    CompletableFuture<VoteStatisticsResponseDto> getVoteStatistics();
}
