package com.shinhan.dongibuyeo.domain.savings.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SavingAccountInfo {
    private String bankCode;
    private String bankName;
    private String accountNo;
    private String accountName;
    private String withdrawalBankCode;
    private String withdrawalAccountNo;
    private String subscriptionPeriod;
    private Long depositBalance;
    private double interestRate;
    private String accountCreateDate;
    private String accountExpiryDate;
}
