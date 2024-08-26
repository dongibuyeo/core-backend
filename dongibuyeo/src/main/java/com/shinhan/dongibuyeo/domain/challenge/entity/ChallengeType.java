package com.shinhan.dongibuyeo.domain.challenge.entity;

import com.shinhan.dongibuyeo.global.entity.TransferType;

import java.util.Arrays;
import java.util.List;

public enum ChallengeType {
    CONSUMPTION_COFFEE(TransferType.COFFEE),
    CONSUMPTION_DRINK(TransferType.DRINK),
    CONSUMPTION_DELIVERY(TransferType.DELIVERY),
    SAVINGS_SEVEN(TransferType.SEVEN),
    QUIZ_SOLBEING(TransferType.QUIZ);

    private final TransferType transferType;

    ChallengeType(TransferType transferType) {
        this.transferType = transferType;
    }

    public TransferType getTransferType() {
        return this.transferType;
    }

    public boolean isConsumptionType() {
        return this.name().startsWith("CONSUMPTION_");
    }

    public static List<ChallengeType> getConsumptionTypes() {
        return Arrays.stream(ChallengeType.values())
                .filter(ChallengeType::isConsumptionType)
                .toList();
    }
}