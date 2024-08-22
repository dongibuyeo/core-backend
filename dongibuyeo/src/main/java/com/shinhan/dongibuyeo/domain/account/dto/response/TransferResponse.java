package com.shinhan.dongibuyeo.domain.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {
    private String transactionUniqueNo;
    private String accountNo;
    private String transactionDate;
    private String transactionType;
    private String transactionTypeName;
    private String transactionAccountNo;
}
