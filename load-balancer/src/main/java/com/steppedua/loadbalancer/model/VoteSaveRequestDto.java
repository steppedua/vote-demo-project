package com.steppedua.loadbalancer.model;


import lombok.Data;

import java.util.UUID;

@Data
public class VoteSaveRequestDto {
    private final UUID userId;
    private final Vote voteValue;
}
