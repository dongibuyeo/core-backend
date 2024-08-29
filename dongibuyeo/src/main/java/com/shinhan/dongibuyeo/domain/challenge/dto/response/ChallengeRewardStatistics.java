package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeRewardStatistics {
    private Long top10PercentCount;
    private Long top10PercentAdditionalRewardSum;
    private Long lower90PercentCount;
    private Long lower90PercentCountAdditionalRewardSum;
}
