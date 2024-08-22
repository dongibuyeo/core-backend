package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class ChallengeNotFoundException extends NotFoundException {

    public ChallengeNotFoundException() {
        super(
                "NOT_FOUND_MEMBER_01",
                "아이디에 부합한 챌린지를 찾을 수 없습니다."
        );
    }

    public ChallengeNotFoundException(UUID challengeId) {
        super(
                "NOT_FOUND_MEMBER_02",
                "아이디에 부합한 챌린지를 찾을 수 없습니다.",
                Map.of("challengeId", String.valueOf(challengeId))
        );
    }
}
