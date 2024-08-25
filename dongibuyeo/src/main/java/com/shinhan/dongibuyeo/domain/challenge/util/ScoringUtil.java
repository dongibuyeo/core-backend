package com.shinhan.dongibuyeo.domain.challenge.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.dongibuyeo.domain.challenge.entity.DailyScore;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import com.shinhan.dongibuyeo.domain.challenge.exception.ChallengeScoringException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ScoringUtil {

    private final ObjectMapper objectMapper;

    public ScoringUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public int calculateTotalScore(MemberChallenge memberChallenge) {
        return memberChallenge.getDailyScores().stream()
                .mapToInt(this::calculateDailyScore)
                .sum();
    }

    public int calculateDailyScore(DailyScore dailyScore) {
        try {
            Map<String, Integer> scoreDetails = objectMapper.readValue(dailyScore.getScoreDetails(), new TypeReference<Map<String, Integer>>() {});
            return scoreDetails.values().stream().mapToInt(Integer::intValue).sum();
        } catch (JsonProcessingException e) {
            throw new ChallengeScoringException(dailyScore.getId());
        }
    }
}
