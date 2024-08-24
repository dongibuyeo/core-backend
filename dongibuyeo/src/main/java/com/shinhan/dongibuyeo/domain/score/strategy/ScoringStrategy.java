package com.shinhan.dongibuyeo.domain.score.strategy;

import com.shinhan.dongibuyeo.domain.account.dto.response.TransactionHistory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public interface ScoringStrategy {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    Map<String, Integer> calculateScore(List<TransactionHistory> transactions, LocalDate date);
}