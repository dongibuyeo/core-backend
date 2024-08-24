package com.shinhan.dongibuyeo.domain.account.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionHistoryRequest {
    private UUID memberId;
    private String accountNo;
    private String startDate;
    private String endDate;
    private String transactionType;
    private String orderByType;
}
