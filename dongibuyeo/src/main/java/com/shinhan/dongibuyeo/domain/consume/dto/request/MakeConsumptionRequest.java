package com.shinhan.dongibuyeo.domain.consume.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MakeConsumptionRequest {
    private UUID memberId;
    private String transactionUniqueNo;
    private String transactionDate;
    private String transactionTime;
    private String transactionType;
    private String transactionTypeName;
    private String transactionAccountNo;
    private Long transactionBalance;
    private Long transactionAfterBalance;
    private String transactionSummary;
    private String transactionMemo;
}
