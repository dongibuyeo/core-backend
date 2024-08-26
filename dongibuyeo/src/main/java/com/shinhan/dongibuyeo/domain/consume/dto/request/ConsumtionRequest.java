package com.shinhan.dongibuyeo.domain.consume.dto.request;

import com.shinhan.dongibuyeo.domain.account.dto.request.TransactionHistoryRequest;
import com.shinhan.dongibuyeo.global.entity.TransferType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumtionRequest {
    private TransferType transferType;
    private TransactionHistoryRequest history;
}
