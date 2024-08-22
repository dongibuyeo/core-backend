package com.shinhan.dongibuyeo.domain.account.dto.response;

import com.shinhan.dongibuyeo.domain.account.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MakeAccountResponse {
    private UUID accountId;
    private String accountNo;
    private AccountType accountType;
}
