package com.steppedua.voteservice.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.steppedua.voteservice.entity.Vote;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteSaveRequestDto {
    @JsonProperty("voteValue")
    private Vote voteValue;
}
