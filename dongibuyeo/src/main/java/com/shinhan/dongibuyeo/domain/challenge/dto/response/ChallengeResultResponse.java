package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeResultResponse {
    private Double totalReward; // 총 상금
    private Double top10PercentRewardPerUnit; // 상위 10% 상금
    private Double lower90PercentRewardPerUnit; // 하위 90% 상금
}
