package com.shinhan.dongibuyeo.domain.challenge.score.scheduler;

import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import com.shinhan.dongibuyeo.domain.challenge.service.ChallengeRewardService;
import com.shinhan.dongibuyeo.domain.challenge.service.DailyScoreService;
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
    private final DailyScoreService dailyScoreService;

    public ChallengeScheduler(ChallengeRepository challengeRepository, ChallengeRewardService challengeRewardService, DailyScoreService dailyScoreService) {
        this.challengeRepository = challengeRepository;
        this.challengeRewardService = challengeRewardService;
        this.dailyScoreService = dailyScoreService;
    }

    /**
     * 매일 자정 모든 챌린지 상태 변경 및 일일 점수 생성
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateChallengeStatusesAndCreateDailyScores() {
        LocalDate today = LocalDate.now();
        log.info("Starting daily challenge update and score creation for date: {}", today);

        challengeRepository.findAll().forEach(challenge -> {
            // 챌린지 상태 변경
            challenge.updateStatus();

            // 진행 중인 챌린지 일일 점수 생성
            if (challenge.getStatus() == ChallengeStatus.IN_PROGRESS) {
                challenge.getChallengeMembers().forEach(memberChallenge -> {
                    try {
                        dailyScoreService.getOrCreateDailyScore(memberChallenge, today);
                    } catch (Exception e) {
                        log.error("Error creating daily score for challenge member: {}", memberChallenge.getId(), e);
                    }
                });
            }
        });
    }

    /**
     * 완료 챌린지 정산
     * 매일 새벽 4시 실행 (최종 피버타임 이후)
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


}