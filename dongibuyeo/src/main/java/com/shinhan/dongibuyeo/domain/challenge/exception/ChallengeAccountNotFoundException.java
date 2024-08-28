package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class ChallengeAccountNotFoundException extends NotFoundException {

    public ChallengeAccountNotFoundException(UUID memberId) {
        super(
                "CHALLENGE_ACCOUNT_NOT_FOUND_01",
                "챌린지 계좌가 존재하지 않습니다.",
                Map.of("memberId", String.valueOf(memberId))
        );
    }

}
