package com.shinhan.dongibuyeo.domain.account.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.domain.account.dto.response.TransferResponse;
import com.shinhan.dongibuyeo.global.header.GlobalResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanTransferResponse {
    @JsonProperty("Header")
    private GlobalResponseHeader header;
    @JsonProperty("REC")
    private List<TransferResponse> rec;
}
