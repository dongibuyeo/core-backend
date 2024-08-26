package com.shinhan.dongibuyeo.domain.savings.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.domain.savings.dto.response.SavingAccounts;
import com.shinhan.dongibuyeo.global.header.GlobalAdminHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanGetSavingAccountsResponse {
    @JsonProperty("Header")
    private GlobalAdminHeader header;

    private String totalCount;

    @JsonProperty("REC")
    private SavingAccounts rec;
}
