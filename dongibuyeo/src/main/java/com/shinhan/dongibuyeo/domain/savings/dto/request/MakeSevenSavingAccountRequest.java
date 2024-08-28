package com.shinhan.dongibuyeo.domain.savings.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MakeSevenSavingAccountRequest {
    private String challengeType;
    private String startDate;
    private UUID memberId;
    private String withdrawalAccountNo;
    private Long depositBalance;
}
