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
import java.util.concurrent.Future;

@RestController
@RequestMapping(LoadBalancerServerUtil.BASE_URI + "/vote")
@RequiredArgsConstructor
public class LoadBalancerController {
    private final LoadBalancerService loadBalancerService;

    //TODO а как тут быть, если у нас Future возвращаться должен?
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

    //TODO тут при запросе выдается
    // {
    //    "done": false,
    //    "cancelled": false
    // }
    // Это появилось благодаря Future, что делать в таком случае?
    @GetMapping("/statistics")
    public Future<VoteStatisticsResponseDto> getVoteStatistics() {
        return loadBalancerService.getVoteStatistics();
    }
}
