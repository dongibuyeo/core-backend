package com.shinhan.dongibuyeo.domain.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MakeProductRequest {
    private String bankCode;
    private String accountName;
    private String accountDescription;
}
