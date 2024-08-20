package com.shinhan.dongibuyeo.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class MemberLoginResponse {
    private UUID memberId;
    private String nickname;
    private String profileImage;
}
