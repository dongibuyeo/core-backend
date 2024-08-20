package com.shinhan.dongibuyeo.domain.member.exception;

import com.shinhan.dongibuyeo.global.exception.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException(UUID memberId) {
        super(
                "NOT_FOUND_MEMBER_01",
                "아이디에 부합한 회원을 찾을 수 없습니다.",
                Map.of("memberId", String.valueOf(memberId))
        );
    }

    public MemberNotFoundException(String email) {
        super(
                "NOT_FOUND_MEMBER_02",
                "이메일에 부합한 회원을 찾을 수 없습니다.",
                Map.of("memberId", String.valueOf(email))
        );
    }
}
