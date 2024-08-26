package com.shinhan.dongibuyeo.domain.savings.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo {
    private String depositInstallment;
    private Long paymentBalance;
    private String paymentDate;
    private String paymentTime;
    private String status;
    private String failureReason;
}
