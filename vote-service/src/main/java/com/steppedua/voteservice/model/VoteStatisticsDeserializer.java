package com.steppedua.voteservice.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;

import java.io.IOException;

public class VoteStatisticsDeserializer extends JsonDeserializer<VoteStatisticsResponseDto> {
    @Override
    public VoteStatisticsResponseDto deserialize(@NonNull JsonParser p, DeserializationContext ctxt) {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        ObjectNode root;
        VoteStatisticsResponseDto voteStatisticsResponseDto;
        try {
            root = mapper.readTree(p);

            int countVoteValueYes = root.get("countVoteValueYes").asInt();
            int countVoteValueNo = root.get("countVoteValueNo").asInt();
            voteStatisticsResponseDto = new VoteStatisticsResponseDto() {
                @Override
                public int getCountVoteValueYes() {
                    return countVoteValueYes;
                }

                @Override
                public int getCountVoteValueNo() {
                    return countVoteValueNo;
                }
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return voteStatisticsResponseDto;
    }
}