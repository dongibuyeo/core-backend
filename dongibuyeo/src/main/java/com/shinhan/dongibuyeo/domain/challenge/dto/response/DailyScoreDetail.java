package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import com.shinhan.dongibuyeo.domain.challenge.entity.ScoreEntry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class DailyScoreDetail {
    private String date;
    private List<ScoreEntry> entries;
}
