package com.shinhan.dongibuyeo.domain.account.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.domain.account.dto.response.AccountDetail;
import com.shinhan.dongibuyeo.global.header.GlobalResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanMakeAccountResponse {
    @JsonProperty("Header")
    GlobalResponseHeader header;
    @JsonProperty("REC")
    AccountDetail rec;
}
