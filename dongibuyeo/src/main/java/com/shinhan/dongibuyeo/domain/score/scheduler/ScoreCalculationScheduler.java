package com.shinhan.dongibuyeo.domain.score.scheduler;

import com.shinhan.dongibuyeo.domain.score.service.ScoreCalculationService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@EnableScheduling
public class ScoreCalculationScheduler {

    private final ScoreCalculationService scoreCalculationService;

    public ScoreCalculationScheduler(ScoreCalculationService scoreCalculationService) {
        this.scoreCalculationService = scoreCalculationService;
    }

    @Scheduled(cron = "0 0 3 * * ?")  // 매일 새벽 3시에 실행
    public void calculateDailyScores() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        scoreCalculationService.calculateScoresForDate(yesterday);
    }
}