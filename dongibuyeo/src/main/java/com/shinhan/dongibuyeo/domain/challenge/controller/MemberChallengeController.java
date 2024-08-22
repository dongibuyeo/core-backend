package com.shinhan.dongibuyeo.domain.challenge.controller;

import com.shinhan.dongibuyeo.domain.challenge.dto.request.MemberChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.JoinChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.MemberChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.service.ChallengeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/challenges/member")
public class MemberChallengeController {

    private final ChallengeService challengeService;

    public MemberChallengeController(final ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @GetMapping
    public ResponseEntity<List<ChallengeResponse>> getMemberChallenges(@RequestParam UUID memberId) {
        return ResponseEntity.ok(challengeService.findAllChallengesByMemberId(memberId));
    }

     @GetMapping("/status")
    public ResponseEntity<List<ChallengeResponse>> getMemberChallengesByStatus(@RequestParam UUID memberId, @RequestParam ChallengeStatus status) {
        return ResponseEntity.ok(challengeService.findAllChallengesByMemberIdAndStatus(memberId, status));
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity<MemberChallengeResponse> getMemberChallengeByChallengeId(@PathVariable UUID challengeId,
                                                                                   @RequestParam UUID memberId) {
        return ResponseEntity.ok(challengeService.findChallengeByChallengeIdAndMemberId(challengeId, memberId));
    }

    @PostMapping("/join")
    public ResponseEntity<Void> joinChallenge(@RequestBody @Valid JoinChallengeRequest request) {
        challengeService.joinChallenge(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelJoinChallenge(@RequestBody @Valid MemberChallengeRequest request) {
        challengeService.cancelJoinChallenge(request.getMemberId(), request.getChallengeId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdrawChallenge(@RequestBody @Valid MemberChallengeRequest request) {
        challengeService.withdrawChallenge(request.getChallengeId(), request.getChallengeId());
        return ResponseEntity.ok().build();
    }
}
