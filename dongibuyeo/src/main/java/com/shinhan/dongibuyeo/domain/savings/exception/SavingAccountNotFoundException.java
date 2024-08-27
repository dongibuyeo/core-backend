package com.shinhan.dongibuyeo.domain.savings.exception;

import com.shinhan.dongibuyeo.global.exception.BaseException;

import java.util.Map;

public class SavingAccountNotFoundException extends BaseException {

    public SavingAccountNotFoundException(String accountName) {
        super(
                "SAVING_ACCOUNT_NOT_FOUND_01",
                "해당 적금 계좌가 존재하지 않습니다",
                Map.of("accountName", accountName)
        );
    }

}
