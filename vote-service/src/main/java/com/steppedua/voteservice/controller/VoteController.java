package com.steppedua.voteservice.controller;

import com.steppedua.voteservice.controller.validator.VoteSaveValidator;
import com.steppedua.voteservice.model.VoteSaveRequestDto;
import com.steppedua.voteservice.model.VoteStatisticsResponseDto;
import com.steppedua.voteservice.service.VoteService;
import com.steppedua.voteservice.util.VoteUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(VoteUtil.BASE_URI + "/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;
    private final VoteSaveValidator voteSaveValidator;

    @PostMapping("/")
    public UUID saveVote(@Valid @RequestBody VoteSaveRequestDto voteSaveRequestDto) {
        final UUID uuid = voteService.saveVote(voteSaveRequestDto);
        return uuid;
    }

    @GetMapping("/statistics")
    public VoteStatisticsResponseDto getVoteStatistics() {
        return voteService.getVoteStatistics();
    }

    @InitBinder("voteSaveRequestDto")
    private void deactivateRequestBinder(WebDataBinder binder) {
        binder.addValidators(voteSaveValidator);
    }
}
