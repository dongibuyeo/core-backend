package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.account.dto.request.TransferRequest;
import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.domain.account.service.AccountService;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.JoinChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.MemberChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import com.shinhan.dongibuyeo.domain.challenge.exception.*;
import com.shinhan.dongibuyeo.domain.challenge.mapper.ChallengeMapper;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import com.shinhan.dongibuyeo.domain.challenge.repository.MemberChallengeRepository;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.global.entity.TransferType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MemberChallengeService {

    private final MemberService memberService;
    private final ChallengeRepository challengeRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    private final ChallengeMapper challengeMapper;
    private final AccountService accountService;

    public MemberChallengeService(MemberService memberService, ChallengeRepository challengeRepository, MemberChallengeRepository memberChallengeRepository, ChallengeMapper challengeMapper, AccountService accountService) {
        this.memberService = memberService;
        this.challengeRepository = challengeRepository;
        this.memberChallengeRepository = memberChallengeRepository;
        this.challengeMapper = challengeMapper;
        this.accountService = accountService;
    }

    private Challenge findChallengeById(UUID challengeId) {
        return challengeRepository.findChallengeById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
    }

    public List<ChallengeResponse> findAllChallengesByMemberId(UUID memberId) {
        return memberChallengeRepository.findChallengesByMemberId(memberId)
                .orElseGet(ArrayList::new)
                .stream()
                .map(challengeMapper::toChallengeResponse)
                .toList();
    }

    @Transactional
    public void joinChallenge(JoinChallengeRequest request) {
        Member member = memberService.getMemberById(request.getMemberId());
        Challenge challenge = findChallengeById(request.getChallengeId());

        validateChallengeJoin(challenge, member);

        MemberChallenge memberChallenge = createMemberChallenge(member, challenge, request.getDeposit());
        challenge.addMember(memberChallenge);
        member.addChallenge(memberChallenge);
    }

    private void validateChallengeJoin(Challenge challenge, Member member) {
        if (LocalDate.now().isAfter(challenge.getStartDate())) {
            throw new ChallengeAlreadyStartedException(challenge.getId());
        }
        if (!member.hasChallengeAccount()) {
            throw new ChallengeCannotJoinException(member.getId());
        }
    }

    private MemberChallenge createMemberChallenge(Member member, Challenge challenge, Long deposit) {
        return MemberChallenge.builder()
                .member(member)
                .challenge(challenge)
                .deposit(deposit)
                .build();
    }

    @Transactional
    public void cancelJoinChallenge(UUID memberId, UUID challengeId) {
        Member member = memberService.getMemberById(memberId);
        Challenge challenge = findChallengeById(challengeId);
        MemberChallenge memberChallenge = getMemberChallenge(challengeId, memberId);

        validateChallengeCancellation(challenge);

        removeMemberFromChallenge(member, challenge, memberChallenge);
        transferDepositBack(member, challenge, memberChallenge.getDeposit());
    }

    private void validateChallengeCancellation(Challenge challenge) {
        if (challenge.getStatus().equals(ChallengeStatus.IN_PROGRESS)) {
            throw new ChallengeAlreadyStartedException(challenge.getId());
        }
    }

    private void removeMemberFromChallenge(Member member, Challenge challenge, MemberChallenge memberChallenge) {
        challenge.removeMember(memberChallenge);
        member.removeChallenge(memberChallenge);
        memberChallenge.softDelete();
    }

    private void transferDepositBack(Member member, Challenge challenge, Long deposit) {
        Account memberChallengeAccount = getMemberChallengeAccount(member);
        accountService.accountTransfer(new TransferRequest(
                member.getId(),
                memberChallengeAccount.getAccountNo(),
                challenge.getAccount().getAccountNo(),
                deposit,
                TransferType.CHALLENGE));
    }

    @Transactional
    public void withdrawChallenge(UUID challengeId, UUID memberId) {
        Challenge challenge = findChallengeById(challengeId);
        Member member = memberService.getMemberById(memberId);
        MemberChallenge memberChallenge = getMemberChallenge(challengeId, memberId);

        validateChallengeWithdrawal(challenge);

        transferDepositBack(member, challenge, memberChallenge.getDeposit());
        removeMemberFromChallenge(member, challenge, memberChallenge);

        // TODO: 적금 해지 후 환급 로직 (유저 적금 계좌 -> 유저 챌린지 계좌)
    }

    private void validateChallengeWithdrawal(Challenge challenge) {
        if(challenge.getType() != ChallengeType.SAVINGS_SEVEN) {
            throw new ChallengeCannotWithdrawException(challenge.getType());
        }
    }

    private MemberChallenge getMemberChallenge(UUID challengeId, UUID memberId) {
        return memberChallengeRepository.findMemberChallengeByChallengeIdAndMemberId(challengeId, memberId)
                .orElseThrow(() -> new MemberChallengeNotFoundException(challengeId, memberId));
    }

    private Account getMemberChallengeAccount(Member member) {
        return member.getChallengeAccount()
                .orElseThrow(() -> new ChallengeAccountNotFoundException(member.getId()));
    }

    public MemberChallengeResponse findChallengeByChallengeIdAndMemberId(UUID challengeId, UUID memberId) {
        return memberChallengeRepository.findChallengeByMemberIdAndChallengeId(challengeId, memberId)
                .orElseThrow(() -> new MemberChallengeNotFoundException(challengeId, memberId));
    }

    public List<ChallengeResponse> findAllChallengesByMemberIdAndStatus(UUID memberId, ChallengeStatus status) {
        return challengeRepository.findChallengesByMemberIdAndStatus(memberId, status);
    }
}