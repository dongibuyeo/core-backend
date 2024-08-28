package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import com.shinhan.dongibuyeo.domain.member.dto.response.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeRankResponse {

    private UUID challengeId;

    private Integer top10PercentCutoff;
    private List<TopRankerInfo> top5Members;
}
