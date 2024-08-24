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
public class ConsumptionCoffeeStrategy implements ScoringStrategy{

    @Override
    public Map<String, Integer> calculateScore(List<TransactionHistory> transactions, LocalDate date) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("DEFAULT", 10);

        boolean consumedMorning = false;
        boolean consumedAfternoon = false;

        for (TransactionHistory transaction : transactions) {
            if (!transaction.getTransactionDate().equals(date.format(DateTimeFormatter.BASIC_ISO_DATE)) ||
                    !transaction.getTransactionSummary().equals(TransferType.COFFEE.name())) {
                continue;
            }

            LocalDateTime transactionDateTime = LocalDateTime.parse(transaction.getTransactionDate() + transaction.getTransactionTime(), formatter);
            int hour = transactionDateTime.getHour();

            if (hour >= 7 && hour < 10) {
                consumedMorning = true;
            } else if (hour >= 11 && hour < 14) {
                consumedAfternoon = true;
            }

            if (consumedMorning && consumedAfternoon) {
                break; // 두 시간대 모두 소비했다면 더 이상 검사할 필요 없음
            }
        }

        if (!consumedMorning) scores.put("NO_COFFEE_7_10", 2);
        if (!consumedAfternoon) scores.put("NO_COFFEE_11_14", 3);
        if (consumedMorning || consumedAfternoon) scores.put("COFFEE_CONSUMED", -5);

        return scores;
    }
}
