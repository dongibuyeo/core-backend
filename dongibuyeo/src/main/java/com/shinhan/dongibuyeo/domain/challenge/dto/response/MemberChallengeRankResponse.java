package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class MemberChallengeRankResponse {

    private UUID memberId;
    private UUID challengeId;

    private Integer totalScore;
    private Double percentileRank;
    private Integer top10PercentCutoff;

}
