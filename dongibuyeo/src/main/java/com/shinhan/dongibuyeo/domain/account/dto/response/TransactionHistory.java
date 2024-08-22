package com.shinhan.dongibuyeo.domain.account.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistory {
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
