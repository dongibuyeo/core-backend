package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.BaseException;

import java.util.Map;
import java.util.UUID;

public class ChallengeAlreadyStartedException extends BaseException {

    public ChallengeAlreadyStartedException(UUID challengeId) {
        super(
                "CHALLENGE_ALREADY_STARTED_01",
                "이미 챌린지가 시작되어 취소할 수 없습니다.",
                Map.of("challengeId", String.valueOf(challengeId))
        );
    }

}
