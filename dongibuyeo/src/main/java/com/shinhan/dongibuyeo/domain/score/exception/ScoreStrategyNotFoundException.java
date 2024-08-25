package com.shinhan.dongibuyeo.domain.score.exception;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.global.exception.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class ScoreStrategyNotFoundException extends NotFoundException {

    public ScoreStrategyNotFoundException(ChallengeType challengeType) {
        super(
                "NOT_FOUND_SCORE_STRATEGY_01",
                "챌린지 타입에 부합한 점수 계산 전략이 없습니다.",
                Map.of("challengeType", String.valueOf(challengeType))
        );
    }
}
