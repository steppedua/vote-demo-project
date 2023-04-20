package com.steppedua.loadbalancer.service;

import com.steppedua.loadbalancer.model.VoteSaveRequestDto;
import com.steppedua.loadbalancer.model.VoteStatisticsResponseDto;

import java.util.UUID;
import java.util.concurrent.Future;

public interface LoadBalancerService {
    Future<UUID> voteSave(VoteSaveRequestDto voteSaveRequestDto);

    Future<VoteStatisticsResponseDto> getVoteStatistics();
}
