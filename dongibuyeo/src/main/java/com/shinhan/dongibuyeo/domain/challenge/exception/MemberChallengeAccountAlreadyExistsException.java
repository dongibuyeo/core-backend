package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.BaseException;

import java.util.Map;
import java.util.UUID;

public class MemberChallengeAccountAlreadyExistsException extends BaseException {

    public MemberChallengeAccountAlreadyExistsException(UUID memberId) {
        super(
                "MEMBER_CHALLENGE_ACCOUNT_ALREADY_EXISTS_01",
                "이미 챌린지 계좌가 존재하는 회원입니다.",
                Map.of("memberId", String.valueOf(memberId))
        );
    }

}
