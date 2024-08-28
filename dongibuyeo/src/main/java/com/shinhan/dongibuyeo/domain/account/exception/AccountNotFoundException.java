package com.shinhan.dongibuyeo.domain.account.exception;

import com.shinhan.dongibuyeo.global.exception.NotFoundException;

import java.util.Map;
import java.util.UUID;

public class AccountNotFoundException extends NotFoundException {

    public AccountNotFoundException(UUID accountId) {
        super(
                "NOT_FOUND_ACCOUNT_01",
                "해당 ID로 계좌를 찾을 수 없습니다.",
                Map.of("accountId", String.valueOf(accountId))
        );
    }

    public AccountNotFoundException(String accountNo) {
        super(
                "NOT_FOUND_ACCOUNT_02",
                "해당 계좌번호로 계좌를 찾을 수 없습니다.",
                Map.of("accountNo", accountNo)
        );
    }
}
