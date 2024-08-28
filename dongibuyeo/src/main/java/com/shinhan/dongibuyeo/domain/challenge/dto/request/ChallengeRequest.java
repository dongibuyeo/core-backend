package com.shinhan.dongibuyeo.domain.challenge.dto.request;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeRequest {

    private ChallengeType type;
    private String startDate;
    private String endDate;
    private String title;
    private String description;
    private String image;
}
