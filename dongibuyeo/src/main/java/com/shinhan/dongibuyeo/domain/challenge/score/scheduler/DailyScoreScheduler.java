package com.shinhan.dongibuyeo.domain.challenge.score.scheduler;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.challenge.service.DailyScoreService;
import com.shinhan.dongibuyeo.domain.savings.service.SavingsService;
import com.shinhan.dongibuyeo.global.entity.TransferType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class DailyScoreScheduler {

    private final DailyScoreService dailyScoreService;
    private final SavingsService savingsService;

    public DailyScoreScheduler(DailyScoreService dailyScoreService, SavingsService savingsService) {
        this.dailyScoreService = dailyScoreService;
        this.savingsService = savingsService;
    }

    /**
     * 적금 납입 여부를 조회하여 점수 계산 / 적금 해지
     * 적금 자동이체 시간: 오전 6시 30분
     */
    @Scheduled(cron = "0 31 6 * * ?")
    public void checkSavingsDeposits() {

        // TODO 챌린지 적금계좌 여부 확인 후 진행
//        List<Member> membersWithSavings = getMembersWithSavingsDeposit();
//        for (Member member : membersWithSavings) {
//            int depositCount = getSavingsDepositCount(member);
//            int score = calculateScoreForSavings(depositCount);
//            dailyScoreService.addScore(member.getId(), score, "Savings deposit");
//        }
    }

    @Scheduled(cron = "1 10,14 * * * *") // 10:01, 14:01에 실행
    public void checkCoffeeFeverTime() {
        checkFeverTime(ChallengeType.CONSUMPTION_COFFEE, TransferType.COFFEE);
    }

    @Scheduled(cron = "1 0 0 * * SAT") // 토요일 00:01에 실행 (금요일 체크)
    public void checkFridayAlcoholFeverTime() {
        dailyScoreService.rewardNonConsumptionDuringFeverTime(ChallengeType.CONSUMPTION_DRINK, TransferType.DRINK);
    }

    @Scheduled(cron = "1 0 0 * * SUN") // 일요일 00:01에 실행 (토요일 체크)
    public void checkSaturdayAlcoholFeverTime() {
        dailyScoreService.rewardNonConsumptionDuringFeverTime(ChallengeType.CONSUMPTION_DRINK, TransferType.DRINK);
    }
    @Scheduled(cron = "1 2 * * * *") // 매시 02:01에 실행
    public void checkDeliveryFeverTime() {
        checkFeverTime(ChallengeType.CONSUMPTION_DELIVERY, TransferType.DELIVERY);
    }

    private void checkFeverTime(ChallengeType challengeType, TransferType transferType) {
        LocalDateTime now = LocalDateTime.now().minusMinutes(1); // 1분 전 시간으로 체크
        if (FeverTimeChecker.isFeverTime(challengeType, now)) {
            log.info("Checking non-consumption for challenge type: {}", challengeType);
            dailyScoreService.rewardNonConsumptionDuringFeverTime(challengeType, transferType);
        }
    }

}