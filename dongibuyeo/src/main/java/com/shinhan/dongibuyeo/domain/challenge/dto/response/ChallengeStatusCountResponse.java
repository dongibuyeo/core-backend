package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeStatusCountResponse {
    private int scheduledCount;
    private int inProgressCount;
    private int completedCount;

    public static ChallengeStatusCountResponse of(int scheduledCount, int inProgressCount, int completedCount) {
        return ChallengeStatusCountResponse.builder()
                .scheduledCount(scheduledCount)
                .inProgressCount(inProgressCount)
                .completedCount(completedCount)
                .build();
    }
}