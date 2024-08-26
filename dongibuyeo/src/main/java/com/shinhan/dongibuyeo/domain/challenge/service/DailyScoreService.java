package com.shinhan.dongibuyeo.domain.challenge.service;

import com.shinhan.dongibuyeo.domain.challenge.entity.DailyScore;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.ScoreDetail;
import com.shinhan.dongibuyeo.domain.challenge.repository.DailyScoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
public class DailyScoreService {

    private final DailyScoreRepository dailyScoreRepository;

    public DailyScoreService(DailyScoreRepository dailyScoreRepository) {
        this.dailyScoreRepository = dailyScoreRepository;
    }

    @Transactional
    public DailyScore getOrCreateDailyScore(MemberChallenge memberChallenge, LocalDate date) {
        Optional<DailyScore> existingDailyScore = dailyScoreRepository
                .findByMemberChallengeAndDate(memberChallenge, date);

        if (existingDailyScore.isPresent()) {
            log.info("DailyScore already exists for date: {} and memberChallenge: {}",
                    date, memberChallenge.getId());
            return existingDailyScore.get();
        }

        DailyScore newDailyScore = new DailyScore(date);
        newDailyScore.updateScoreDetails(new ScoreDetail("Daily Base Score", 10, 10));
        memberChallenge.addDailyScore(newDailyScore);

        log.info("Created new DailyScore for date: {} and memberChallenge: {}",
                date, memberChallenge.getId());
        return dailyScoreRepository.save(newDailyScore);
    }
}
