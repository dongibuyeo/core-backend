package com.shinhan.dongibuyeo.domain.challenge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CancelJoinChallengeRequest {

    private UUID memberId;
}
