package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import com.shinhan.dongibuyeo.domain.challenge.entity.ScoreDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@AllArgsConstructor
public class DailyScoreDetailResponse {
    private String date;
    private List<ScoreDetail> entries;

    @Builder
    public DailyScoreDetailResponse(LocalDate date, List<ScoreDetail> entries) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        this.date = (date != null) ? date.format(formatter) : null;
        this.entries = entries;
    }
}
