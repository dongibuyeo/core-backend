package com.shinhan.dongibuyeo.domain.savings.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.domain.savings.dto.response.SavingInfo;
import com.shinhan.dongibuyeo.domain.savings.dto.response.SavingPaymentInfo;
import com.shinhan.dongibuyeo.global.header.GlobalUserHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanGetSavingPaymentResponse {
    @JsonProperty("Header")
    private GlobalUserHeader header;
    @JsonProperty("REC")
    private List<SavingPaymentInfo> rec;
}
