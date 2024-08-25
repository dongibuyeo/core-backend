package com.shinhan.dongibuyeo.domain.challenge.strategy;

import com.shinhan.dongibuyeo.domain.account.dto.response.TransactionHistory;
import com.shinhan.dongibuyeo.global.entity.TransferType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConsumptionDeliveryStrategy implements ScoringStrategy {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public Map<String, Integer> calculateScore(List<TransactionHistory> transactions, LocalDate date) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("DEFAULT", 10);

        boolean consumedLateNight = false;

        LocalDateTime startDateTime = date.atTime(21, 0);
        LocalDateTime endDateTime = date.plusDays(1).atTime(2, 0);

        for (TransactionHistory transaction : transactions) {
            LocalDateTime transactionDateTime = LocalDateTime.parse(transaction.getTransactionDate() + transaction.getTransactionTime(), formatter);

            if (!transaction.getTransactionSummary().equals(TransferType.DELIVERY.name())) {
                continue;
            }

            if ((transactionDateTime.isAfter(startDateTime) || transactionDateTime.equals(startDateTime))
                    && transactionDateTime.isBefore(endDateTime)) {
                consumedLateNight = true;
                break;
            }
        }

        if (!consumedLateNight) {
            scores.put("NO_DELIVERY_21_2", 5);
        } else {
            scores.put("DELIVERY_CONSUMED", -5);
        }

        return scores;
    }
}