package com.steppedua.voteservice.model;


import com.steppedua.voteservice.entity.Vote;
import lombok.Data;

import java.util.UUID;

@Data
public class VoteSaveRequestDto {
    private final UUID userId;
    private final Vote voteValue;
}
