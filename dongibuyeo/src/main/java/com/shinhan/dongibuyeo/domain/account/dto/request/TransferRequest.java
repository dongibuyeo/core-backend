package com.shinhan.dongibuyeo.domain.account.dto.request;

import com.shinhan.dongibuyeo.global.entity.TransferType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    private UUID memberId;
    private String depositAccountNo;
    private String withdrawalAccountNo;
    private Long transactionBalance;
    private TransferType transferType;
}
