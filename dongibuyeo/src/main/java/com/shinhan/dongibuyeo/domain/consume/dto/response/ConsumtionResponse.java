package com.shinhan.dongibuyeo.domain.consume.dto.response;

import com.shinhan.dongibuyeo.global.entity.TransferType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumtionResponse {
    private TransferType transferType;
    private Long totalConsumtion;
}
