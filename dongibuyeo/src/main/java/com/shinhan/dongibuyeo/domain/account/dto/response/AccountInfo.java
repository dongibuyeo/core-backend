package com.shinhan.dongibuyeo.domain.account.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
    private String bankCode;
    private String accountNo;
    @JsonProperty("currency")
    private Currency currency;
}
