package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class DailyScoreDetail {
    private LocalDate date;
    private int dailyTotalScore;
    private Map<String, Integer> scoreDetails;
}
