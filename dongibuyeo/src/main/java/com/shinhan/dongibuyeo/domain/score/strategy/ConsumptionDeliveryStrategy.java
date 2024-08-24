package com.shinhan.dongibuyeo.domain.score.strategy;

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

    @Override
    public Map<String, Integer> calculateScore(List<TransactionHistory> transactions, LocalDate date) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("DEFAULT", 10);

        boolean consumedLateNight = false;

        for (TransactionHistory transaction : transactions) {
            if (!transaction.getTransactionDate().equals(date.format(DateTimeFormatter.BASIC_ISO_DATE)) ||
                    !transaction.getTransactionSummary().equals(TransferType.DELIVERY.name())) {
                continue;
            }

            LocalDateTime transactionDateTime = LocalDateTime.parse(transaction.getTransactionDate() + transaction.getTransactionTime(), formatter);
            int hour = transactionDateTime.getHour();

            if (hour >= 21 || hour < 2) {
                consumedLateNight = true;
                break;  // 21-2시 사이 배달음식을 주문했다면 루프 종료
            }
        }

        if (!consumedLateNight) scores.put("NO_DELIVERY_21_2", 5);
        if (consumedLateNight) scores.put("DELIVERY_CONSUMED", -5);

        return scores;
    }
}
