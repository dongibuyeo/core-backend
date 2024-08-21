package com.shinhan.dongibuyeo.domain.challenge.controller;

import com.shinhan.dongibuyeo.domain.challenge.dto.request.CancelJoinChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.ChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.JoinChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.service.ChallengeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(final ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @GetMapping
    public ResponseEntity<List<ChallengeResponse>> getChallenges() {
        return ResponseEntity.ok(challengeService.findAllChallenges());
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity<ChallengeResponse> getChallengeById(@PathVariable UUID challengeId) {
        return ResponseEntity.ok(challengeService.findChallengeByChallengeId(challengeId));
    }

    @DeleteMapping("/{challengeId}")
    public ResponseEntity<ChallengeResponse> deleteChallengeById(@PathVariable UUID challengeId) {
        challengeService.deleteChallengeByChallengeId(challengeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<ChallengeResponse> makeChallenge(@RequestBody @Valid ChallengeRequest request) {
        return ResponseEntity.ok(challengeService.makeChallenge(request));
    }

    @PostMapping("/{challengeId}/join")
    public ResponseEntity<Void> joinChallenge(@PathVariable UUID challengeId, @RequestBody @Valid JoinChallengeRequest request) {
        challengeService.joinChallenge(challengeId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{challengeId}/cancel")
    public ResponseEntity<Void> cancelJoinChallenge(@PathVariable UUID challengeId, @RequestBody @Valid CancelJoinChallengeRequest request) {
        challengeService.cancelJoinChallenge(challengeId, request.getMemberId());
        return ResponseEntity.ok().build();
    }
}
