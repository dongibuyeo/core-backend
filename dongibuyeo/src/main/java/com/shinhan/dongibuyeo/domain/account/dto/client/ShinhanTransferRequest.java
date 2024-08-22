package com.shinhan.dongibuyeo.domain.account.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.global.header.GlobalUserHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanTransferRequest {
    @JsonProperty("Header")
    private GlobalUserHeader header;
    private String depositAccountNo;
    private String depositTransactionSummary;
    private Long transactionBalance;
    private String withdrawalAccountNo;
    private String withdrawalTransactionSummary;

    @Override
    public String toString() {
        return "ShinhanTransferRequest{" +
                "header=" + header +
                ", depositAccountNo='" + depositAccountNo + '\'' +
                ", depositTransactionSummary='" + depositTransactionSummary + '\'' +
                ", transactionBalance=" + transactionBalance +
                ", withdrawalAccountNo='" + withdrawalAccountNo + '\'' +
                ", withdrawalTransactionSummary='" + withdrawalTransactionSummary + '\'' +
                '}';
    }
}
