package com.shinhan.dongibuyeo.domain.savings.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SavingPaymentInfo {
    private String bankCode;
    private String bankName;
    private String accountNo;
    private String accountName;
    private double interestRate;
    private Long depositBalance;
    private Long totalBalance;
    private String accountCreateDate;
    private String accountExpireDate;

    @JsonProperty("paymentInfo")
    private List<PaymentInfo> paymentInfo;
}
