package com.shinhan.dongibuyeo.domain.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DepositResponse {
    private String transactionUniqueNo;
    private String transactionDate;
}
