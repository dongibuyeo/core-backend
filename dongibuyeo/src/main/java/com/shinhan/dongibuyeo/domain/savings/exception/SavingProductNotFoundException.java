package com.shinhan.dongibuyeo.domain.savings.exception;

import com.shinhan.dongibuyeo.global.exception.NotFoundException;

import java.util.Map;

public class SavingProductNotFoundException extends NotFoundException {

    public SavingProductNotFoundException(String accountName) {
        super(
                "SAVING_PRODUCT_NOT_FOUND_01",
                "해당 적금 상품이 존재하지 않습니다",
                Map.of("accountName", accountName)
        );
    }

}
