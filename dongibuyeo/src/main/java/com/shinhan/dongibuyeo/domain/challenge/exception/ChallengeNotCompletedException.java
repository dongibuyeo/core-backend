package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class ChallengeNotCompletedException extends NotFoundException {

    public ChallengeNotCompletedException(UUID memberId) {
        super(
                "CHALLENGE_NOT_COMPLETED_01",
                "챌린지가 아직 종료되지 않았습니다.",
                Map.of("challengeId", String.valueOf(memberId))
        );
    }

}
