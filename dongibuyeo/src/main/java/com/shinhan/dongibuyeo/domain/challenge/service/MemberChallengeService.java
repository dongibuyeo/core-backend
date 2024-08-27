package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.domain.account.service.AccountService;
import com.shinhan.dongibuyeo.domain.challenge.dto.request.JoinChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.*;
import com.shinhan.dongibuyeo.domain.challenge.entity.*;
import com.shinhan.dongibuyeo.domain.challenge.exception.*;
import com.shinhan.dongibuyeo.domain.challenge.mapper.ChallengeMapper;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import com.shinhan.dongibuyeo.domain.challenge.repository.MemberChallengeRepository;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.domain.savings.dto.response.SavingAccountsDetail;
import com.shinhan.dongibuyeo.domain.savings.service.SavingsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MemberChallengeService {

    private final MemberService memberService;
    private final ChallengeRepository challengeRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    private final ChallengeMapper challengeMapper;
    private final AccountService accountService;
    private final SavingsService savingsService;
    private final ChallengeRewardService challengeRewardService;
    private final ChallengeService challengeService;

    public MemberChallengeService(MemberService memberService, ChallengeRepository challengeRepository, MemberChallengeRepository memberChallengeRepository, ChallengeMapper challengeMapper, AccountService accountService, SavingsService savingsService, ChallengeRewardService challengeRewardService, ChallengeService challengeService, ChallengeService challengeService1) {
        this.memberService = memberService;
        this.challengeRepository = challengeRepository;
        this.memberChallengeRepository = memberChallengeRepository;
        this.challengeMapper = challengeMapper;
        this.accountService = accountService;
        this.savingsService = savingsService;
        this.challengeRewardService = challengeRewardService;
        this.challengeService = challengeService1;
    }

    private Challenge findChallengeById(UUID challengeId) {
        return challengeRepository.findChallengeById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
    }

    public List<ChallengeResponse> findAllChallengesByMemberId(UUID memberId) {
        return memberChallengeRepository.findChallengesByMemberId(memberId)
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

        Optional<Challenge> existingChallenge = memberChallengeRepository.findChallengesByMemberId(member.getId())
                .stream()
                .filter(findChallenge -> findChallenge.getId().equals(challenge.getId()))
                .findAny();

        if (existingChallenge.isPresent()) {
            throw new ChallengeAlreadyJoinedException(challenge.getId(), member.getId());
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

        challengeRewardService.transferFromChallengeAccountToMemberAccount(member, challenge, memberChallenge.getDeposit());
        removeMemberFromChallenge(member, challenge, memberChallenge);
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
    
    @Transactional
    public void withdrawChallenge(UUID challengeId, UUID memberId) {
        Challenge challenge = findChallengeById(challengeId);
        Member member = memberService.getMemberById(memberId);
        MemberChallenge memberChallenge = getMemberChallenge(challengeId, memberId);

        // 적금의 경우만 중도해지 가능
        validateChallengeWithdrawal(challenge);

        // 예치금 환급
        challengeRewardService.transferFromChallengeAccountToMemberAccount(member, challenge, memberChallenge.getDeposit());
        memberChallenge.updateStatus(MemberChallengeStatus.REWARDED);
        memberChallenge.softDelete();

        // 적금 해지(유저 적금 계좌 -> 유저 적금 납입 계좌로 자동 환급됨)
        Optional<SavingAccountsDetail> savingChallengeAccount = savingsService.getAllSavingsByMemberId(memberId)
                .stream()
                .filter(savings -> savings.getAccountName().equals(challenge.getTitle()))
                .findFirst();

        if (savingChallengeAccount.isPresent()) {
            String accountNo = savingChallengeAccount.get().getAccountNo();
            savingsService.deleteSavingAccounts(memberId, accountNo);
        }
    }

    private void validateChallengeWithdrawal(Challenge challenge) {
        if (challenge.getType() != ChallengeType.SAVINGS_SEVEN) {
            throw new ChallengeCannotWithdrawException(challenge.getType());
        }
    }

    private MemberChallenge getMemberChallenge(UUID challengeId, UUID memberId) {
        return memberChallengeRepository.findMemberChallengeByChallengeIdAndMemberId(challengeId, memberId)
                .orElseThrow(() -> new MemberChallengeNotFoundException(challengeId, memberId));
    }

    private Account getMemberChallengeAccount(Member member) {
        return member.getChallengeAccount();
    }

    public MemberChallengeResponse findChallengeByChallengeIdAndMemberId(UUID challengeId, UUID memberId) {
        return memberChallengeRepository.findChallengeByMemberIdAndChallengeId(challengeId, memberId)
                .orElseThrow(() -> new MemberChallengeNotFoundException(challengeId, memberId));
    }

    public List<ChallengeResponse> findAllChallengesByMemberIdAndStatus(UUID memberId, ChallengeStatus status) {
        return challengeRepository.findChallengesByMemberIdAndStatus(memberId, status);
    }

    @Transactional(readOnly = true)
    public ScoreDetailResponse getChallengeScoreDetail(UUID memberId, UUID challengeId) {
        MemberChallenge memberChallenge = memberChallengeRepository.findMemberChallengeByChallengeIdAndMemberId(challengeId, memberId)
                .orElseThrow(() -> new MemberChallengeNotFoundException(challengeId, memberId));

        List<DailyScoreDetail> dailyScores = memberChallenge.getDailyScores().stream()
                .map(this::convertToDailyScoreDetail)
                .sorted(Comparator.comparing(DailyScoreDetail::getDate).reversed())
                .collect(Collectors.toList());

        return new ScoreDetailResponse(memberChallenge.getTotalScore(), dailyScores);
    }

    private DailyScoreDetail convertToDailyScoreDetail(DailyScore dailyScore) {
        return DailyScoreDetail.builder()
                .date(dailyScore.getDate().toString())
                .entries(dailyScore.getScoreDetails())
                .build();
    }

    public List<MemberChallenge> findByMemberId(UUID memberId) {
        return memberChallengeRepository.findByMemberId(memberId);
    }

    public MemberChallenge findByMemberIdAndChallengeType(UUID memberId, ChallengeType challengeType) {
        return  memberChallengeRepository.findByMemberIdAndChallengeType(memberId, challengeType)
                .orElseThrow(() -> new MemberChallengeNotFoundException(memberId, challengeType));
    }

    public List<MemberChallenge> findsByChallengeTypeAndStatus(ChallengeType challengeType, ChallengeStatus challengeStatus) {
        return memberChallengeRepository.findAllByChallengeTypeAndChallengeStatus(challengeType, challengeStatus);
    }

    @Transactional
    public RewardResponse getReward(UUID memberId, UUID challengeId) {
        Member member = memberService.getMemberById(memberId);
        Challenge challenge = challengeService.findChallengeById(challengeId);
        MemberChallenge memberChallenge = getMemberChallenge(challengeId, memberId);
        MemberChallengeStatus status = memberChallenge.getStatus();

        if (status != MemberChallengeStatus.CALCULATED) {
            throw new MemberChallengeCannotRewardException(status);
        }

        Long baseReward = memberChallenge.getBaseReward();
        Long additionalReward = memberChallenge.getAdditionalReward();
        challengeRewardService.transferFromChallengeAccountToMemberAccount(member, challenge, baseReward);
        challengeRewardService.transferFromChallengeAccountToMemberAccount(member, challenge, additionalReward);

        memberChallenge.updateStatus(MemberChallengeStatus.REWARDED);
        return new RewardResponse(baseReward, additionalReward);
    }
}