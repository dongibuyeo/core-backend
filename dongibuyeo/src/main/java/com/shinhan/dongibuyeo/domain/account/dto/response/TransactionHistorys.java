package com.shinhan.dongibuyeo.domain.account.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistorys {
    private String totalCount;
    @JsonProperty("list")
    private List<TransactionHistory> transactions;
}
