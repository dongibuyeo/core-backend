package com.shinhan.dongibuyeo.domain.challenge.dto.request;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeRequest {

    private ChallengeType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String description;
    private String image;
}
