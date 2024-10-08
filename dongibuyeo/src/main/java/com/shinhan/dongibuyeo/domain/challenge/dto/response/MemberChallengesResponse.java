package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MemberChallengesResponse {

    private int totalCalculatedNum;
    private List<MemberChallengeDetail> memberChallengeDetails;
}
