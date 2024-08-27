package com.shinhan.dongibuyeo.domain.challenge.controller;

import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.JoinChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.MemberChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.MemberChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.RewardResponse;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ScoreDetailResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.service.MemberChallengeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/challenges/member")
public class MemberChallengeController {

    private final MemberChallengeService memberChallengeService;

    public MemberChallengeController(MemberChallengeService memberChallengeService) {
        this.memberChallengeService = memberChallengeService;
    }

    @GetMapping
    public ResponseEntity<List<ChallengeResponse>> getMemberChallenges(@RequestParam UUID memberId) {
        return ResponseEntity.ok(memberChallengeService.findAllChallengesByMemberId(memberId));
    }

     @GetMapping("/status")
    public ResponseEntity<List<ChallengeResponse>> getMemberChallengesByStatus(@RequestParam UUID memberId, @RequestParam ChallengeStatus status) {
        return ResponseEntity.ok(memberChallengeService.findAllChallengesByMemberIdAndStatus(memberId, status));
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity<MemberChallengeResponse> getMemberChallengeByChallengeId(@PathVariable UUID challengeId,
                                                                                   @RequestParam UUID memberId) {
        return ResponseEntity.ok(memberChallengeService.findChallengeByChallengeIdAndMemberId(challengeId, memberId));
    }

    @PostMapping("/join")
    public ResponseEntity<Void> joinChallenge(@RequestBody @Valid JoinChallengeRequest request) {
        memberChallengeService.joinChallenge(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/account")
    public ResponseEntity<Void> makeMemberChallengeAccount(@RequestBody @Valid MakeAccountRequest request) {
        memberChallengeService.makeMemberChallengeAccount(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelJoinChallenge(@RequestBody @Valid MemberChallengeRequest request) {
        memberChallengeService.cancelJoinChallenge(request.getMemberId(), request.getChallengeId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdrawChallenge(@RequestBody @Valid MemberChallengeRequest request) {
        memberChallengeService.withdrawChallenge(request.getChallengeId(), request.getChallengeId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/score-details")
    public ResponseEntity<ScoreDetailResponse> getChallengeScoreDetail(@RequestBody @Valid MemberChallengeRequest request) {
        return ResponseEntity.ok(memberChallengeService.getChallengeScoreDetail(request.getMemberId(), request.getChallengeId()));
    }

    @GetMapping("/reward")
    public ResponseEntity<RewardResponse> getReward(@RequestBody @Valid MemberChallengeRequest request) {
        return ResponseEntity.ok(memberChallengeService.getReward(request.getMemberId(), request.getChallengeId()));
    }
}
