package com.steppedua.voteservice.repository;

import com.steppedua.voteservice.entity.VoteEntity;
import com.steppedua.voteservice.model.VoteStatisticsResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, UUID> {

    @Query("select sum (case when v.voteValue = 'YES' then 1 else 0 end) as countVoteValueYes, " +
            "sum (case when v.voteValue = 'NO' then 1 else 0 end) as countVoteValueNo " +
            "from VoteEntity v")
    VoteStatisticsResponseDto findVoteValueStatistics();
}
