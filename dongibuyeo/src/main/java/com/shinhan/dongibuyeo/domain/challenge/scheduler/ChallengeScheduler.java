package com.shinhan.dongibuyeo.domain.challenge.scheduler;

import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import com.shinhan.dongibuyeo.domain.challenge.service.ChallengeRewardService;
import com.shinhan.dongibuyeo.domain.challenge.service.ScoreCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class ChallengeScheduler {

    private final ChallengeRepository challengeRepository;
    private final ChallengeRewardService challengeRewardService;
    private final ScoreCalculationService scoreCalculationService;

    public ChallengeScheduler(ChallengeRepository challengeRepository,
                              ChallengeRewardService challengeRewardService,
                              ScoreCalculationService scoreCalculationService) {
        this.challengeRepository = challengeRepository;
        this.challengeRewardService = challengeRewardService;
        this.scoreCalculationService = scoreCalculationService;
    }

    /**
     * 모든 챌린지 상태 변경 메서드
     * 매일 자정 1분 후 실행
     */
    @Scheduled(cron = "0 1 0 * * ?")
    @Transactional
    public void updateAllChallengeStatuses() {
        LocalDate today = LocalDate.now();
        log.info("Starting daily challenge status update for date: {}", today);

        challengeRepository.findAll()
                .forEach(Challenge::updateStatus);
    }

    /**
     * 완료 챌린지 정산
     * 매일 새벽 4시 실행 (일일 점수 계산 이후)
     */
    @Scheduled(cron = "0 0 4 * * ?")
    @Transactional
    public void checkAndCompleteEndedChallenges() {
        log.info("Starting to check for ended challenges");
        LocalDate today = LocalDate.now();

        List<Challenge> endedChallenges = challengeRepository.findChallengesByEndDate(today);

        for (Challenge challenge : endedChallenges) {
            try {
                log.info("Processing ended challenge: {}", challenge.getId());
                challengeRewardService.processChallengeRewards(challenge.getId());
                challenge.updateStatus();
                challengeRepository.save(challenge);
                log.info("Challenge {} completed and rewards processed", challenge.getId());
            } catch (Exception e) {
                log.error("Error processing challenge {}: {}", challenge.getId(), e.getMessage(), e);
            }
        }

        log.info("Finished checking for ended challenges");
    }

    /**
     * 일일 점수 계산
     * 매일 새벽 3시 실행
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void calculateDailyScores() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("Starting daily score calculation for date: {}", yesterday);
        try {
            scoreCalculationService.calculateDailyScores(yesterday);
            log.info("Completed daily score calculation for date: {}", yesterday);
        } catch (Exception e) {
            log.error("Error calculating daily scores for date {}: {}", yesterday, e.getMessage(), e);
        }
    }
}