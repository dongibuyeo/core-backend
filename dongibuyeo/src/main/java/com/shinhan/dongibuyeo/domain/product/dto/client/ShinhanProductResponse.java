package com.shinhan.dongibuyeo.domain.product.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.domain.product.dto.response.MakeProductResponse;
import com.shinhan.dongibuyeo.global.header.GlobalResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanProductResponse {
    @JsonProperty("Header")
    private GlobalResponseHeader header;
    @JsonProperty("REC")
    private MakeProductResponse rec;
}
