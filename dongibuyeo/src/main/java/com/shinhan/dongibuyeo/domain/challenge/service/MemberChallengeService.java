package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.account.dto.request.MakeAccountRequest;
import com.shinhan.dongibuyeo.domain.account.dto.request.TransferRequest;
import com.shinhan.dongibuyeo.domain.account.dto.response.MakeAccountResponse;
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
import com.shinhan.dongibuyeo.domain.savings.exception.SavingAccountNotFoundException;
import com.shinhan.dongibuyeo.domain.savings.service.SavingsService;
import com.shinhan.dongibuyeo.global.entity.TransferType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MemberChallengeService {

    @Value("${shinhan.challenge.member-account}")
    private String memberChallengeAccountCode;

    @Value("${shinhan.deposit.min}")
    private Long minDeposit;

    @Value("${shinhan.deposit.max}")
    private Long maxDeposit;

    @Value("${shinhan.deposit.unit}")
    private Long depositUnit;

    private final MemberService memberService;
    private final ChallengeRepository challengeRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    private final ChallengeMapper challengeMapper;
    private final SavingsService savingsService;
    private final ChallengeRewardService challengeRewardService;
    private final ChallengeService challengeService;
    private final AccountService accountService;

    public MemberChallengeService(MemberService memberService, ChallengeRepository challengeRepository, MemberChallengeRepository memberChallengeRepository, ChallengeMapper challengeMapper, SavingsService savingsService, ChallengeRewardService challengeRewardService, ChallengeService challengeService, ChallengeService challengeService1, AccountService accountService) {
        this.memberService = memberService;
        this.challengeRepository = challengeRepository;
        this.memberChallengeRepository = memberChallengeRepository;
        this.challengeMapper = challengeMapper;
        this.savingsService = savingsService;
        this.challengeRewardService = challengeRewardService;
        this.challengeService = challengeService1;
        this.accountService = accountService;
    }

    private Challenge findChallengeById(UUID challengeId) {
        return challengeRepository.findChallengeById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
    }

    private List<Challenge> findAllChallengesByMemberId(UUID memberId) {
        return memberChallengeRepository.findChallengesByMemberId(memberId);
    }

    public MemberChallengesResponse findAllMemberChallengesByMemberId(UUID memberId) {

        int totalCalculatedNum = memberChallengeRepository.countAllByMemberIdAndStatus(memberId, MemberChallengeStatus.CALCULATED);

        List<MemberChallengeDetail> memberChallengeDetails = findByMemberId(memberId)
                .stream()
                .map(challengeMapper::toMemberChallengeResponse)
                .toList();

        return new MemberChallengesResponse(
                totalCalculatedNum,
                memberChallengeDetails
        );
    }

    /**
     * 회원의 챌린지 계좌 생성 메서드
     * - 챌린지 계정 하나당 하나의 챌린지 계좌 존재
     */
    @Transactional
    public MakeAccountResponse makeMemberChallengeAccount(UUID memberId) {
        Member member = memberService.getMemberById(memberId);
        if (member.hasChallengeAccount()) {
            throw new MemberChallengeAccountAlreadyExistsException(memberId);
        }

        return accountService.makeChallengeAccount(
                new MakeAccountRequest(
                        memberId,
                        memberChallengeAccountCode
                )
        );
    }

    /**
     * 회원의 챌린지 참여 메서드
     * - 챌린지 시작 전, 챌린지 계좌가 존재하는 회원에 한해 참여가 가능 (중복 참여 불가)
     * - 적금의 경우만 별도의 적금 가입 로직 필요
     *
     * @param request 회원 ID, 챌린지 ID, 예치금
     */
    @Transactional
    public void joinChallenge(JoinChallengeRequest request) {
        Member member = memberService.getMemberById(request.getMemberId());
        Challenge challenge = findChallengeById(request.getChallengeId());
        Long deposit = request.getDeposit();

        validateChallengeJoin(challenge, member);
        validateDeposit(deposit);

        // 예치금 입금 (유저 챌린지 계좌 -> 챌린지 전용 계좌)
        transferDeposit(member, challenge.getAccount().getAccountNo(), deposit);

        MemberChallenge memberChallenge = createMemberChallenge(member, challenge, deposit);
        challenge.addMember(memberChallenge);
        member.addChallenge(memberChallenge);
        memberChallengeRepository.save(memberChallenge);
    }

    private void validateDeposit(Long deposit) {
        if (deposit == null) {
            throw new IllegalArgumentException("Deposit amount cannot be null");
        }

        if (deposit < minDeposit) {
            throw new IllegalArgumentException("Deposit amount must be at least " + minDeposit + " won");
        }

        if (deposit > maxDeposit) {
            throw new IllegalArgumentException("Deposit amount cannot exceed " + maxDeposit + " won");
        }

        if (deposit % depositUnit != 0) {
            throw new IllegalArgumentException("Deposit amount must be in units of " + depositUnit + " won");
        }
    }

    private void validateChallengeJoin(Challenge challenge, Member member) {
        // 이미 시작된 챌린지 참여 불가
        if (LocalDate.now().isAfter(challenge.getStartDate())) {
            throw new ChallengeAlreadyStartedException(challenge.getId());
        }

        // 챌린지 계정이 없는 경우 참여 불가
        if (!member.hasChallengeAccount()) {
            throw new MemberChallengeAccoutNotFoundException(member.getId());
        }

        // 이미 참여 중인 챌린지 참여 불가
        Optional<Challenge> existingChallenge = memberChallengeRepository.findChallengesByMemberId(member.getId())
                .stream()
                .filter(findChallenge -> findChallenge.getId().equals(challenge.getId()))
                .findAny();

        if (existingChallenge.isPresent()) {
            throw new ChallengeAlreadyJoinedException(challenge.getId(), member.getId());
        }

        String accountName = challenge.getType().toString() + challenge.getStartDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
        // 적금 챌린지에서 적금 계좌가 없는 경우 참여 불가
        if (challenge.getType() == ChallengeType.SAVINGS_SEVEN) {
            Optional<SavingAccountsDetail> memberSavingAccount = savingsService.findMemberSavingAccountByAccountName(
                    member.getId(),
                    accountName);

            if (memberSavingAccount.isEmpty()) {
                throw new SavingAccountNotFoundException(challenge.getTitle());
            }
        }
    }

    @Transactional
    public void transferDeposit(Member member, String challengeAccountNo, Long deposit) {
        try {
            accountService.accountTransfer(
                    new TransferRequest(
                            member.getId(),
                            challengeAccountNo,
                            member.getChallengeAccount().getAccountNo(),
                            deposit,
                            TransferType.CHALLENGE
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new CannotJoinChallengeException();
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

        // 적금 챌린지의 경우 별도의 해지 로직 수행
        if (challenge.getType() == ChallengeType.SAVINGS_SEVEN) {
            withdrawChallenge(challengeId, memberId);
            return;
        }

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
        String accountName = challenge.getType().toString() + challenge.getStartDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
        Optional<SavingAccountsDetail> savingChallengeAccount = savingsService.getAllSavingsByMemberId(memberId)
                .stream()
                .filter(savings -> savings.getAccountName().equals(accountName))
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

    public MemberChallengeDetail findChallengeByChallengeIdAndMemberId(UUID challengeId, UUID memberId) {
        return memberChallengeRepository.findChallengeByMemberIdAndChallengeId(memberId, challengeId)
                .orElseThrow(() -> new MemberChallengeNotFoundException(challengeId, memberId));
    }

    public MemberChallengesResponse findAllChallengesByMemberIdAndStatus(UUID memberId, ChallengeStatus status) {

        int totalCalculatedNum = memberChallengeRepository.countAllByMemberIdAndStatus(memberId, MemberChallengeStatus.CALCULATED);

        List<MemberChallengeDetail> memberChallengeDetails = memberChallengeRepository.findAllByMemberIdAndChallengeStatus(memberId, status)
                .stream()
                .map(challengeMapper::toMemberChallengeResponse)
                .toList();

        return new MemberChallengesResponse(
                totalCalculatedNum,
                memberChallengeDetails
        );
    }

    @Transactional(readOnly = true)
    public ScoreDetailResponse getChallengeScoreDetail(UUID memberId, UUID challengeId) {
        MemberChallenge memberChallenge = memberChallengeRepository.findMemberChallengeByChallengeIdAndMemberId(challengeId, memberId)
                .orElseThrow(() -> new MemberChallengeNotFoundException(challengeId, memberId));

        List<DailyScoreDetailResponse> dailyScores = memberChallenge.getDailyScores().stream()
                .map(this::convertToDailyScoreDetail)
                .sorted(Comparator.comparing(DailyScoreDetailResponse::getDate).reversed())
                .toList();

        return new ScoreDetailResponse(memberChallenge.getTotalScore(), dailyScores);
    }

    private DailyScoreDetailResponse convertToDailyScoreDetail(DailyScore dailyScore) {
        return DailyScoreDetailResponse.builder()
                .date(dailyScore.getDate())
                .entries(dailyScore.getScoreDetails())
                .build();
    }

    public List<MemberChallenge> findByMemberId(UUID memberId) {
        return memberChallengeRepository.findByMemberId(memberId);
    }

    public MemberChallenge findByMemberIdAndChallengeType(UUID memberId, ChallengeType challengeType) {
        return memberChallengeRepository.findByMemberIdAndChallengeType(memberId, challengeType)
                .orElseThrow(() -> new MemberChallengeNotFoundException(memberId, challengeType));
    }

    public List<MemberChallenge> findAllByChallengeTypeAndStatus(ChallengeType challengeType, ChallengeStatus challengeStatus) {
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

    public ChallengeStatusCountResponse getChallengeStatusCount(UUID memberId) {

        int scheduledCount = 0;
        int inProgressCount = 0;
        int completedCount = 0;

        List<Challenge> challenges = findAllChallengesByMemberId(memberId);
        for (Challenge challenge : challenges) {
            switch (challenge.getStatus()) {
                case SCHEDULED -> scheduledCount++;
                case IN_PROGRESS -> inProgressCount++;
                case COMPLETED -> completedCount++;
            }
        }

        return ChallengeStatusCountResponse.of(scheduledCount, inProgressCount, completedCount);
    }
}