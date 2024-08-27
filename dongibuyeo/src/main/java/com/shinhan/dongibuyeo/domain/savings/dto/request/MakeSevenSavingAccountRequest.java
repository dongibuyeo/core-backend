package com.shinhan.dongibuyeo.domain.savings.dto.request;

import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MakeSevenSavingAccountRequest {
    private String challengeType;
    private LocalDate startDate;
    private UUID memberId;
    private String withdrawalAccountNo;
    private Long depositBalance;
}
