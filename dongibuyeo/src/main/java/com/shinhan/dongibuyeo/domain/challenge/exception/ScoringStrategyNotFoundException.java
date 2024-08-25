package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.global.exception.NotFoundException;

import java.util.Map;

public class ScoringStrategyNotFoundException extends NotFoundException {

    public ScoringStrategyNotFoundException(ChallengeType challengeType) {
        super(
                "NOT_FOUND_SCORING_STRATEGY_01",
                "챌린지 타입에 부합한 점수 계산 전략이 없습니다.",
                Map.of("challengeType", String.valueOf(challengeType))
        );
    }
}
