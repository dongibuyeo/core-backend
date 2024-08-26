package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.BaseException;

import java.util.Map;
import java.util.UUID;

public class ChallengeNotEndedException extends BaseException {

    public ChallengeNotEndedException(UUID challengeId) {
        super(
                "CHALLENGE_NOT_ENDED_01",
                "종료되지 않은 챌린지입니다.",
                Map.of("challengeId", String.valueOf(challengeId))
        );
    }

}
