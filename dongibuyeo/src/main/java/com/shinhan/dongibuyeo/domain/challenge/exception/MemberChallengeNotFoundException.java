package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
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

    public MemberChallengeNotFoundException(UUID memberId, ChallengeType challengeType) {
        super(
                "NOT_FOUND_MEMBER_CHALLENGE_02",
                "해당 회원과 챌린지 타입에 부합한 챌린지 참여 이력을 찾을 수 없습니다.",
                Map.of("memberId", String.valueOf(memberId), "challengeType", String.valueOf(challengeType))
        );
    }
}
