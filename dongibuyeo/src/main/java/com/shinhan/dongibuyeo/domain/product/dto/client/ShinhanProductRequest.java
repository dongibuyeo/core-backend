package com.shinhan.dongibuyeo.domain.product.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shinhan.dongibuyeo.global.header.GlobalAdminHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShinhanProductRequest {
    @JsonProperty("Header")
    private GlobalAdminHeader header;
    private String bankCode;
    private String accountName;
    private String accountDescription;
}
