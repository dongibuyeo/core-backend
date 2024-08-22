package com.shinhan.dongibuyeo.domain.account.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.domain.account.dto.response.TransactionHistorys;
import com.shinhan.dongibuyeo.global.header.GlobalResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanTransactionHistoryResponse {
    @JsonProperty("Header")
    private GlobalResponseHeader header;
    @JsonProperty("REC")
    private TransactionHistorys rec;
}
