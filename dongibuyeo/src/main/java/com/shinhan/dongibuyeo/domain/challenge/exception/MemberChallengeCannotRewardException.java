package com.shinhan.dongibuyeo.domain.challenge.exception;

import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallengeStatus;
import com.shinhan.dongibuyeo.global.exception.BaseException;

import java.util.Map;
import java.util.UUID;

public class MemberChallengeCannotRewardException extends BaseException {

    public MemberChallengeCannotRewardException(MemberChallengeStatus status) {
        super(
                "MEMBER_CHALLENGE_CANNOT_REWARD_01",
                "환급 가능한 상태가 아닙니다.",
                Map.of("status", String.valueOf(status))
        );
    }

}
