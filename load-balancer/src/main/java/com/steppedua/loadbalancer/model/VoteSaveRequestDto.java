package com.steppedua.loadbalancer.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteSaveRequestDto {
    @JsonProperty("voteValue")
    private Vote voteValue;
}
