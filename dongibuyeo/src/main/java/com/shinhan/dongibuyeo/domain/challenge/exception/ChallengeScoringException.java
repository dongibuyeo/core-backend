package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.BaseException;

import java.util.Map;
import java.util.UUID;

public class ChallengeScoringException extends BaseException {

    public ChallengeScoringException(UUID scoreId) {
        super(
                "SCORING_PARSING_EXCEPTION_01",
                "점수 상세 변환 중 오류가 발생했습니다.",
                Map.of("scoreId", String.valueOf(scoreId))
        );
    }

}
