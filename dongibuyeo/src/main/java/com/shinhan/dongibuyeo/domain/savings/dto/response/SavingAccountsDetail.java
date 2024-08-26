package com.shinhan.dongibuyeo.domain.savings.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SavingAccountsDetail {
    private String bankCode;
    private String bankName;
    private String userName;
    private String accountNo;
    private String accountName;
    private String accountDescription;
    private String withdrawalBankCode;
    private String withdrawalBankName;
    private String withdrawalAccountNo;
    private String subscriptionPeriod;
    private Long depositBalance;
    private double interestRate;
    private String installmentNumber;
    private Long totalBalance;
    private Long accountCreateDate;
    private Long accountExpiryDate;
}
