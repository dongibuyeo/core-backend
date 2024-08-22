package com.shinhan.dongibuyeo.domain.account.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.global.header.GlobalUserHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanDepositRequest {
    @JsonProperty("Header")
    private GlobalUserHeader header;
    private String accountNo;
    private Long transactionBalance;
    private String transactionSummary;
}
