package com.shinhan.dongibuyeo.domain.savings.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.global.header.GlobalAdminHeader;
import com.shinhan.dongibuyeo.global.header.GlobalUserHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanMakeSavingRequest {
    @JsonProperty("Header")
    GlobalAdminHeader header;
    private String bankCode;
    private String accountName;
    private String accountDescription;
    private Long minSubscriptionBalance;
    private String subscriptionPeriod;
    private Long maxSubscriptionBalance;
    private double interestRate;
    private String rateDescription;
}