package com.shinhan.dongibuyeo.domain.savings.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MakeSavingAccountRequest {
    private UUID memberId;
    private String withdrawalAccountNo;
    private String accountTypeUniqueNo;
    private Long depositBalance;
}
