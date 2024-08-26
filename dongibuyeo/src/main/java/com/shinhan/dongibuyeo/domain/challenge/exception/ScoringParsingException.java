package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.BaseException;

import java.util.Map;
import java.util.UUID;

public class ScoringParsingException extends BaseException {

    public ScoringParsingException(UUID scoreId) {
        super(
                "SCORING_PARSING_EXCEPTION",
                "점수 상세 파싱 중 오류가 발생했습니다.",
                Map.of("scoreId", String.valueOf(scoreId))
        );
    }

}
