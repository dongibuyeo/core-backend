package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeStatus;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeResponse {

    private UUID challengeId;
    private ChallengeType type;
    private ChallengeStatus status;
    private String accountNo;
    private String startDate;
    private String endDate;

    private String title;
    private String description;

    private Long totalDeposit;
    private Integer participants;

}
