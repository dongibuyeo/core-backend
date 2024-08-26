package com.shinhan.dongibuyeo.domain.savings.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteSavingInfo {
    private String status;
    private String bankCode;
    private String bankName;
    private String accountNo;
    private String accountName;
    private Long totalBalance;
    private Long earlyTerminationInterest;
    private Long earlyTerminationBalance;
    private String earlyTerminationDate;
}
