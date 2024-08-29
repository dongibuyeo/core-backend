package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdditionalRewardResponse {
    private long totalReward; // 총 상금
    private long interestEarned; // 이자
    private long remainingFromFailures; // 잔여 예치금
    private long top10PercentRewardPerUnit; // 상위 10% 상금
    private long lower90PercentRewardPerUnit; // 하위 90% 상금

    private int top10PercentMemberNum;
    private int lower90PercentMemberNum;
}
