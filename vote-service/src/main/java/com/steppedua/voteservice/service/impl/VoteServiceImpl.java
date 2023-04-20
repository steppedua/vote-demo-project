package com.steppedua.voteservice.service.impl;

import com.steppedua.voteservice.entity.VoteEntity;
import com.steppedua.voteservice.model.VoteSaveRequestDto;
import com.steppedua.voteservice.model.VoteStatisticsResponseDto;
import com.steppedua.voteservice.repository.VoteRepository;
import com.steppedua.voteservice.service.VoteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;

    @Override
    @Transactional
    public UUID saveVote(@NonNull final VoteSaveRequestDto voteSaveRequestDto) {
        log.debug("Start saveVote method");
        final var voteEntity = new VoteEntity(voteSaveRequestDto.getVoteValue());

        voteRepository.save(voteEntity);
        log.debug("End saveVote method");
        return voteEntity.getUserId();
    }

    @Override
    @Transactional(readOnly = true)
    public VoteStatisticsResponseDto getVoteStatistics() {
        log.debug("Start getVoteStatistics method");
        final var voteValueStatistics = voteRepository.findVoteValueStatistics();
        log.debug("End getVoteStatistics method");
        return voteValueStatistics;
    }
}
