package com.shinhan.dongibuyeo.domain.challenge.strategy;

import com.shinhan.dongibuyeo.domain.account.dto.response.TransactionHistory;
import com.shinhan.dongibuyeo.global.entity.TransferType;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConsumptionDrinkStrategy implements ScoringStrategy {

    @Override
    public Map<String, Integer> calculateScore(List<TransactionHistory> transactions, LocalDate date) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("DEFAULT", 10);

        boolean consumedAlcohol = false;

        for (TransactionHistory transaction : transactions) {
            if (!transaction.getTransactionDate().equals(date.format(DateTimeFormatter.BASIC_ISO_DATE)) ||
                    !transaction.getTransactionSummary().equals(TransferType.DRINK.name())) {
                continue;
            }

            consumedAlcohol = true;
            break;  // 하루 중 한 번이라도 술을 마셨다면 루프 종료
        }

        if (date.getDayOfWeek() == DayOfWeek.FRIDAY && !consumedAlcohol) scores.put("NO_ALCOHOL_FRIDAY", 5);
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY && !consumedAlcohol) scores.put("NO_ALCOHOL_SATURDAY", 5);
        if (consumedAlcohol) scores.put("ALCOHOL_CONSUMED", -5);

        return scores;
    }
}
