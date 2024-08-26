package com.shinhan.dongibuyeo.domain.savings.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SavingAccounts {

    private String totalCount;

    @JsonProperty("list")
    private List<SavingAccountsDetail> savingDetails;
}
