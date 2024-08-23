package com.shinhan.dongibuyeo.domain.account.exception;

import com.shinhan.dongibuyeo.global.exception.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class AccountNotFoundException extends NotFoundException {

    public AccountNotFoundException(UUID accountId) {
        super(
                "NOT_FOUND_ACCOUNT_01",
                "계좌를 찾을 수 없습니다.",
                Map.of("accountId", String.valueOf(accountId))
        );
    }
}
