package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.account.service.AccountService;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeNotEndedException;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeNotFoundException;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import com.shinhan.dongibuyeo.domain.challenge.repository.MemberChallengeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ChallengeRewardService {

    private final ChallengeRepository challengeRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    private final AccountService accountService;
    private final ScoreCalculationService scoreCalculationService;

    public ChallengeRewardService(ChallengeRepository challengeRepository,
                                  MemberChallengeRepository memberChallengeRepository,
                                  AccountService accountService, ScoreCalculationService scoreCalculationService) {
        this.challengeRepository = challengeRepository;
        this.memberChallengeRepository = memberChallengeRepository;
        this.accountService = accountService;
        this.scoreCalculationService = scoreCalculationService;
    }

    @Transactional
    public void processChallengeRewards(UUID challengeId) {
        Challenge challenge = findAndValidateChallenge(challengeId);
        List<MemberChallenge> memberChallenges = challenge.getChallengeMembers();
        long totalDepositPool = calculateTotalDepositPool(memberChallenges);

        processBaseRewards(memberChallenges, challenge);
        distributeAdditionalRewards(memberChallenges, totalDepositPool);
    }

    private Challenge findAndValidateChallenge(UUID challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));

        if (challenge.getStatus() == ChallengeStatus.IN_PROGRESS) {
            throw new ChallengeNotEndedException(challengeId);
        }
        return challenge;
    }

    private void processBaseRewards(List<MemberChallenge> memberChallenges, Challenge challenge) {
        for (MemberChallenge memberChallenge : memberChallenges) {
            // TODO 직전달, 이번달 소비량 계산 로직 추가
            long previousMonthConsumption = 0L;
            long currentMonthConsumption = 0L;

            boolean isSuccess = currentMonthConsumption < previousMonthConsumption;
            long baseReward = calculateBaseReward(memberChallenge, previousMonthConsumption, currentMonthConsumption);

            memberChallenge.updateSuccessStatus(isSuccess);
            memberChallenge.updateBaseReward(baseReward);
        }
    }

    private long calculateBaseReward(MemberChallenge memberChallenge, long previousMonthConsumption, long currentMonthConsumption) {
        if (memberChallenge.getIsSuccess()) {
            return memberChallenge.getDeposit();
        }

        double increaseRate = calculateIncreaseRate(previousMonthConsumption, currentMonthConsumption);
        return memberChallenge.getDeposit() - (long)(memberChallenge.getDeposit() * increaseRate);
    }

    private double calculateIncreaseRate(long previousMonthConsumption, long currentMonthConsumption) {
        if (previousMonthConsumption == 0) {
            return 1.0;
        }

        double increaseRate = (double) (currentMonthConsumption - previousMonthConsumption) / previousMonthConsumption;
        return Math.max(0.0, Math.min(increaseRate, 1.0));
    }

    private long calculateTotalDepositPool(List<MemberChallenge> memberChallenges) {
        return memberChallenges.stream()
                .mapToLong(MemberChallenge::getDeposit)
                .sum();
    }

    private void distributeAdditionalRewards(List<MemberChallenge> memberChallenges, long totalDepositPool) {
        long remainingPool = calculateRemainingPool(memberChallenges, totalDepositPool);
        long topPerformersPool = remainingPool / 2;
        long othersPool = remainingPool - topPerformersPool;

        List<MemberChallenge> sortedChallenges = memberChallenges.stream()
                .sorted(Comparator.comparingInt(scoreCalculationService::calculateTotalScore).reversed())
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