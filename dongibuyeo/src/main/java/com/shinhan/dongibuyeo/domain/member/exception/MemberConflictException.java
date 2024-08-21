package com.shinhan.dongibuyeo.domain.member.exception;

import com.shinhan.dongibuyeo.global.exception.ConflictException;
import com.shinhan.dongibuyeo.global.exception.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class MemberConflictException extends ConflictException {

    public MemberConflictException(String email) {
        super(
                "CONFLICT_MEMBER_01",
                "이미 존재하는 회원입니다.",
                Map.of("memberId", String.valueOf(email))
        );
    }

}
