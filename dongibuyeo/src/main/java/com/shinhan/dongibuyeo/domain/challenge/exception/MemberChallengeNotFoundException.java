package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class MemberChallengeNotFoundException extends NotFoundException {

    public MemberChallengeNotFoundException(UUID challengeId, UUID memberId) {
        super(
                "NOT_FOUND_MEMBER_CHALLENGE_01",
                "해당 회원에 부합한 챌린지 참여 이력을 찾을 수 없습니다.",
                Map.of("challengeId", String.valueOf(challengeId), "memberId", String.valueOf(memberId))
        );
    }
}
