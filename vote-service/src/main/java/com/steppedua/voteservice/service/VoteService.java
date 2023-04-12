package com.steppedua.voteservice.service;

import com.steppedua.voteservice.model.VoteSaveRequestDto;
import com.steppedua.voteservice.model.VoteStatisticsResponseDto;

import java.util.UUID;

public interface VoteService {
    UUID saveVote(VoteSaveRequestDto voteSaveRequestDto);

    VoteStatisticsResponseDto getVoteStatistics();
}
