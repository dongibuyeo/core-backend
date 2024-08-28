package com.shinhan.dongibuyeo.domain.challenge.controller;

import com.shinhan.dongibuyeo.domain.challenge.dto.request.ChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeRankResponse;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResultResponse;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.MemberChallengeRankResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
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

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PostMapping
    public ResponseEntity<ChallengeResponse> makeChallenge(@RequestBody @Valid ChallengeRequest request) {
        return ResponseEntity.ok(challengeService.makeChallenge(request));
    }

    @GetMapping
    public ResponseEntity<List<ChallengeResponse>> getChallenges() {
        return ResponseEntity.ok(challengeService.findAllChallenges());
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity<ChallengeResponse> getChallengeById(@PathVariable UUID challengeId) {
        return ResponseEntity.ok(challengeService.findChallengeByChallengeId(challengeId));
    }

    @GetMapping("/status")
    public ResponseEntity<List<ChallengeResponse>> getChallengesByStatus(@RequestParam ChallengeStatus status) {
        return ResponseEntity.ok(challengeService.findAllChallengesByStatus(status));

    }

    @DeleteMapping("/{challengeId}")
    public ResponseEntity<ChallengeResponse> deleteChallengeById(@PathVariable UUID challengeId) {
        challengeService.deleteChallengeByChallengeId(challengeId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{challengeId}")
    public ResponseEntity<ChallengeResponse> modifyChallengeById(@PathVariable UUID challengeId,
                                                                 @RequestBody @Valid ChallengeRequest request) {
        return ResponseEntity.ok(challengeService.updateChallengeByChallengeId(challengeId, request));
    }

    @GetMapping("/rank/{challengeId}")
    public ResponseEntity<ChallengeRankResponse> getChallengeRank(@PathVariable UUID challengeId) {
        return ResponseEntity.ok(challengeService.getChallengeRank(challengeId));
    }

    @GetMapping("/rank/my-challenge")
    public ResponseEntity<MemberChallengeRankResponse> getMyChallenges(@RequestParam UUID challengeId,
                                                                       @RequestParam UUID memberId) {
        return ResponseEntity.ok(challengeService.getMemberChallengeRank(challengeId, memberId));
    }

    @GetMapping("/estimate-reward")
    public ResponseEntity<ChallengeResultResponse> calculateEstimatedReward(@RequestParam UUID challengeId) {
        return ResponseEntity.ok(challengeService.calculateEstimatedReward(challengeId));
    }

}
