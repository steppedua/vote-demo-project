package com.steppedua.loadbalancer.controller;

import com.steppedua.loadbalancer.model.VoteSaveRequestDto;
import com.steppedua.loadbalancer.model.VoteStatisticsResponseDto;
import com.steppedua.loadbalancer.service.LoadBalancerService;
import com.steppedua.loadbalancer.util.LoadBalancerServerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(LoadBalancerServerUtil.BASE_URI + "/vote")
@RequiredArgsConstructor
public class LoadBalancerController {
    private final LoadBalancerService loadBalancerService;

    @PostMapping("/")
    public ResponseEntity<Void> saveVote(@RequestBody VoteSaveRequestDto voteSaveRequestDto) {
        final var uuid = loadBalancerService.voteSave(voteSaveRequestDto);
        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(uuid)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/statistics")
    public VoteStatisticsResponseDto getVoteStatistics() {
        return loadBalancerService.getVoteStatistics();
    }
}
