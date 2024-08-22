package com.shinhan.dongibuyeo.domain.challenge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinChallengeRequest {

    private UUID challengeId;
    private UUID memberId;
    private Long deposit;
}
