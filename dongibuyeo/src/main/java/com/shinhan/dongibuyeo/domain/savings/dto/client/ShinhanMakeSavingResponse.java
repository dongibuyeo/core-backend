package com.shinhan.dongibuyeo.domain.savings.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.domain.savings.dto.response.SavingInfo;
import com.shinhan.dongibuyeo.global.header.GlobalResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanMakeSavingResponse {
    @JsonProperty("Header")
    GlobalResponseHeader header;
    @JsonProperty("REC")
    SavingInfo rec;
}
