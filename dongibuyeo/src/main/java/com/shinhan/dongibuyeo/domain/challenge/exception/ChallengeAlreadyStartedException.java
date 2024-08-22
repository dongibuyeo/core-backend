package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.BaseException;

import java.util.Map;
import java.util.UUID;

public class ChallengeAlreadyStartedException extends BaseException {

    public ChallengeAlreadyStartedException(UUID challengeId) {
        super(
                "CHALLENGE_ALREADY_STARTED_01",
                "이미 시작된 챌린지입니다.",
                Map.of("challengeId", String.valueOf(challengeId))
        );
    }

}
