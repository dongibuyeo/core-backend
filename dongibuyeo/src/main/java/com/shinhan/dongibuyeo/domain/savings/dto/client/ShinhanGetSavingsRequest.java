package com.shinhan.dongibuyeo.domain.savings.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.global.header.GlobalUserHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanGetSavingsRequest {
    @JsonProperty("Header")
    private GlobalUserHeader header;
}
