package com.shinhan.dongibuyeo.domain.member.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanMemberRequest {
    private String apiKey;
    private String userId;
}
