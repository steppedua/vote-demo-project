package com.steppedua.voteservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = VoteStatisticsDeserializer.class)
public interface VoteStatisticsResponseDto {
    @JsonProperty("countVoteValueYes")
    int getCountVoteValueYes();

    @JsonProperty("countVoteValueNo")
    int getCountVoteValueNo();
}
