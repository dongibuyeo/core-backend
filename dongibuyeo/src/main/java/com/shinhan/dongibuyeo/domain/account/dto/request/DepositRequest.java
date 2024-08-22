package com.shinhan.dongibuyeo.domain.account.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequest {
    private UUID memberId;
    private String accountNo;
    private Long transactionBalance;
}
