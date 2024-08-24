package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.BaseException;

import java.util.Map;
import java.util.UUID;

public class ChallengeCannotJoinException extends BaseException {

    public ChallengeCannotJoinException(UUID memberId) {
        super(
                "CHALLENGE_CANNOT_JOIN_01",
                "챌린지에 참여할 수 없습니다.",
                Map.of("memberId", String.valueOf(memberId))
        );
    }

}
