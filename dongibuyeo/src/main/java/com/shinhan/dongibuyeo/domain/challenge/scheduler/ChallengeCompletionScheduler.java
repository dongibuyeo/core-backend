package com.shinhan.dongibuyeo.domain.challenge.scheduler;

import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.repository.ChallengeRepository;
import com.shinhan.dongibuyeo.domain.challenge.service.ChallengeRewardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class ChallengeCompletionScheduler {

    private final ChallengeRepository challengeRepository;
    private final ChallengeRewardService challengeRewardService;

    public ChallengeCompletionScheduler(ChallengeRepository challengeRepository, ChallengeRewardService challengeRewardService) {
        this.challengeRepository = challengeRepository;
        this.challengeRewardService = challengeRewardService;
    }

    @Scheduled(cron = "0 0 1 * * ?") // 매일 새벽 1시에 실행
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