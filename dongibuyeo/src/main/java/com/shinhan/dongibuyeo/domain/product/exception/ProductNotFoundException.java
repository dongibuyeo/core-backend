package com.shinhan.dongibuyeo.domain.product.exception;

import com.shinhan.dongibuyeo.global.exception.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException() {
        super(
                "NOT_FOUND_PRODUCT_01",
                "상품을 찾을 수 없습니다."
        );
    }
}
