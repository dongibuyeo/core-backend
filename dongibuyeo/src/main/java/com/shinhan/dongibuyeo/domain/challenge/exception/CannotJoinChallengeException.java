package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.global.exception.BaseException;

import java.util.Map;
import java.util.UUID;

public class CannotJoinChallengeException extends BaseException {

    public CannotJoinChallengeException() {
        super(
                "CANNOT_JOIN_CHALLENGE_01",
                "예치금 입금에 실패하였습니다."
        );
    }
}
