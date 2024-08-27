package com.shinhan.dongibuyeo.domain.savings.exception;

import com.shinhan.dongibuyeo.global.exception.BaseException;

import java.util.Map;

public class SavingAccountAlreadyExistsException extends BaseException {

    public SavingAccountAlreadyExistsException(String accountName) {
        super(
                "SAVING_ACCOUNT_ALREADY_EXIST_01",
                "해당 적금 계좌가 이미 존재합니다",
                Map.of("accountName", accountName)
        );
    }

}
