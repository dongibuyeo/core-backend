package com.shinhan.dongibuyeo.domain.account.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MakeAccountRequest {
    private UUID memberId;
    private String accountTypeUniqueNo;
}
