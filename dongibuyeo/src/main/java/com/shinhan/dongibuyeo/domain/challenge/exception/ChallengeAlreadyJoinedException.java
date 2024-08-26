package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.BaseException;

import java.util.Map;
import java.util.UUID;

public class ChallengeAlreadyJoinedException extends BaseException {

    public ChallengeAlreadyJoinedException(UUID challengeId, UUID memberId) {
        super(
                "CHALLENGE_ALREADY_JOINED_01",
                "이미 참여 이력이 있는 챌린지입니다.",
                Map.of("challengeId", String.valueOf(challengeId), "memberId", String.valueOf(memberId))
        );
    }
}
