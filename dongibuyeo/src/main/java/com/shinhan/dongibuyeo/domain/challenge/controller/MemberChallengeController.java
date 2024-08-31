package com.shinhan.dongibuyeo.domain.challenge.controller;

import com.shinhan.dongibuyeo.domain.account.dto.request.MemberIdRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.MakeAccountResponse;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.JoinChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.MemberChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.*;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.service.MemberChallengeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/challenges/member")
public class MemberChallengeController {

    private final MemberChallengeService memberChallengeService;

    public MemberChallengeController(MemberChallengeService memberChallengeService) {
        this.memberChallengeService = memberChallengeService;
    }

    @GetMapping
    public ResponseEntity<MemberChallengesResponse> getMemberChallenges(@RequestParam UUID memberId) {
        return ResponseEntity.ok(memberChallengeService.findAllMemberChallengesByMemberId(memberId));
    }

    @GetMapping("/status")
    public ResponseEntity<?> getMemberChallengesByStatus(@RequestParam UUID memberId, @RequestParam ChallengeStatus status) {
        return ResponseEntity.ok(memberChallengeService.findAllChallengesByMemberIdAndStatus(memberId, status));
    }

    @GetMapping("/status-count")
    public ResponseEntity<ChallengeStatusCountResponse> getChallengeStatusCount(@RequestParam UUID memberId) {
        return ResponseEntity.ok(memberChallengeService.getChallengeStatusCount(memberId));
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity<MemberChallengeDetail> getMemberChallengeByChallengeId(@PathVariable UUID challengeId,
                                                                                 @RequestParam UUID memberId) {
        return ResponseEntity.ok(memberChallengeService.findChallengeByChallengeIdAndMemberId(challengeId, memberId));
    }

    @PostMapping("/join")
    public ResponseEntity<Void> joinChallenge(@RequestBody @Valid JoinChallengeRequest request) {
        memberChallengeService.joinChallenge(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/account")
    public ResponseEntity<MakeAccountResponse> makeMemberChallengeAccount(@RequestBody @Valid MemberIdRequest memberIdRequest) {
        log.info("[makeMemberChallengeAccount] memberId: {}", memberIdRequest.getMemberId());
        return ResponseEntity.ok(memberChallengeService.makeMemberChallengeAccount(memberIdRequest.getMemberId()));
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelJoinChallenge(@RequestBody @Valid MemberChallengeRequest request) {
        memberChallengeService.cancelJoinChallenge(request.getMemberId(), request.getChallengeId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdrawChallenge(@RequestBody @Valid MemberChallengeRequest request) {
        memberChallengeService.withdrawChallenge(request.getChallengeId(), request.getMemberId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/score-details")
    public ResponseEntity<ScoreDetailResponse> getChallengeScoreDetail(@RequestBody @Valid MemberChallengeRequest request) {
        return ResponseEntity.ok(memberChallengeService.getChallengeScoreDetail(request.getMemberId(), request.getChallengeId()));
    }

    @PostMapping("/reward")
    public ResponseEntity<RewardResponse> getReward(@RequestBody @Valid MemberChallengeRequest request) {
        return ResponseEntity.ok(memberChallengeService.getReward(request.getMemberId(), request.getChallengeId()));
    }
}
