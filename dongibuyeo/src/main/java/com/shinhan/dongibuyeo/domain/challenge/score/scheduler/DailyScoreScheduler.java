package com.shinhan.dongibuyeo.domain.challenge.score.scheduler;

import com.shinhan.dongibuyeo.domain.challenge.entity.*;
import com.shinhan.dongibuyeo.domain.challenge.service.DailyScoreService;
import com.shinhan.dongibuyeo.domain.challenge.service.MemberChallengeService;
import com.shinhan.dongibuyeo.domain.savings.dto.response.PaymentInfo;
import com.shinhan.dongibuyeo.domain.savings.dto.response.SavingPaymentInfo;
import com.shinhan.dongibuyeo.domain.savings.service.SavingsService;
import com.shinhan.dongibuyeo.global.entity.TransferType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Component
@EnableScheduling
@Slf4j
public class DailyScoreScheduler {

    private final DailyScoreService dailyScoreService;
    private final MemberChallengeService memberChallengeService;
    private final SavingsService savingsService;

    public DailyScoreScheduler(DailyScoreService dailyScoreService, MemberChallengeService memberChallengeService, SavingsService savingsService) {
        this.dailyScoreService = dailyScoreService;
        this.memberChallengeService = memberChallengeService;
        this.savingsService = savingsService;
    }

    /**
     * 적금 납입 여부를 조회하여 점수 계산 / 적금 해지
     * 적금 자동이체 시간: 오전 6시 30분
     */
    @Transactional
    @Scheduled(cron = "0 31 6 * * ?")
    public void checkSavingsDeposits() {

        List<MemberChallenge> memberChallenges = memberChallengeService.findAllByChallengeTypeAndStatus(ChallengeType.SAVINGS_SEVEN, ChallengeStatus.IN_PROGRESS);
        for (MemberChallenge memberChallenge : memberChallenges) {
            UUID memberId = memberChallenge.getMember().getId();
            Challenge challenge = memberChallenge.getChallenge();
            String accountName = challenge.getType().toString() + challenge.getStartDate().format(DateTimeFormatter.ofPattern("yyMMdd"));

            List<SavingPaymentInfo> savingPayment = savingsService.getSavingPayment(memberId, accountName);
            try {
                List<PaymentInfo> paymentInfoList = savingPayment.get(0).getPaymentInfo();
                PaymentInfo todayPayment = paymentInfoList.get(paymentInfoList.size() - 1);
                LocalDate today = LocalDate.now();
                LocalDate paymentDate = LocalDate.parse(todayPayment.getPaymentDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));

                if (paymentDate.equals(today) && "SUCCESS".equals(todayPayment.getStatus())) {
                    // 오늘 납입이 성공한 경우 5점 추가
                    DailyScore todayScore = dailyScoreService.getOrCreateDailyScore(memberChallenge, today);
                    int currentScore = todayScore.getTotalScore();
                    ScoreDetail scoreDetail = new ScoreDetail("DAILY SAVINGS", +5, currentScore + 5);
                    todayScore.addScoreDetail(scoreDetail);
                    memberChallenge.addDailyScore(todayScore);
                } else {
                    memberChallengeService.withdrawChallenge(challenge.getId(), memberId);
                }
            } catch (Exception e) {
                //TODO 어떻게 처리할지 고민
                log.info("[checkSavingsDeposits] 적금 챌린지 점수 계산 중 오류 발생: memberChallengeId:{}, error:{}", memberChallenge.getId(), e.getMessage());
            }
        }
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