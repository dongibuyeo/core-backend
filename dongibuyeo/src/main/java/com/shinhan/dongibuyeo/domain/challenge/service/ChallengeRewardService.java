package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.account.dto.request.TransactionHistoryRequest;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallengeStatus;
import com.shinhan.dongibuyeo.domain.consume.dto.request.ConsumtionRequest;
import com.shinhan.dongibuyeo.domain.consume.dto.response.ConsumtionResponse;
import com.shinhan.dongibuyeo.domain.consume.service.ConsumeService;
import com.shinhan.dongibuyeo.global.entity.TransferType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ChallengeRewardService {

    private final ChallengeService challengeService;
    private final ConsumeService consumeService;

    public ChallengeRewardService(ChallengeService challengeService, ConsumeService consumeService) {
        this.challengeService = challengeService;
        this.consumeService = consumeService;
    }


    /**
     * 종료된 소비 챌린지의 환급 처리 메서드
     * 기본 환급액과 추가 환급액을 계산
     *
     * @param challengeId 종료된 챌린지의 UUID
     */
    @Transactional
    public void processConsumptionChallengeRewards(UUID challengeId) {
        Challenge challenge = challengeService.findChallengeById(challengeId);

        List<MemberChallenge> memberChallenges = challenge.getChallengeMembers().stream()
                .filter(memberChallenge -> memberChallenge.getStatus().equals(MemberChallengeStatus.BEFORE_CALCULATION))
                .toList();

        if (!memberChallenges.isEmpty()) {
            long totalDepositPool = calculateTotalDepositPool(memberChallenges);
            getConsumptionBaseRewards(memberChallenges, challenge);
            getConsumptionAdditionalRewards(memberChallenges, totalDepositPool);
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

            long currentPeriodConsumption = getTotalConsumption(memberId, challengeStartDate, challengeEndDate, transferType);
            long previousPeriodConsumption = getTotalConsumption(memberId, previousPeriodStartDate, previousPeriodEndDate, transferType);

            boolean isSuccess = currentPeriodConsumption < previousPeriodConsumption;
            long baseReward = calculateBaseReward(isSuccess, memberChallenge, previousPeriodConsumption, currentPeriodConsumption);

            memberChallenge.updateSuccessStatus(isSuccess);
            memberChallenge.updateBaseReward(baseReward);
            memberChallenge.updateStatus(MemberChallengeStatus.CALCULATED);
        }
    }

    private long getTotalConsumption(UUID memberId, LocalDate startDate, LocalDate endDate, TransferType transferType) {
        ConsumtionRequest request = createConsumtionRequest(memberId, startDate, endDate, transferType);
        ConsumtionResponse response = consumeService.getTotalConsumtion(request);
        return response.getTotalConsumtion();
    }

    private ConsumtionRequest createConsumtionRequest(UUID memberId, LocalDate startDate, LocalDate endDate, TransferType transferType) {
        TransactionHistoryRequest historyRequest = TransactionHistoryRequest.builder()
                .memberId(memberId)
                .startDate(startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .endDate(endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();

        return new ConsumtionRequest(transferType, historyRequest);
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

    private long calculateTotalDepositPool(List<MemberChallenge> memberChallenges) {
        return memberChallenges.stream()
                .mapToLong(MemberChallenge::getDeposit)
                .sum();
    }

    /**
     * 추가 환급금 계산 메서드
     * - 상위 10%의 유저들과 상위 90%유저들이 상금의 50%씩을 분배받는다.
     * 단, 환급금 분배의 경우 각 그룹 내 해당 유저의 예치금 비율에 따라 산정된다.
     */
    private void getConsumptionAdditionalRewards(List<MemberChallenge> memberChallenges, long totalDepositPool) {
        long remainingPool = calculateRemainingPool(memberChallenges, totalDepositPool);
        long topPerformersPool = remainingPool / 2;
        long othersPool = remainingPool - topPerformersPool;

        List<MemberChallenge> sortedChallenges = memberChallenges.stream()
                .sorted(Comparator.comparingInt(MemberChallenge::getTotalScore).reversed())
                .toList();

        int topPerformersCount = (int) Math.ceil(sortedChallenges.size() * 0.1);
        distributeRewardsToGroup(sortedChallenges, 0, topPerformersCount, topPerformersPool);
        distributeRewardsToGroup(sortedChallenges, topPerformersCount, sortedChallenges.size(), othersPool);
    }


    private void distributeRewardsToGroup(List<MemberChallenge> sortedChallenges, int startIndex, int endIndex, long rewardPool) {
        long groupDepositTotal = calculateGroupDepositTotal(sortedChallenges, startIndex, endIndex);

        for (int i = startIndex; i < endIndex; i++) {
            MemberChallenge memberChallenge = sortedChallenges.get(i);
            long additionalReward = (rewardPool * memberChallenge.getDeposit()) / groupDepositTotal;
            memberChallenge.updateAdditionalReward(additionalReward);
        }
    }

    private long calculateGroupDepositTotal(List<MemberChallenge> sortedChallenges, int startIndex, int endIndex) {
        return sortedChallenges.subList(startIndex, endIndex).stream()
                .mapToLong(MemberChallenge::getDeposit)
                .sum();
    }

    private long calculateRemainingPool(List<MemberChallenge> memberChallenges, long totalDepositPool) {
        long totalBaseRewards = memberChallenges.stream()
                .mapToLong(MemberChallenge::getBaseReward)
                .sum();
        return totalDepositPool - totalBaseRewards;
    }
}