package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class MemberChallengeAccoutNotFoundException extends NotFoundException {

    public MemberChallengeAccoutNotFoundException(UUID memberId) {
        super(
                "MEMBER_CHALLENGE_ACCOUNT_NOT_FOUND_01",
                "챌린지 계좌가 없어 챌린지에 참여할 수 없습니다.",
                Map.of("memberId", String.valueOf(memberId))
        );
    }

}
