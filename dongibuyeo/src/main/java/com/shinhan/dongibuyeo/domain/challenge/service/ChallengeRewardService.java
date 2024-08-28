package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.account.dto.request.TransferRequest;
import com.shinhan.dongibuyeo.domain.account.entity.Account;
import com.shinhan.dongibuyeo.domain.account.service.AccountService;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.AdditionalRewardResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.score.util.ScoreUtils;
import com.shinhan.dongibuyeo.domain.consume.service.ConsumeService;
import com.shinhan.dongibuyeo.domain.member.dto.response.MemberResponse;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.global.entity.TransferType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ChallengeRewardService {

    @Value("${shinhan.challenge.interest-rate}")
    private double challengeInterestRate;

    @Value("${shinhan.challenge.money-unit}")
    private double moneyUnit;

    private final ConsumeService consumeService;
    private final AccountService accountService;
    private final MemberService memberService;

    public ChallengeRewardService(ConsumeService consumeService, AccountService accountService, MemberService memberService) {
        this.consumeService = consumeService;
        this.accountService = accountService;
        this.memberService = memberService;
    }

    /**
     * 종료된 소비 챌린지의 환급 처리 메서드
     * 기본 환급액과 추가 환급액을 계산
     *
     * @param challenge 작일 종료된 챌린지
     */
    @Transactional
    public void processConsumptionChallengeRewards(Challenge challenge) {
        List<MemberChallenge> memberChallenges = challenge.getChallengeMembers().stream()
                .filter(memberChallenge -> memberChallenge.getStatus().equals(MemberChallengeStatus.BEFORE_CALCULATION))
                .toList();

        if (!memberChallenges.isEmpty()) {
            getConsumptionBaseRewards(memberChallenges, challenge);
            getConsumptionAdditionalRewards(memberChallenges, challenge.getTotalDeposit());
        }
    }

    /**
     * 소비 챌린지의 기본 환급액 조회 메서드
     * - 챌린지 성공 여부 판별 후 기본 환급액 계산
     */
    private void getConsumptionBaseRewards(List<MemberChallenge> memberChallenges, Challenge challenge) {

        LocalDate challengeStartDate = challenge.getStartDate();
        LocalDate challengeEndDate = challenge.getEndDate();

        // 챌린지 기간과 동일한 길이의 직전 기간 계산
        long challengeDuration = ChronoUnit.DAYS.between(challengeStartDate, challengeEndDate);
        LocalDate previousPeriodStartDate = challengeStartDate.minusDays(challengeDuration);
        LocalDate previousPeriodEndDate = challengeStartDate.minusDays(1);

        TransferType transferType = challenge.getType().getTransferType();

        for (MemberChallenge memberChallenge : memberChallenges) {
            UUID memberId = memberChallenge.getMember().getId();

            long currentPeriodConsumption = consumeService.getTotalConsumption(memberId, challengeStartDate, challengeEndDate, transferType);
            long previousPeriodConsumption = consumeService.getTotalConsumption(memberId, previousPeriodStartDate, previousPeriodEndDate, transferType);

            boolean isSuccess = currentPeriodConsumption < previousPeriodConsumption;
            long baseReward = calculateBaseReward(isSuccess, memberChallenge, previousPeriodConsumption, currentPeriodConsumption);

            memberChallenge.updateSuccessStatus(isSuccess);
            memberChallenge.updateBaseReward(baseReward);
        }
    }

    private long calculateBaseReward(boolean isSuccess, MemberChallenge memberChallenge, long previousMonthConsumption, long currentMonthConsumption) {
        if (isSuccess) {
            return memberChallenge.getDeposit();
        }

        // 실패시 성공률에 따른 기본 환급액 계산
        double increaseRate = calculateIncreaseRate(previousMonthConsumption, currentMonthConsumption);
        return memberChallenge.getDeposit() - (long) (memberChallenge.getDeposit() * increaseRate);
    }

    private double calculateIncreaseRate(long prev, long cur) {
        if (prev == 0) {
            return 1.0;
        }

        double increaseRate = (double) (cur - prev) / prev;
        return Math.max(0.0, Math.min(increaseRate, 1.0));
    }

    /**
     * 추가 환급금 계산 메서드
     * - 성공 유저 중 상위 10%의 유저들과 상위 90% 유저들이 상금의 50%씩을 배분받는다.
     * - ChallengeService의 calculateEstimatedReward 메서드를 활용하여 만원 단위의 추가 환급금을 구한 뒤 계산
     */
    private void getConsumptionAdditionalRewards(List<MemberChallenge> memberChallenges, long totalDeposit) {
        List<MemberChallenge> successMembersOrderByScore = memberChallenges.stream()
                .filter(MemberChallenge::getIsSuccess)
                .sorted(Comparator.comparingInt(MemberChallenge::getTotalScore).reversed())
                .toList();

        int totalCountOfSuccessMember = successMembersOrderByScore.size();
        int top10PercentMemberNum = totalCountOfSuccessMember / 10;
        int lower90PercentMemberNum = totalCountOfSuccessMember - top10PercentMemberNum;

        long leftDeposit = calculateRemainingPool(memberChallenges, totalDeposit);

        AdditionalRewardResponse additionalReward = ScoreUtils.calculateEstimatedAdditionalRewardPerUnit(
                challengeInterestRate,
                totalDeposit,
                leftDeposit,
                top10PercentMemberNum,
                lower90PercentMemberNum
        );

        distributeRewardsToGroup(successMembersOrderByScore, 0, top10PercentMemberNum, additionalReward.getTop10PercentRewardPerUnit());
        distributeRewardsToGroup(successMembersOrderByScore, top10PercentMemberNum, totalCountOfSuccessMember, additionalReward.getLower90PercentRewardPerUnit());
    }

    private void distributeRewardsToGroup(List<MemberChallenge> memberChallenges, int startIndex, int endIndex, long rewardPerUnit) {
        for (int i = startIndex; i < endIndex; i++) {
            MemberChallenge memberChallenge = memberChallenges.get(i);
            long additionalReward = (long) Math.floor(rewardPerUnit * memberChallenge.getDeposit() / moneyUnit);
            memberChallenge.updateAdditionalReward(additionalReward);
            memberChallenge.updateStatus(MemberChallengeStatus.CALCULATED);
        }
    }

    private long calculateRemainingPool(List<MemberChallenge> memberChallenges, long totalDepositPool) {
        long totalBaseRewards = memberChallenges.stream()
                .mapToLong(MemberChallenge::getBaseReward)
                .sum();
        return totalDepositPool - totalBaseRewards;
    }

    /**
     * 챌린지 계좌 -> 유저 챌린지 계좌 환급 메서드
     *
     * @param member    환급 대상 회원
     * @param challenge 환급 챌린지
     * @param deposit   환급금
     */
    public void transferFromChallengeAccountToMemberAccount(Member member, Challenge challenge, Long deposit) {
        MemberResponse adminMember = memberService.findAdminMember();
        Account memberChallengeAccount = member.getChallengeAccount();
        log.info("[환급 메서드] memberAccount: {}, challengeAccount: {}", memberChallengeAccount.getAccountNo(), challenge.getAccount().getAccountNo());
        accountService.accountTransfer(new TransferRequest(
                adminMember.getMemberId(),
                memberChallengeAccount.getAccountNo(),
                challenge.getAccount().getAccountNo(),
                deposit,
                TransferType.CHALLENGE));
    }
}