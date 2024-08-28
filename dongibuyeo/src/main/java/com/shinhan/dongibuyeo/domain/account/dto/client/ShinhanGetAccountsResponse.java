package com.shinhan.dongibuyeo.domain.account.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.domain.account.dto.response.AccountDetailInfo;
import com.shinhan.dongibuyeo.global.header.GlobalUserHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanGetAccountsResponse {
    @JsonProperty("Header")
    private GlobalUserHeader header;
    @JsonProperty("REC")
    private List<AccountDetailInfo> rec;
}
