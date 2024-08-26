package com.shinhan.dongibuyeo.domain.savings.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.domain.savings.dto.response.SavingAccountInfo;
import com.shinhan.dongibuyeo.global.header.GlobalResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanDeleteSavingResponse {
    @JsonProperty("Header")
    GlobalResponseHeader header;
    @JsonProperty("REC")
    SavingAccountInfo rec;
}
