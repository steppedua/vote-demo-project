package com.steppedua.loadbalancer.service;

import com.steppedua.loadbalancer.model.VoteSaveRequestDto;
import com.steppedua.loadbalancer.model.VoteStatisticsResponseDto;

import java.util.UUID;

public interface LoadBalancerService {
    UUID voteSave(VoteSaveRequestDto voteSaveRequestDto);

    VoteStatisticsResponseDto getVoteStatistics();
}
