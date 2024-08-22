package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.global.exception.BaseException;

import java.util.Map;
import java.util.UUID;

public class ChallengeCannotWithdrawException extends BaseException {

    public ChallengeCannotWithdrawException(ChallengeType type) {
        super(
                "CHALLENGE_CANNOT_WITHDRAW_01",
                "중도해지가 불가한 챌린지입니다.",
                Map.of("challengeType", String.valueOf(type))
        );
    }

}
